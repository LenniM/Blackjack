package blackjack;

import java.util.List;

public interface UserInterface {
	
	// Zeigt eine komplette Hand mit Besitzer an, z.B. Spieler oder Dealer.
    void displayHand(Hand hand, String owner);
    
    // Beim Dealer kann die zweite Karte am Anfang verdeckt bleiben.
    void displayDealerHand(Hand hand, boolean hideHole);
    
    // Einfache Textausgabe fuer Spielmeldungen.
    void displayMessage(String message);
    
    // Allgemeine Texteingabe.
    String askInput(String prompt);
    
    // Fragt eine Aktion ab und bekommt eine Liste erlaubter Strings.
    String askAction(List<String> allowedActions);
    
    // Fragt den Einsatz innerhalb eines Mindest- und Hoechstwerts ab.
    int askBet(int min, int max);

    // Fragt die gewuenschte Option im Startmenue ab (1: Anmelden, 2: Registrieren, 3: Gast).
    int askStartMenuOption();

    // Liest eine Passworteingabe vom Benutzer ein.
    String askPassword(String prompt);
}