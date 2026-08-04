package blackjack;

import java.time.LocalDateTime;
import java.util.List;

public class Blackjack extends Game {
    // Der Dealer zieht nach Standardregel bis mindestens 17 Punkte erreicht sind.
    private static final int DEALER_STANDS_AT = 17;

    private final UserInterface ui;
    private final Player player;
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
        this.deck = new Deck(8); // Erzeugt standardmäßig einen Kartensatz aus 8 gemischten Decks.
        this.minBet = 10;
        this.maxBet = 500;
    }

    @Override
    public void start() {
        // Ein Start spielt die Haupt-Menueschleife ab.
        state = "RUNNING";
        startTime = LocalDateTime.now();
        sessionLoop();
        state = "FINISHED";
    }

    @Override
    public void restart() {
        // Neues Deck mischen und danach wieder starten.
        deck.refresh();
        start();
    }

    private void sessionLoop() {
        // Menueschleife waehrend der laufenden Spielsession.
        boolean inSession = true;
        while (inSession) {
            String choice = ui.askSessionChoice(player);

            // Option 1: Eine neue Spielrunde starten.
            if ("1".equals(choice)) {
            	// Prüft vor Rundenstart, ob ausreichend Chips für den Mindesteinsatz vorliegen.
                if (player.getChips() < minBet) {
                    ui.displayMessage("Nicht genug Chips für den Mindesteinsatz (" + minBet + "). Bitte Guthaben aufladen!");
                } else {
                	// Startet den regulären Ablauf einer einzelnen Blackjack-Runde.
                    playSingleRound();
                }
            // Option 2: Account-Einstellungen verwalten.
            } else if ("2".equals(choice)) {
                openSettingsMenu();
            // Option 3: Rückkehr ins Hauptmenü der Anwendung.
            } else if ("3".equals(choice)) {
                inSession = false;
            } else {
                ui.displayMessage("Ungueltige Option.");
            }
        }
    }

    private void openSettingsMenu() {
        // Untermenü für Statistiken, Rundenlimit und Aufladen.
        boolean inSettings = true;
        while (inSettings) {
        	// Blendet das Untermenü ein und erfasst die Eingabe des Benutzers.
            String choice = ui.askSettingsChoice();
         // Blendet die detaillierten Kontostatistiken ein.
            if ("1".equals(choice)) {
                ui.displayStats(player);
            } else if ("2".equals(choice)) {	// Leitet den Prozess zur Festlegung eines Rundenlimits ein.
                handleSetBetLimit();
            } else if ("3".equals(choice)) {	// Öffnet den Dialog zum erhöhen des Chip-Guthabens.
                handleRechargeChips();
            } else if ("4".equals(choice)) {	// Verlässt das Einstellungsuntermenü.
                inSettings = false;
            } else {
                ui.displayMessage("Ungueltige Auswahl.");
            }
        }
    }

    private void handleSetBetLimit() {
        // Fragt das neue Rundenlimit ab und setzt es im Account.
        try {
            int limit = ui.askNewMaxBetLimit(player.getMaxBetLimit());
            player.setMaxBetLimit(limit); // Speichert das eingegebene Limit im Account-Objekt ab.
         // Gibt die Bestätigung über das geänderte Limit aus.
            ui.displayMessage("Rundenlimit erfolgreich auf " + (limit > 0 ? limit + " Chips" : "kein Limit") + " gesetzt.");
        } catch (IllegalArgumentException e) {
            ui.displayMessage("Fehler: " + e.getMessage()); // Reagiert auf ungültige Betragseingaben mit einer Hinweismeldung.
        }
    }

    private void handleRechargeChips() {
        // Erlaubt das freie Aufladen des Chip-Guthabens.
        try {
            int amount = ui.askRechargeAmount(); 	// Fragt den aufzuladenden Chip-Betrag vom Spieler ab.
            player.addChips(amount);
            ui.displayMessage("Erfolgreich " + amount + " Chips aufgeladen. Neuer Kontostand: " + player.getChips() + " Chips.");
        } catch (IllegalArgumentException e) {
            ui.displayMessage("Fehler: " + e.getMessage()); // Reagiert auf ungültige Betragseingaben mit einer Hinweismeldung.
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
        // Fuehrt eine einzelne Spielrunde durch und zieht den Einsatz ab.
        int bet = 0;
        while (true) {
            bet = ui.askBet(minBet, maxBet);
            try {
                player.placeBet(bet);	// Bucht den gewählten Einsatz vom Chip-Konto ab.
                break;
            } catch (IllegalArgumentException e) {
                ui.displayMessage("Fehler bei Einsatzplatzierung: " + e.getMessage());	// Informiert bei Limit- oder Guthabenüberschreitung.
            }
        }

        // Es gibt genau eine Spielerhand und eine Dealerhand.
        playerHand = new Hand();
        dealerHand = new Hand();
        playerHand.setBet(bet);

        // Klassischer Start: Spieler, Dealer, Spieler, Dealer.
        // Die zweite Dealerkarte wird spaeter verdeckt angezeigt.
        playerHand.addCard(drawCard());
        dealerHand.addCard(drawCard());
        playerHand.addCard(drawCard());
        dealerHand.addCard(drawCard());

        playerTurn(); 	// Führt die interaktive Zugphase des Spielers aus.

        // Der Dealer zieht nur, wenn der Spieler nicht bereits überkauft hat
        if (!playerHand.isBusted()) {	
            dealerPlay();
        }

        evaluateRound();
    }

    private void playerTurn() {
        // Der Spieler darf solange handeln, bis er steht, bustet oder Blackjack hat.
        while ("RUNNING".equals(state) && "ACTIVE".equals(playerHand.getStatus())) {
        	
        	// Stellt die aktuelle Spielsituation auf der Konsole dar.
        	ui.displayDealerHand(dealerHand, true);
            ui.displayHand(playerHand, player.getUsername());
            
            // Beendet die Entscheidungsphase sofort bei einem Natural Blackjack.
            if (playerHand.isBlackjack()) {
                return;
            }

            // Holt die gewählte Spielaktion ("HIT" oder "STAND") ein.
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
        // Erst am Ende werden die finalen Werte verglichen.
        int playerValue = playerHand.calculateValue();
        int dealerValue = dealerHand.calculateValue();

        if (playerHand.isBusted()) {
            player.loseBet(); // Einsatz verfaellt
            ui.displayRoundResult(playerHand, dealerHand, "BUST", player.getUsername());
        } else if (dealerHand.isBusted()) {
            player.winBet(playerHand.getBet()); // Regulaerer Sieg 1:1
            ui.displayRoundResult(playerHand, dealerHand, "DEALER_BUST", player.getUsername());
        } else if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            player.winBet((int) (playerHand.getBet() * 1.5)); // Natural Blackjack 3:2
            ui.displayRoundResult(playerHand, dealerHand, "BLACKJACK", player.getUsername());
        } else if (dealerHand.isBlackjack() && !playerHand.isBlackjack()) {
            player.loseBet(); // Einsatz verfaellt
            ui.displayRoundResult(playerHand, dealerHand, "DEALER_BLACKJACK", player.getUsername());
        } else if (playerValue > dealerValue) {
            player.winBet(playerHand.getBet()); // Regulaerer Sieg 1:1
            ui.displayRoundResult(playerHand, dealerHand, "WIN", player.getUsername());
        } else if (playerValue < dealerValue) {
            player.loseBet(); // Einsatz verfaellt
            ui.displayRoundResult(playerHand, dealerHand, "LOSE", player.getUsername());
        } else {
            player.pushBet(); // Push erstattet Einsatz
            ui.displayRoundResult(playerHand, dealerHand, "PUSH", player.getUsername());
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