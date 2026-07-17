package blackjack;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public final class Card implements Serializable {
    private static final long serialVersionUID = 1L;

    // Gueltige Rang-Codes nach der deutschen Vorgabe.
    private static final Set<String> VALID_RANKS = Set.of(
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "B", "D", "K", "A"
    );

    // Gueltige Farb-Codes:
    // H = Herz, K = Karo, P = Pik, Z = Kreuz.
    private static final Set<String> VALID_SUITS = Set.of("H", "K", "P", "Z");

    // Vollstaendige Kartenanzeige, z.B. "4H", "10K" oder "AP".
    private final String displayCard;

    // Standardwert der Karte. Ein Ass startet immer mit 11.
    // Die Umwertung auf 1 passiert spaeter in Hand.calculateValue().
    private final int value;

    public Card(String displayCard) {
        // Null, leere Strings und reine Leerzeichen sind keine gueltigen Karten.
        if (displayCard == null || displayCard.isBlank()) {
            throw new IllegalArgumentException("Karte darf nicht leer sein.");
        }

        // Eingaben wie " ap " werden akzeptiert und intern als "AP" gespeichert.
        String normalized = displayCard.trim().toUpperCase();
        String rank = extractRank(normalized);
        String suit = extractSuit(normalized);

        // Die Notation ist nur gueltig, wenn Rang und Farbe bekannt sind.
        if (!VALID_RANKS.contains(rank) || !VALID_SUITS.contains(suit)) {
            throw new IllegalArgumentException("Ungueltige Karten-Notation: " + displayCard);
        }

        this.displayCard = normalized;
        this.value = calculateDefaultValue(rank);
    }

    public String getDisplayCard() {
        return displayCard;
    }

    public int getValue() {
        return value;
    }

    public boolean isAce() {
        // Diese Hilfsmethode macht die Ass-Logik in Hand besser lesbar.
        return "A".equals(getRank());
    }

    public String getRank() {
        // Der Rang ist alles ausser dem letzten Zeichen.
        return extractRank(displayCard);
    }

    public String getSuit() {
        // Die Farbe ist immer das letzte Zeichen.
        return extractSuit(displayCard);
    }

    @Override
    public String toString() {
        return displayCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        Card card = (Card) o;
        return displayCard.equals(card.displayCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayCard);
    }

    private static String extractRank(String displayCard) {
        return displayCard.substring(0, displayCard.length() - 1);
    }

    private static String extractSuit(String displayCard) {
        return displayCard.substring(displayCard.length() - 1);
    }

    private static int calculateDefaultValue(String rank) {
        // Bildkarten zaehlen 10, Ass zaehlt standardmaessig 11.
        if ("B".equals(rank) || "D".equals(rank) || "K".equals(rank)) {
            return 10;
        }
        if ("A".equals(rank)) {
            return 11;
        }
        return Integer.parseInt(rank);
    }
}
