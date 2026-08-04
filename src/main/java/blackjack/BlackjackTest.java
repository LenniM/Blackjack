package blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackTest {

    private Player player;
    private TestUI ui;
    private Blackjack blackjack;

    // Ein Mock-UI, das vordefinierte Eingaben aus Listen zurueckgibt
    // und letzte Ausgaben speichert, um sie in den Tests zu verifizieren.
    static class TestUI implements UserInterface {
        List<String> sessionChoices = new ArrayList<>();
        List<Integer> bets = new ArrayList<>();
        List<String> actions = new ArrayList<>();

        String lastOutcome;

        @Override public void displayHand(Hand hand, String owner) {}
        @Override public void displayDealerHand(Hand hand, boolean hideHole) {}
        @Override public void displayMessage(String msg) {}
        @Override public void displayWelcomeBanner() {}
        @Override public String askMainMenuChoice() { return "3"; }
        @Override public String[] askLoginCredentials() { return new String[]{"test", "test"}; }
        @Override public String[] askRegistrationCredentials() { return new String[]{"test", "test"}; }
        @Override public void displaySaveConfirmation(String username) {}

        @Override
        public String askSessionChoice(Player player) {
            return sessionChoices.remove(0);
        }

        @Override
        public String askSettingsChoice() { return ""; }

        @Override public void displayStats(Player player) {}

        @Override
        public int askNewMaxBetLimit(int currentLimit) { return 0; }

        @Override
        public int askRechargeAmount() { return 0; }

        @Override
        public void displayRoundResult(Hand playerHand, Hand dealerHand, String outcome, String playerName) {
            lastOutcome = outcome;
        }

        @Override public String askInput(String prompt) { return ""; }

        @Override
        public String askAction(List<String> allowedActions) {
            return "STAND";
        }

        @Override
        public int askBet(int min, int max) {
            return 10;
        }
    }

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", 1000);
        ui = new TestUI();
        blackjack = new Blackjack(ui, player);
    }

    @Test
    void testSingleActionRound() {
        ui.sessionChoices.add("1"); // Spielen
        ui.sessionChoices.add("3"); // Danach beenden
        ui.actions.add("STAND"); // Eine Aktion testen (Stand)

        blackjack.start();

        assertNotNull(ui.lastOutcome, "Es sollte ein Runden-Ergebnis geben");
    }
}
