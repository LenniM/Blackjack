package blackjack;

import java.time.LocalDateTime;
import java.util.List;

public class Blackjack extends Game {
    // Der Dealer zieht nach Standardregel bis mindestens 17 Punkte erreicht sind.
    private static final int DEALER_STANDS_AT = 17;

    private final UserInterface ui;
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private final int minBet;
    private final int maxBet;

    public Blackjack(UserInterface ui) {
        // Standard-Konstruktor mit direkten Default-Werten.
        super();
        this.ui = ui;
        this.deck = new Deck(8);
        this.minBet = 10;
        this.maxBet = 500;
    }

    @Override
    public void start() {
        // Ein Start spielt im Prototyp genau eine Runde.
        state = "RUNNING";
        startTime = LocalDateTime.now();
        playSingleRound();
        state = "FINISHED";
    }

    @Override
    public void restart() {
        // Neues Deck mischen und danach wieder eine einzelne Runde starten.
        deck.refresh();
        start();
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
        ui.displayMessage("Blackjack-Prototyp gestartet.");
        int bet = ui.askBet(minBet, maxBet);

        // Fuer den ersten Prototyp gibt es genau eine Spielerhand und eine Dealerhand.
        playerHand = new Hand();
        dealerHand = new Hand();
        playerHand.setBet(bet);

        // Klassischer Start: Spieler, Dealer, Spieler, Dealer.
        // Die zweite Dealerkarte wird spaeter verdeckt angezeigt.
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
            ui.displayHand(playerHand, "Spieler");

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
        // Erst am Ende werden die finalen Werte verglichen.
        int playerValue = playerHand.calculateValue();
        int dealerValue = dealerHand.calculateValue();

        ui.displayMessage("Endstand:");
        ui.displayHand(playerHand, "Spieler");
        ui.displayDealerHand(dealerHand, false);

        if (playerHand.isBusted()) {
            ui.displayMessage("Spieler verliert: ueber 21.");
        } else if (dealerHand.isBusted()) {
            ui.displayMessage("Spieler gewinnt: Dealer ist ueber 21.");
        } else if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            ui.displayMessage("Blackjack! Spieler gewinnt.");
        } else if (dealerHand.isBlackjack() && !playerHand.isBlackjack()) {
            ui.displayMessage("Dealer hat Blackjack. Spieler verliert.");
        } else if (playerValue > dealerValue) {
            ui.displayMessage("Spieler gewinnt.");
        } else if (playerValue < dealerValue) {
            ui.displayMessage("Dealer gewinnt.");
        } else {
            ui.displayMessage("Unentschieden.");
        }
    }

    private Card drawCard() {
        // Falls das Deck leer ist, wird es fuer den Prototyp automatisch erneuert.
        if (deck.isEmpty()) {
            deck.refresh();
        }
        return deck.draw();
    }
}
