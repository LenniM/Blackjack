package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Deck implements Serializable {
    private static final long serialVersionUID = 1L;

    // Reihenfolge ist fuer die Erstellung egal, weil danach gemischt wird.
    private static final List<String> SUITS = List.of("H", "K", "P", "Z");
    private static final List<String> RANKS = List.of(
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "B", "D", "K", "A"
    );

    // ArrayList passt hier gut, weil Karten am Ende entfernt werden.
    private final ArrayList<Card> cards = new ArrayList<>();
    private final int numberOfDecks;
    private final Random random;

    public Deck(int numberOfDecks) {
        // Blackjack wird oft mit mehreren Decks gespielt, aber mindestens eins ist noetig.
        if (numberOfDecks <= 0) {
            throw new IllegalArgumentException("numberOfDecks muss groesser als 0 sein.");
        }
        this.numberOfDecks = numberOfDecks;
        this.random = new Random();
        initialize();
        shuffle();
    }

    public void initialize() {
        // Erst leeren, damit initialize() auch fuer refresh() wiederverwendet werden kann.
        cards.clear();
        for (int deckIndex = 0; deckIndex < numberOfDecks; deckIndex++) {
            for (String suit : SUITS) {
                for (String rank : RANKS) {
                    cards.add(new Card(rank + suit));
                }
            }
        }
    }

    public void shuffle() {
        // Collections.shuffle nutzt den Random dieses Decks.
        Collections.shuffle(cards, random);
    }

    public Card draw() {
        // Ziehen aus einem leeren Deck waere ein Programmierfehler.
        // Blackjack.drawCard() erneuert das Deck vorher automatisch.
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Das Deck ist leer.");
        }
        return cards.remove(cards.size() - 1);
    }

    public void refresh() {
        // Neues vollstaendiges Deck erzeugen und direkt mischen.
        initialize();
        shuffle();
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
