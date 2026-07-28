package blackjack;

public class Player extends Account {
    private static final long serialVersionUID = 1L;

    // Guthaben, das dem Spieler aktuell zur Verfuegung steht.
    private int chips;

    // Der Einsatz der laufenden Runde. Bei Split/Double kann er sich erhoehen.
    private int currentBet;

    // Statistik ueber alle Runden hinweg, unabhaengig vom aktuellen Guthaben.
    private int totalWins;
    private int totalLosses;

    public Player(String username, String password, int startChips) {
        // Account uebernimmt Benutzername, Passwort, ID und Erstellzeitpunkt.
        super(username, password);

        // Ein negatives Startguthaben waere fachlich nicht sinnvoll.
        if (startChips < 0) {
            throw new IllegalArgumentException("Startguthaben darf nicht negativ sein.");
        }
        this.chips = startChips;
    }

    public void placeBet(int amount) {
        // Ein Einsatz muss positiv sein und darf das Guthaben nicht uebersteigen.
        if (amount <= 0) {
            throw new IllegalArgumentException("Einsatz muss groesser als 0 sein.");
        }
        if (!canAfford(amount)) {
            throw new IllegalArgumentException("Nicht genug Chips fuer diesen Einsatz.");
        }

        // Der Einsatz wird sofort vom Guthaben abgezogen und separat gemerkt.
        chips -= amount;
        currentBet += amount;
    }

    public void winBet(int amount) {
        // Gewinn: Der urspruengliche Einsatz plus der Gewinnbetrag gehen ans Guthaben.
        if (amount < 0) {
            throw new IllegalArgumentException("Gewinn darf nicht negativ sein.");
        }
        chips += currentBet + amount;
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

    public int getCurrentBet() {
        return currentBet;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }
}
