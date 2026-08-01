package blackjack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    // Speicherort fuer die serialisierte Benutzer-Datenbank.
    private static final String FILE_PATH = "accounts.ser";
    private Map<String, Player> players = new HashMap<>();

    public AccountManager() {
        // Beim Erzeugen direkt bestehende Konten aus der Datei laden.
        loadAccounts();
    }

    public boolean register(String username, String password, int startChips) {
        // Registrierung schlaegt fehl, wenn der Benutzername bereits existiert.
        if (players.containsKey(username.toLowerCase())) {
            return false;
        }
        Player newPlayer = new Player(username, password, startChips);
        players.put(username.toLowerCase(), newPlayer);
        saveAccounts();
        return true;
    }

    public Player login(String username, String password) {
        // Benutzer suchen und Passwort abgleichen.
        Player player = players.get(username.toLowerCase());
        if (player != null && player.checkPassword(password)) {
            return player;
        }
        return null;
    }

    public Player createGuestPlayer(int startChips) {
        // Erstellt einen nicht-persistenten Gast-Spieler.
        return new Player("Gast", startChips);
    }

    public void saveAccounts() {
        // Die Map mit allen Spielern via Java-Serialisierung in Datei schreiben.
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(players);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Benutzerdaten: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadAccounts() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        // Konten aus der Datei deserialisieren.
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            players = (Map<String, Player>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden der Benutzerdaten: " + e.getMessage());
        }
    }
}