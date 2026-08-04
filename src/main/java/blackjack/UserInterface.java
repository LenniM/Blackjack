package blackjack;

import java.util.List;

public interface UserInterface {
    // Zeigt eine komplette Hand mit Besitzer an, z.B. Spieler oder Dealer.
    void displayHand(Hand hand, String owner);

    // Beim Dealer kann die zweite Karte am Anfang verdeckt bleiben.
    void displayDealerHand(Hand hand, boolean hideHole);

    // Einfache Textausgabe fuer Spielmeldungen.
    void displayMessage(String msg);

    // Gibt das Startbanner der Anwendung auf der Konsole aus.
    void displayWelcomeBanner();

    // Erfragt die Auswahl im Hauptmenue der Anwendung.
    String askMainMenuChoice();

    // Erfragt Benutzername und Passwort fuer die Anmeldung.
    String[] askLoginCredentials();

    // Erfragt Benutzername und Passwort fuer eine Neuregistrierung.
    String[] askRegistrationCredentials();

    // Gibt die Bestaetigung der persistenten Speicherung aus.
    void displaySaveConfirmation(String username);

    // Erfragt die Menueauswahl der laufenden Spielsession inklusive Kopfzeile.
    String askSessionChoice(Player player);

    // Erfragt die Auswahl im Einstellungsuntermenue.
    String askSettingsChoice();

    // Gibt die persistenten Account-Statistiken des Spielers aus.
    void displayStats(Player player);

    // Erfragt das neue maximale Einsatzlimit pro Runde.
    int askNewMaxBetLimit(int currentLimit);

    // Erfragt den Betrag zum Aufladen des Guthabens.
    int askRechargeAmount();

    // Zeigt das Endergebnis einer ausgewerteten Spielrunde an.
    void displayRoundResult(Hand playerHand, Hand dealerHand, String outcome, String playerName);

    // Allgemeine Texteingabe.
    String askInput(String prompt);

    // Fragt eine Aktion ab und bekommt eine Liste erlaubter Strings.
    String askAction(List<String> allowedActions);

    // Fragt den Einsatz innerhalb eines Mindest- und Hoechstwerts ab.
    int askBet(int min, int max);
    
}