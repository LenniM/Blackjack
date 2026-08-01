package blackjack;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void konstruktor_erstelltDeckMit52Karten() {
        Deck deck = new Deck(1);
        assertEquals(52, deck.size());
        assertFalse(deck.isEmpty());
    }

    @Test
    void konstruktor_erstelltMehrereDecksMitKorrekterKartenzahl() {
        Deck deck = new Deck(8);
        assertEquals(416, deck.size());
    }

    @Test
    void konstruktor_wirftExceptionBeiNullDecks() {
        assertThrows(IllegalArgumentException.class, () -> new Deck(0));
    }

    @Test
    void konstruktor_wirftExceptionBeiNegativerDeckAnzahl() {
        assertThrows(IllegalArgumentException.class, () -> new Deck(-1));
    }

    @Test
    void draw_gibtKarteZurueckUndReduziertDeckgroesse() {
        Deck deck = new Deck(1);
        Card card = deck.draw();
        assertNotNull(card);
        assertEquals(51, deck.size());
        assertFalse(deck.isEmpty());
    }

    @Test
    void draw_wirftExceptionWennDeckLeerIst() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertTrue(deck.isEmpty());
        assertThrows(NoSuchElementException.class, deck::draw);
    }

    @Test
    void refresh_stelltVollstaendigesDeckWiederHer() {
        Deck deck = new Deck(1);
        deck.draw();
        deck.draw();
        deck.refresh();
        assertEquals(52, deck.size());
        assertFalse(deck.isEmpty());
    }
}