package blackjack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Verwaltet das Registrieren, Anmelden und Speichern aller Benutzerkonten.
public class UserManager {
    // Dateiname fuer die Speicherung der serialisierten Benutzerdatenbank.
    private static final String FILE_NAME = "users.ser";

    // Map zur schnellen Zuordnung von Benutzernamen zu User Objekten.
    private Map<String, User> users;

    // Konstruktor: Laedt beim Start bestehende Konten aus der Datei oder erstellt eine neue Map.
    @SuppressWarnings("unchecked")
    public UserManager() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                // Liest die gespeicherte HashMap aus der Datei ein.
                users = (Map<String, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // Tritt ein Lese- oder Deserialisierungsfehler auf, wird eine neue Datenbank initialisiert.
                System.err.println("Fehler beim Laden der Benutzerdaten. Erstelle neue Datenbank.");
                users = new HashMap<>();
            }
        } else {
            // Falls die Datei noch nicht existiert, wird eine leere HashMap angelegt.
            users = new HashMap<>();
        }
    }

    // Registriert einen neuen Benutzer, sofern der Benutzername noch verfuegbar ist.
    public boolean register(String username, String password, int initialBalance) {
        if (users.containsKey(username)) {
            return false; // Benutzername ist bereits vergeben
        }
        User newUser = new User(username, password, initialBalance);
        users.put(username, newUser);
        save(); // Speichert den neuen Zustand direkt ab
        return true;
    }

    // Ueberprueft Zugangsdaten und liefert bei Erfolg das entsprechende User Objekt zurueck.
    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        return null; // Login fehlgeschlagen
    }

    // Speichert die aktuelle Benutzer Map dauerhaft mittels Java Serialisierung in der Datei.
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Benutzerdaten: " + e.getMessage());
        }
    }
}