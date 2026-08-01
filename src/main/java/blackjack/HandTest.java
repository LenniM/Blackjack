package blackjack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    @Test
    void neueHand_istLeerUndAktiv() {
        Hand hand = new Hand();

        assertTrue(hand.getCards().isEmpty());
        assertEquals(0, hand.calculateValue());
        assertEquals(0, hand.getBet());
        assertEquals("ACTIVE", hand.getStatus());
        assertFalse(hand.isBusted());
        assertFalse(hand.isBlackjack());
        assertFalse(hand.isSoft());
        assertFalse(hand.isDoubled());
        assertFalse(hand.isFromSplit());
    }

    @Test
    void addCard_fuegtKarteZurHandHinzu() {
        Hand hand = new Hand();
        Card card = new Card("4H");

        hand.addCard(card);

        assertEquals(1, hand.getCards().size());
        assertEquals(card, hand.getCards().getFirst());
    }

    @Test
    void calculateValue_berechnetWertOhneAssKorrekt() {
        Hand hand = new Hand();

        hand.addCard(new Card("10H"));
        hand.addCard(new Card("7K"));

        assertEquals(17, hand.calculateValue());
    }

    @Test
    void calculateValue_berechnetBildkartenAlsZehn() {
        Hand hand = new Hand();

        hand.addCard(new Card("BH"));
        hand.addCard(new Card("DK"));
        hand.addCard(new Card("KK"));

        assertEquals(30, hand.calculateValue());
    }

    @Test
    void calculateValue_zaehltAssAlsElfWennMoeglich() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("7K"));

        assertEquals(18, hand.calculateValue());
    }

    @Test
    void calculateValue_zaehltAssAlsEinsWennSonstUeberkauft() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("9K"));
        hand.addCard(new Card("5K"));

        assertEquals(15, hand.calculateValue());
    }

    @Test
    void calculateValue_behandeltMehrereAsseKorrekt() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("AK"));
        hand.addCard(new Card("9K"));

        assertEquals(21, hand.calculateValue());
    }

    @Test
    void isBusted_gibtTrueBeiWertUeber21Zurueck() {
        Hand hand = new Hand();

        hand.addCard(new Card("10H"));
        hand.addCard(new Card("9K"));
        hand.addCard(new Card("5K"));

        assertTrue(hand.isBusted());
        assertEquals("BUSTED", hand.getStatus());
    }

    @Test
    void isBusted_gibtFalseBeiWert21Zurueck() {
        Hand hand = new Hand();

        hand.addCard(new Card("10H"));
        hand.addCard(new Card("7K"));
        hand.addCard(new Card("4K"));

        assertFalse(hand.isBusted());
        assertEquals(21, hand.calculateValue());
    }

    @Test
    void isBlackjack_gibtTrueBeiZweiKartenMitWert21Zurueck() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("KH"));

        assertTrue(hand.isBlackjack());
        assertEquals("BLACKJACK", hand.getStatus());
    }

    @Test
    void isBlackjack_gibtFalseBeiDreiKartenMitWert21Zurueck() {
        Hand hand = new Hand();

        hand.addCard(new Card("7H"));
        hand.addCard(new Card("7K"));
        hand.addCard(new Card("7P"));

        assertFalse(hand.isBlackjack());
        assertEquals(21, hand.calculateValue());
    }

    @Test
    void isSoft_gibtTrueWennAssAlsElfZaehlt() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("6K"));

        assertTrue(hand.isSoft());
    }

    @Test
    void isSoft_gibtFalseWennAssAlsEinsZaehlt() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("9K"));
        hand.addCard(new Card("8K"));

        assertFalse(hand.isSoft());
    }

    @Test
    void isSoft_gibtFalseOhneAssZurueck() {
        Hand hand = new Hand();

        hand.addCard(new Card("10H"));
        hand.addCard(new Card("7K"));

        assertFalse(hand.isSoft());
    }

    @Test
    void countAces_zaehltAlleAsse() {
        Hand hand = new Hand();

        hand.addCard(new Card("AH"));
        hand.addCard(new Card("AK"));
        hand.addCard(new Card("7K"));

        assertEquals(2, hand.countAces());
    }


    // Hinweis: Die Methode Split wurde noch nicht implementiert.
    @Test
    void split_wirftUnsupportedOperationException() {
        Hand hand = new Hand();

        assertThrows(UnsupportedOperationException.class, () -> hand.split());
    }

    @Test
    void getCards_gibtKartenlisteZurueck() {
        Hand hand = new Hand();
        Card firstCard = new Card("4H");
        Card secondCard = new Card("9K");

        hand.addCard(firstCard);
        hand.addCard(secondCard);

        List<Card> cards = hand.getCards();

        assertEquals(List.of(firstCard, secondCard), cards);
    }

    @Test
    void setBet_speichertGueltigenEinsatz() {
        Hand hand = new Hand();

        hand.setBet(100);

        assertEquals(100, hand.getBet());
    }

    @Test
    void setBet_wirftExceptionBeiNegativemEinsatz() {
        Hand hand = new Hand();

        assertThrows(IllegalArgumentException.class, () -> hand.setBet(-1));
    }

    @Test
    void setStatus_speichertStatus() {
        Hand hand = new Hand();

        hand.setStatus("STAND");

        assertEquals("STAND", hand.getStatus());
    }

    @Test
    void setDoubled_speichertDoubleStatus() {
        Hand hand = new Hand();

        hand.setDoubled(true);

        assertTrue(hand.isDoubled());
    }
}