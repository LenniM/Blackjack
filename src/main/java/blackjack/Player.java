package blackjack;

import java.io.Serializable;

public class Player extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    // Guthaben, das dem Spieler aktuell zur Verfuegung steht.
    private int chips;

    // Der Einsatz der laufenden Runde. Bei Split/Double kann er sich erhoehen.
    private int currentBet;

    // Statistik ueber alle Runden hinweg, unabhaengig vom aktuellen Guthaben.
    private int totalWins;
    private int totalLosses;

    // Kennzeichnung, ob es sich um einen temporaeren Gast-Account handelt.
    private boolean guest;

    public Player(String username, String password, int startChips) {
        // Account uebernimmt Benutzername, Passwort, ID und Erstellzeitpunkt.
        super(username, password);

        // Ein negatives Startguthaben waere fachlich nicht sinnvoll.
        if (startChips < 0) {
            throw new IllegalArgumentException("Startguthaben darf nicht negativ sein.");
        }
        this.chips = startChips;
        this.guest = false;
    }

    // Spezieller Konstruktor fuer unangemeldete Gast-Spieler.
    public Player(String guestName, int startChips) {
        super(guestName, "");
        if (startChips < 0) {
            throw new IllegalArgumentException("Startguthaben darf nicht negativ sein.");
        }
        this.chips = startChips;
        this.guest = true;
    }

    public void placeBet(int amount) {
        // Ein Einsatz muss positiv sein und darf das Guthaben nicht uebersteigen.
        if (amount <= 0) {
            throw new IllegalArgumentException("Einsatz muss groesser als 0 sein.");
        }
        if (!canAfford(amount)) {
            throw new IllegalArgumentException("Nicht genug Chips fuer diesen Einsatz.");
        }
        // Pruefung gegen das im Account eingestellte maximale Einsatzlimit pro Runde.
        if (getMaxBetLimit() > 0 && amount > getMaxBetLimit()) {
            throw new IllegalArgumentException("Einsatz uebersteigt das maximale Rundenlimit von " + getMaxBetLimit() + " Chips.");
        }

        // Der Einsatz wird sofort vom Guthaben abgezogen und separat gemerkt.
        chips -= amount;
        currentBet += amount;
    }

    public void winBet(int profitAmount) {
        // Gewinn: Der urspruengliche Einsatz plus der Gewinnbetrag gehen ans Guthaben.
        if (profitAmount < 0) {
            throw new IllegalArgumentException("Gewinn darf nicht negativ sein.");
        }
        chips += currentBet + profitAmount;
        totalWins++;
        currentBet = 0;
    }

    public void loseBet() {
        // Verlust: Der Einsatz wurde bereits bei placeBet() abgezogen, hier nur verbuchen.
        totalLosses++;
        currentBet = 0;
    }

    public void pushBet() {
        // Unentschieden (Push): Der Spieler bekommt genau seinen Einsatz zurueck.
        chips += currentBet;
        currentBet = 0;
    }

    public boolean canAfford(int amount) {
        return chips >= amount;
    }

    public int getChips() {
        return chips;
    }

    // Erlaubt das Aufladen des Chip-Guthabens im Einstellungsmenue.
    public void addChips(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Aufladebetrag muss groesser als 0 sein.");
        }
        this.chips += amount;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public boolean isGuest() {
        return guest;
    }
}