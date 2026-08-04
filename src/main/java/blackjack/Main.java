package blackjack;

public class Main {
    // Das einheitliche Startguthaben fuer neue Registrierungen und Gaeste.
    private static final int START_GUTHABEN = 500;

    public static void main(String[] args) {
        // Einstiegspunkt
        // Spaeter koennte hier auch eine andere UI eingesetzt werden.
        UserInterface ui = new ConsoleUI();
        
        // Erzeugt die Instanz zur Verwaltung der Konten und Datei-Persistenz.
        AccountManager accountManager = new AccountManager();

        ui.displayWelcomeBanner();

        boolean running = true;

        // Vorgelagerter Dialog fuer Anmeldung, Registrierung oder Gastzugang.
        while (running) {
        	
        	// Liest die gewählte Menüoption des Hauptmenüs ein.
            String choice = ui.askMainMenuChoice();
            Player activePlayer = null;

            if ("1".equals(choice)) {		// Option 1: Anmeldung eines bestehenden Benutzers.
                String[] credentials = ui.askLoginCredentials(); 	// Erfragt Benutzername und Passwort über die Konsolen-UI.
                activePlayer = accountManager.login(credentials[0], credentials[1]);
                if (activePlayer == null) {
                    ui.displayMessage("Anmeldung fehlgeschlagen! Ungueltiges Passwort oder Benutzer existiert nicht.");
                }
            } else if ("2".equals(choice)) {		// Option 2: Registrierung eines neuen Kontos.
                String[] credentials = ui.askRegistrationCredentials();
                if (accountManager.register(credentials[0], credentials[1], START_GUTHABEN)) {
                    ui.displayMessage("Registrierung erfolgreich! 500 Chips Guthaben wurden gutgeschrieben.");
                    activePlayer = accountManager.login(credentials[0], credentials[1]);
                } else {
                    ui.displayMessage("Benutzername ist bereits vergeben.");
                }
            } else if ("3".equals(choice)) {		// Option 3: Starten einer Session als temporärer Gast-Spieler
                activePlayer = accountManager.createGuestPlayer(START_GUTHABEN);
                ui.displayMessage("Als Gast angemeldet. Startguthaben: 500 Chips.");
            } else if ("4".equals(choice)) {		// Option 4: Beenden des Programms.
                running = false;
                ui.displayMessage("Vielen Dank fuers Spielen! Bis zum naechsten Mal.");
                continue;
            } else {
                ui.displayMessage("Ungueltige Option. Bitte erneut versuchen.");
                continue;
            }

            // Game ist der abstrakte Basistyp, Blackjack die konkrete Implementierung.
            if (activePlayer != null) {
                Game game = new Blackjack(ui, activePlayer);
                game.start();

                // Speichern nach Spielende (nur fuer registrierte Benutzer).
                if (!activePlayer.isGuest()) {
                    accountManager.saveAccounts();
                    ui.displaySaveConfirmation(activePlayer.getUsername());
                }
            }
        }
    }
}