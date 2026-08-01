package blackjack;

public class Main {
    // Das einheitliche Startguthaben fuer neue Registrierungen und Gaeste.
    private static final int START_GUTHABEN = 500;

    public static void main(String[] args) {
        // Einstiegspunkt fuer den Konsolen-Prototyp.
        UserInterface ui = new ConsoleUI();
        AccountManager accountManager = new AccountManager();

        ui.displayMessage("================---------------------------================");
        ui.displayMessage("  Willkommen beim Blackjack-Simulator (HWR Berlin Project)");
        ui.displayMessage("================---------------------------================");

        boolean running = true;

        // Vorgelagerter Dialog fuer Anmeldung, Registrierung oder Gastzugang.
        while (running) {
            ui.displayMessage("\n=== HAUPTMENUE ===");
            ui.displayMessage("[1] Anmelden");
            ui.displayMessage("[2] Registrieren");
            ui.displayMessage("[3] Als Gast spielen");
            ui.displayMessage("[4] Beenden");

            String choice = ui.askInput("Auswahl:");
            Player activePlayer = null;

            if ("1".equals(choice)) {
                String user = ui.askInput("Benutzername:");
                String pass = ui.askInput("Passwort:");
                activePlayer = accountManager.login(user, pass);
                if (activePlayer == null) {
                    ui.displayMessage("Anmeldung fehlgeschlagen! Ungueltiges Passwort oder Benutzer existiert nicht.");
                }
            } else if ("2".equals(choice)) {
                String user = ui.askInput("Neuer Benutzername:");
                String pass = ui.askInput("Neues Passwort:");
                if (accountManager.register(user, pass, START_GUTHABEN)) {
                    ui.displayMessage("Registrierung erfolgreich! 500 Chips Guthaben wurden gutgeschrieben.");
                    activePlayer = accountManager.login(user, pass);
                } else {
                    ui.displayMessage("Benutzername ist bereits vergeben.");
                }
            } else if ("3".equals(choice)) {
                activePlayer = accountManager.createGuestPlayer(START_GUTHABEN);
                ui.displayMessage("Als Gast angemeldet. Startguthaben: 500 Chips.");
            } else if ("4".equals(choice)) {
                running = false;
                ui.displayMessage("Vielen Dank fürs Spielen! Bis zum naechsten Mal.");
                continue;
            } else {
                ui.displayMessage("Ungueltige Option. Bitte erneut versuchen.");
                continue;
            }

            // Spiel starten, sobald ein gueltiger Player vorliegt.
            if (activePlayer != null) {
                Game game = new Blackjack(ui, activePlayer);
                game.start();

                // Speichern nach Spielende (nur fuer registrierte Benutzer).
                if (!activePlayer.isGuest()) {
                    accountManager.saveAccounts();
                    ui.displayMessage("Fortschritt für " + activePlayer.getUsername() + " wurde gespeichert.");
                }
            }
        }
    }
}