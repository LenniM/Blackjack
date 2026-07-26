package blackjack;

import java.util.List;
import java.util.Scanner;

// Konkrete Implementierung des UserInterface fuer die Ausfuehrung in der Konsole.
public class ConsoleUI implements UserInterface {
    // Scanner Instanz fuer das Einlesen von Benutzereingaben ueber System.in.
    private final Scanner scanner;

    // Konstruktor: Initialisiert den Scanner fuer Konsoleneingaben.
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    // Zeigt die Karten einer Hand sowie deren berechneten Gesamtwert an.
    @Override
    public void displayHand(Hand hand, String owner) {
        System.out.printf("%s: %s (Wert: %d)%n", owner, hand.getCards(), hand.calculateValue());
    }

    // Zeigt die Dealer Hand an; maskiert bei Bedarf die zweite Karte.
    @Override
    public void displayDealerHand(Hand hand, boolean hideHole) {
        if (hideHole && hand.getCards().size() > 1) {
            System.out.printf("Dealer: [%s, verdeckt]%n", hand.getCards().get(0));
        } else {
            displayHand(hand, "Dealer");
        }
    }

    // Gibt eine allgemeine Nachricht auf der Konsole aus.
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    // Gibt einen Prompt aus und liest eine Textzeile vom Benutzer ein.
    @Override
    public String askInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Fragt eine Spielaktion ab und wiederholt die Abfrage bei ungueltiger Eingabe.
    @Override
    public String askAction(List<String> allowedActions) {
        while (true) {
            System.out.print("Aktion (" + allowedActions + "): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (allowedActions.contains(input)) {
                return input;
            }
            displayMessage("Ungueltige Aktion.");
        }
    }

    // Liest einen Spieleinsatz ein und validiert, ob er innerhalb des erlaubten Bereichs liegt.
    @Override
    public int askBet(int min, int max) {
        while (true) {
            System.out.print("Einsatz (" + min + " - " + max + "): ");
            String input = scanner.nextLine().trim();
            try {
                int amount = Integer.parseInt(input);
                if (amount >= min && amount <= max) {
                    return amount;
                }
            } catch (NumberFormatException e) {
                displayMessage("Bitte einen gueltigen Einsatz eingeben.");
            }
        }
    }

    // Praesentiert das Startmenue in der Konsole und garantiert eine gueltige Auswahl (1 bis 3).
    @Override
    public int askStartMenuOption() {
        System.out.println("\n=== BLACKJACK STARTMENUE ===");
        System.out.println("1) Anmelden");
        System.out.println("2) Account erstellen");
        System.out.println("3) Als Gast spielen");
        System.out.print("Bitte Option waehlen (1-3): ");

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int option = Integer.parseInt(input);
                if (option >= 1 && option <= 3) {
                    return option;
                }
            } catch (NumberFormatException ignored) {}
            System.out.print("Ungueltige Eingabe. Bitte 1, 2 oder 3 eingeben: ");
        }
    }

    // Liest ein Passwort vom Benutzer in der Konsole ein.
    @Override
    public String askPassword(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}