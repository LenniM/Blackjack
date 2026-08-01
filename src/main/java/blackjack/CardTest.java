package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void konstruktor_akzeptiertGueltigeKarte() {
        Card card = new Card("4H");
        assertEquals("4H", card.getDisplayCard());
        assertEquals(4, card.getValue());
    }

    @Test
    void konstruktor_normalisiertEingabe() {
        Card card = new Card("  4h  ");
        assertEquals("4H", card.getDisplayCard());
    }

    @Test
    void konstruktor_akzeptiertZehnerKarte() {
        Card card = new Card("10K");
        assertEquals("10K", card.getDisplayCard());
        assertEquals(10, card.getValue());
    }

    @Test
    void konstruktor_wirftExceptionBeiLeererEingabe() {
        assertThrows(IllegalArgumentException.class, () -> new Card(""));
    }

    @Test
    void konstruktor_wirftExceptionBeiNullEingabe() {
        assertThrows(IllegalArgumentException.class, () -> new Card(null));
    }

    @Test
    void konstruktor_wirftExceptionBeiUngueltigemRang() {
        assertThrows(IllegalArgumentException.class, () -> new Card("1H"));
    }

    @Test
    void konstruktor_wirftExceptionBeiUngueltigerFarbe() {
        assertThrows(IllegalArgumentException.class, () -> new Card("4X"));
    }

    @Test
    void getValue_gibtRichtigeWerteFuerRaengeZurueck() {
        assertEquals(10, new Card("BH").getValue());
        assertEquals(10, new Card("DH").getValue());
        assertEquals(10, new Card("KH").getValue());
        assertEquals(7, new Card("7H").getValue());
    }

    @Test
    void toString_gibtNormalisierteKartenanzeigeZurueck() {
        Card card = new Card("  ah  ");
        assertEquals("AH", card.toString());
    }

    @Test
    void equals_gibtTrueBeiGleicherKarteZurueck() {
        Card firstCard = new Card("4H");
        Card secondCard = new Card("4H");
        assertEquals(firstCard, secondCard);
    }

    @Test
    void equals_gibtTrueBeiGleicherKarteMitAndererSchreibweiseZurueck() {
        Card firstCard = new Card("4h");
        Card secondCard = new Card("4H");
        assertEquals(firstCard, secondCard);
    }

    @Test
    void equals_gibtFalseBeiUngleicherKarteZurueck() {
        Card firstCard = new Card("4H");
        Card secondCard = new Card("5H");
        assertNotEquals(firstCard, secondCard);
    }

    @Test
    void equals_gibtFalseBeiNullUndAnderemObjekttypZurueck() {
        Card card = new Card("4H");
        assertNotEquals(null, card);
        assertNotEquals("4H", card);
    }

    @Test
    void hashCode_gleicheKartenHabenGleichenHashCode() {
        Card firstCard = new Card("4h");
        Card secondCard = new Card("4H");

        assertEquals(firstCard.hashCode(), secondCard.hashCode());
    }

    @Test
    void isAce_gibtNurBeiAssTrueZurueck() {
        assertTrue(new Card("AH").isAce());
        assertFalse(new Card("KH").isAce());
    }
}