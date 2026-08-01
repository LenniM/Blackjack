package blackjack;

import java.time.LocalDateTime;
import java.util.List;

public class Blackjack extends Game {
    // Der Dealer zieht nach Standardregel bis mindestens 17 Punkte erreicht sind.
    private static final int DEALER_STANDS_AT = 17;

    private final UserInterface ui;
    private final Player player; // Der angemeldete oder Gast-Spieler
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private final int minBet;
    private final int maxBet;

    public Blackjack(UserInterface ui, Player player) {
        // Standard-Konstruktor mit direkten Default-Werten.
        super();
        this.ui = ui;
        this.player = player;
        this.deck = new Deck(8);
        this.minBet = 10;
        this.maxBet = 500;
    }

    @Override
    public void start() {
        // Startet die Hauptschleife fuer den Spielbetrieb.
        state = "RUNNING";
        startTime = LocalDateTime.now();
        gameLoop();
        state = "FINISHED";
    }

    @Override
    public void restart() {
        // Neues Deck mischen und danach wieder starten.
        deck.refresh();
        start();
    }

    private void gameLoop() {
        // Menueschleife waehrend der laufenden Spielsession.
        boolean continuePlaying = true;
        while (continuePlaying) {
            ui.displayMessage("\n-------------------------------------------");
            ui.displayMessage("Spieler: " + player.getUsername() + " | Guthaben: " + player.getChips() + " Chips");
            if (player.getMaxBetLimit() > 0) {
                ui.displayMessage("Max. Rundenlimit: " + player.getMaxBetLimit() + " Chips");
            }
            ui.displayMessage("-------------------------------------------");
            ui.displayMessage("[1] Runde spielen | [2] Einstellungen (Rundenlimit) | [3] Hauptmenue");

            String choice = ui.askInput("Auswahl:");
            if ("1".equals(choice)) {
                // Automatisches Aufladen bei Bankrott.
                if (player.getChips() < minBet) {
                    ui.displayMessage("Nicht genug Chips! Mindesteinsatz ist " + minBet + ". Aufladen mit 500 Chips...");
                    player.addChips(500);
                }
                playSingleRound();
            } else if ("2".equals(choice)) {
                configureSettings();
            } else if ("3".equals(choice)) {
                continuePlaying = false;
            } else {
                ui.displayMessage("Ungueltige Option.");
            }
        }
    }

    private void configureSettings() {
        // Einstellungen waehrend der Runden: Maximales Einsatzlimit pro Runde anpassen.
        ui.displayMessage("\n=== ACCOUNT EINSTELLUNGEN ===");
        ui.displayMessage("Aktuelles maximales Rundenlimit: " + (player.getMaxBetLimit() > 0 ? player.getMaxBetLimit() + " Chips" : "Kein Limit"));
        String input = ui.askInput("Neues maximales Einsatzlimit pro Runde eingeben (0 fuer kein Limit):");
        try {
            int limit = Integer.parseInt(input);
            player.setMaxBetLimit(limit);
            ui.displayMessage("Maximales Einsatzlimit pro Runde erfolgreich auf " + (limit > 0 ? limit + " Chips" : "kein Limit") + " gesetzt.");
        } catch (IllegalArgumentException e) {
            ui.displayMessage("Fehler: " + e.getMessage());
        }
    }

    public void hit(Hand hand) {
        // HIT bedeutet: eine weitere Karte fuer die angegebene Hand.
        hand.addCard(drawCard());
    }

    public void stand() {
        // STAND beendet den Spielerzug ohne weitere Karte.
        playerHand.setStatus("STAND");
    }

    private void playSingleRound() {
        // Führt eine einzelne Spielrunde durch und zieht den Einsatz ab.
        int bet = 0;
        while (true) {
            bet = ui.askBet(minBet, maxBet);
            try {
                player.placeBet(bet);
                break;
            } catch (IllegalArgumentException e) {
                ui.displayMessage("Fehler bei Einsatzplatzierung: " + e.getMessage());
            }
        }

        playerHand = new Hand();
        dealerHand = new Hand();
        playerHand.setBet(bet);

        // Klassischer Start: Spieler, Dealer, Spieler, Dealer.
        playerHand.addCard(drawCard());
        dealerHand.addCard(drawCard());
        playerHand.addCard(drawCard());
        dealerHand.addCard(drawCard());

        playerTurn();

        if (!playerHand.isBusted()) {
            dealerPlay();
        }

        evaluateRound();
    }

    private void playerTurn() {
        // Der Spieler darf solange handeln, bis er steht, bustet oder Blackjack hat.
        while ("RUNNING".equals(state) && "ACTIVE".equals(playerHand.getStatus())) {
            ui.displayDealerHand(dealerHand, true);
            ui.displayHand(playerHand, player.getUsername());

            if (playerHand.isBlackjack()) {
                return;
            }

            String action = ui.askAction(List.of("HIT", "STAND"));
            if ("HIT".equals(action)) {
                hit(playerHand);
            } else {
                stand();
            }
        }
    }

    private void dealerPlay() {
        // Nach dem Spielerzug deckt der Dealer seine verdeckte Karte auf.
        ui.displayMessage("Dealer deckt auf.");
        ui.displayDealerHand(dealerHand, false);

        // Dealer zieht automatisch bis mindestens 17 Punkte.
        while (dealerHand.calculateValue() < DEALER_STANDS_AT) {
            dealerHand.addCard(drawCard());
            ui.displayDealerHand(dealerHand, false);
        }
    }

    private void evaluateRound() {
        // Wertet das Ergebnis aus und verbucht Gewinn/Verlust auf dem Spielerobjekt.
        int playerValue = playerHand.calculateValue();
        int dealerValue = dealerHand.calculateValue();

        ui.displayMessage("\nEndstand:");
        ui.displayHand(playerHand, player.getUsername());
        ui.displayDealerHand(dealerHand, false);

        if (playerHand.isBusted()) {
            ui.displayMessage("Spieler verliert: ueber 21.");
            player.loseBet();
        } else if (dealerHand.isBusted()) {
            ui.displayMessage("Spieler gewinnt (1:1): Dealer ist ueber 21.");
            player.winBet(playerHand.getBet()); // 1:1 Gewinn
        } else if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            ui.displayMessage("Natural Blackjack! Spieler gewinnt im Verhältnis 3:2.");
            player.winBet((int) (playerHand.getBet() * 1.5)); // 3:2 Gewinn
        } else if (dealerHand.isBlackjack() && !playerHand.isBlackjack()) {
            ui.displayMessage("Dealer hat Blackjack. Spieler verliert.");
            player.loseBet();
        } else if (playerValue > dealerValue) {
            ui.displayMessage("Spieler gewinnt (1:1).");
            player.winBet(playerHand.getBet()); // 1:1 Gewinn
        } else if (playerValue < dealerValue) {
            ui.displayMessage("Dealer gewinnt.");
            player.loseBet();
        } else {
            ui.displayMessage("Unentschieden (Push). Einsatz wird erstattet.");
            player.pushBet();
        }
    }

    private Card drawCard() {
        // Falls das Deck leer ist, wird es automatisch erneuert.
        if (deck.isEmpty()) {
            deck.refresh();
        }
        return deck.draw();
    }
}