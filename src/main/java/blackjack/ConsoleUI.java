package blackjack;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UserInterface {
    // Ein Scanner reicht fuer alle Konsoleneingaben.
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void displayHand(Hand hand, String owner) {
        // Beispiel: Spieler: [4H, AP] (Wert: 15)
        System.out.printf("%s: %s (Wert: %d)%n", owner, hand.getCards(), hand.calculateValue());
    }

    @Override
    public void displayDealerHand(Hand hand, boolean hideHole) {
        // Zu Beginn der Runde wird die zweite Dealerkarte nicht gezeigt.
        if (hideHole && hand.getCards().size() > 1) {
            System.out.printf("Dealer: [%s, verdeckt]%n", hand.getCards().get(0));
            return;
        }
        displayHand(hand, "Dealer");
    }

    @Override
    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public String askInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine().trim();
    }

    @Override
    public String askAction(List<String> allowedActions) {
        while (true) {
            // Eingaben werden grossgeschrieben, damit "hit" und "HIT" beide funktionieren.
            String input = askInput("Aktion " + allowedActions + ":").toUpperCase();
            if (allowedActions.contains(input)) {
                return input;
            }
            displayMessage("Ungueltige Aktion.");
        }
    }

    @Override
    public int askBet(int min, int max) {
        while (true) {
            String input = askInput("Einsatz (" + min + "-" + max + "):");
            try {
                // parseInt wirft eine Exception, wenn keine Zahl eingegeben wurde.
                int amount = Integer.parseInt(input);
                if (amount >= min && amount <= max) {
                    return amount;
                }
            } catch (NumberFormatException ignored) {
                // Try again below.
            }
            displayMessage("Bitte einen gueltigen Einsatz eingeben.");
        }
    }
}
