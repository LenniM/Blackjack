package blackjack;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UserInterface {
    // Ein Scanner reicht fuer alle Konsoleneingaben.
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void displayHand(Hand hand, String owner) {
    	// Formatiert und gibt die Karten einerr Hand mit Gesamtwert auf der Konsole aus.
    	// Beispiel: Spieler: [4H, AP] (Wert: 15)
        System.out.printf("%s: %s (Wert: %d)%n", owner, hand.getCards(), hand.calculateValue());
    }

    @Override
    public void displayDealerHand(Hand hand, boolean hideHole) {
        // Zu Beginn der Runde wird die zweite Dealerkarte nicht gezeigt.
        if (hideHole && hand.getCards().size() > 1) {
        	// Gibt nur die erste Karte des Dealers aus und markiert die zweite als verdeckt.
            System.out.printf("Dealer: [%s, verdeckt]%n", hand.getCards().get(0));
            return;
        }
     // Gibt die vollständige Dealer-Hand aus, wenn das Aufdecken erlaubt ist.
        displayHand(hand, "Dealer");
    }

    @Override
    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void displayWelcomeBanner() {
        // Formatiert das globale Willkommensbanner beim Anwendungsstart.
        System.out.println("================---------------------------================");
        System.out.println("        Willkommen beim Blackjack-Simulator");
        System.out.println("================---------------------------================");
    }

    @Override
    public String askMainMenuChoice() {
        // Formatiert das Hauptmenue und liest die Benutzerauswahl ein.
        System.out.println("\n=== HAUPTMENUE ===");
        System.out.println("[1] Anmelden");
        System.out.println("[2] Registrieren");
        System.out.println("[3] Als Gast spielen");
        System.out.println("[4] Beenden");
        // Liest die Benutzereingabe über die Hilfsmethode askInput ein.
        return askInput("Auswahl:");
    }

    @Override
    public String[] askLoginCredentials() {
        // Fragt die Anmeldedaten fuer bestehende Konten ab.
        String user = askInput("Benutzername:");
        String pass = askInput("Passwort:");
        // Liefert Benutzername und Passwort als zweielementiges Array zurück.
        return new String[]{user, pass};
    }

    @Override
    public String[] askRegistrationCredentials() {
        // Fragt die Kontodaten fuer eine Neuregistrierung ab.
        String user = askInput("Neuer Benutzername:");
        String pass = askInput("Neues Passwort:");
        // Gibt die Registrierungsdaten zur weiteren Verarbeitung zurück.
        return new String[]{user, pass};
    }

    @Override
    public void displaySaveConfirmation(String username) {
        // Gibt die Erfolgsmeldung der Datei-Persistierung aus.
        System.out.println("Fortschritt für " + username + " wurde gespeichert.");
    }

    @Override
    public String askSessionChoice(Player player) {
        // Formatiert das Optionsmenue der Spielsession und erfragt die Wahl.
        System.out.println("\n-------------------------------------------");
        System.out.println("Spieler: " + player.getUsername() + " | Guthaben: " + player.getChips() + " Chips");
        // Prüft, ob für den Spieler ein aktives Rundenlimit gesetzt ist.
        if (player.getMaxBetLimit() > 0) {
            System.out.println("Rundenlimit: " + player.getMaxBetLimit() + " Chips");
        }
        System.out.println("-------------------------------------------");
        System.out.println("[1] Runde spielen | [2] Account-Einstellungen | [3] Hauptmenue");
        // Liest die gewählte Menüoption ein und gibt sie zurück.
        return askInput("Auswahl:");
    }

    @Override
    public String askSettingsChoice() {
        // Formatiert das Einstellungsuntermenue auf der Konsole und erfasst die Auswahl.
        System.out.println("\n=== ACCOUNT EINSTELLUNGEN ===");
        System.out.println("[1] Statistik ansehen");
        System.out.println("[2] Neues maximales Einsatzlimit pro Runde (max. 500)");
        System.out.println("[3] Guthaben aufladen");
        System.out.println("[4] Zurueck");
        return askInput("Auswahl:");
    }

    @Override
    public void displayStats(Player player) {
        // Formatiert die Auswertung der persistenten Spielstatistiken.
        int totalRounds = player.getTotalWins() + player.getTotalLosses();
        double winRate = totalRounds > 0 ? ((double) player.getTotalWins() / totalRounds) * 100 : 0.0;
        // Ausgbe des Statistik-Headers.
        System.out.println("\n=== PERSISTENTE STATISTIK ===");
        System.out.println("Benutzername:       " + player.getUsername());
        System.out.println("Aktuelles Guthaben: " + player.getChips() + " Chips");
        System.out.println("Gesamte Siege:      " + player.getTotalWins());
        System.out.println("Gesamte Niederlagen:" + player.getTotalLosses());
        System.out.printf("Siegquote:          %.1f%%%n", winRate);
        System.out.println("Max. Rundenlimit:   " + (player.getMaxBetLimit() > 0 ? player.getMaxBetLimit() + " Chips" : "Kein Limit (Max. 500)"));
        System.out.println("=============================");
    }

    @Override
    public int askNewMaxBetLimit(int currentLimit) {
        // Erfragt die Eingabe fuer ein neues Einsatzlimit inklusive Konvertierung.
        System.out.println("Aktuelles Rundenlimit: " + (currentLimit > 0 ? currentLimit + " Chips" : "Kein Limit (Max. 500)"));
        String input = askInput("Neues Rundenlimit eingeben (0 fuer kein Limit, max. 500):");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bitte eine gueltige Zahl eingeben.");
        }
    }

    @Override
    public int askRechargeAmount() {
        // Erfragt den Betrag zum Aufladen des Chip-Guthabens.
        String input = askInput("Wieviel Guthaben möchtest du aufladen?:");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bitte eine gueltige Zahl eingeben.");
        }
    }

    @Override
    public void displayRoundResult(Hand playerHand, Hand dealerHand, String outcome, String playerName) {
        // Formatiert die Endstandsanzeige einer Spielrunde.
        System.out.println("\nEndstand:");
        // Stellt die finalen Hände von Spieler und Dealer gegenüber.
        displayHand(playerHand, playerName);
        displayDealerHand(dealerHand, false);

        switch (outcome) {
            case "BUST":
                System.out.println("Spieler verliert: ueber 21.");
                break;
            case "DEALER_BUST":
                System.out.println("Spieler gewinnt (1:1): Dealer ist ueber 21.");
                break;
            case "BLACKJACK":
                System.out.println("Natural Blackjack! Spieler gewinnt im Verhältnis 3:2.");
                break;
            case "DEALER_BLACKJACK":
                System.out.println("Dealer hat Blackjack. Spieler verliert.");
                break;
            case "WIN":
                System.out.println("Spieler gewinnt (1:1).");
                break;
            case "LOSE":
                System.out.println("Dealer gewinnt.");
                break;
            case "PUSH":
                System.out.println("Unentschieden (Push). Einsatz wird erstattet.");
                break;
            default:
                break;
        }
    }

    @Override
    public String askInput(String prompt) {
        System.out.print(prompt + " ");
     // Entfernt führende sowie nachfolgende Leerzeichen aus der Eingabe.
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
             // Validiert, ob der Betrag innerhalb der definierten Runden-Einsatzgrenzen liegt.
                if (amount >= min && amount <= max) {
                    return amount;
                }
            } catch (NumberFormatException ignored) {
            }
         // Gibt Hinweis bei fehlerhaftem Einsatzformat oder Grenzverletzung aus.
            displayMessage("Bitte einen gueltigen Einsatz eingeben.");
        }
    }
}