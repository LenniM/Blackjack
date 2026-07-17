package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hand implements Serializable {
    private static final long serialVersionUID = 1L;

    // Handstatus als String, damit keine Enums verwendet werden.
    // Erlaubte Werte: "ACTIVE", "STAND", "BUSTED", "BLACKJACK", "SURRENDERED".
    private final ArrayList<Card> cards = new ArrayList<>();
    private int bet;
    private String status = "ACTIVE";
    private boolean doubled;
    private boolean fromSplit;

    public void addCard(Card card) {
        // Jede neue Karte kann den Status der Hand veraendern.
        cards.add(card);
        refreshStatus();
    }

    public int calculateValue() {
        // Karten liefern ihren Standardwert:
        // Zahlenkarten 2-10, Bildkarten 10, Ass erst einmal 11.
        int sum = 0;
        for (Card card : cards) {
            sum += card.getValue();
        }
        int aceCount = countAces();

        // Ass-Sonderfall:
        // Solange die Hand ueber 21 ist, wird je ein Ass von 11 auf 1 umgerechnet.
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }

        return sum;
    }

    public boolean isBusted() {
        return calculateValue() > 21;
    }

    public boolean isBlackjack() {
        // Ein echter Blackjack besteht aus genau zwei Karten und zaehlt 21.
        // Nach einem Split wird diese Hand hier bewusst nicht als Blackjack gewertet.
        return cards.size() == 2 && calculateValue() == 21 && !fromSplit;
    }

    public boolean isSoft() {
        // Eine Soft-Hand enthaelt mindestens ein Ass, das aktuell als 11 zaehlt.
        int sum = 0;
        for (Card card : cards) {
            sum += card.getValue();
        }
        return countAces() > 0 && sum <= 21;
    }

    public int countAces() {
        int aceCount = 0;
        for (Card card : cards) {
            if (card.isAce()) {
                aceCount++;
            }
        }
        return aceCount;
    }

    public Hand split() {
        // Split ist im Prototyp vorbereitet, wird aber im Spielablauf noch nicht benutzt und ist noch nicht implementiert.
        throw new UnsupportedOperationException("Split ist noch nicht implementiert.");
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        // Negative Einsaetze sind fachlich ungueltig.
        if (bet < 0) {
            throw new IllegalArgumentException("Einsatz darf nicht negativ sein.");
        }
        this.bet = bet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDoubled() {
        return doubled;
    }

    public void setDoubled(boolean doubled) {
        this.doubled = doubled;
    }

    public boolean isFromSplit() {
        return fromSplit;
    }

    private void refreshStatus() {
        // Automatische Statuspflege nach dem Ziehen einer Karte.
        if (isBusted()) {
            status = "BUSTED";
        } else if (isBlackjack()) {
            status = "BLACKJACK";
        }
    }
}
