package blackjack;

// Hauptklasse und Einstiegspunkt der Anwendung.
// Steuert den Anmelde- und Registrierungsablauf vor dem eigentlichen Spielstart.
public class Main {
    public static void main(String[] args) {
        // Erzeugung der Benutzerschnittstelle und des Persistence Managers.
        UserInterface ui = new ConsoleUI();
        UserManager userManager = new UserManager();
        User currentUser = null;

        boolean authenticated = false;

        // Schleife fuer das Startmenue bis eine erfolgreiche Anmeldung oder ein Gastbeitritt erfolgt.
        while (!authenticated) {
            int option = ui.askStartMenuOption();

            switch (option) {
                case 1: // Anmelden
                    String username = ui.askInput("Benutzername: ");
                    String password = ui.askPassword("Passwort: ");
                    currentUser = userManager.login(username, password);
                    if (currentUser != null) {
                        ui.displayMessage("Erfolgreich angemeldet! Willkommen, " + currentUser.getUsername() + ".");
                        authenticated = true;
                    } else {
                        ui.displayMessage("Anmeldung fehlgeschlagen! Benutzername oder Passwort falsch.");
                    }
                    break;

                case 2: // Account erstellen
                    String newName = ui.askInput("Waehle einen Benutzernamen: ");
                    String newPass = ui.askPassword("Waehle ein Passwort: ");
                    boolean success = userManager.register(newName, newPass, 1000);
                    if (success) {
                        ui.displayMessage("Account erfolgreich erstellt! Sie koennen sich jetzt anmelden.");
                    } else {
                        ui.displayMessage("Erstellung fehlgeschlagen. Benutzername bereits vergeben.");
                    }
                    break;

                case 3: // Gastzugang
                    currentUser = new User("Gast", "", 500);
                    ui.displayMessage("Willkommen! Sie spielen als Gast mit 500 Start-Chips.");
                    authenticated = true;
                    break;
            }
        }

        // Initialisierung und Start der eigentlichen Spiellogik.
        Game game = new Blackjack(ui);
        game.start();
    }
}