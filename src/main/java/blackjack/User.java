package blackjack;

import java.io.Serializable;

// Benutzerkonto mit Authentifizierungsdaten und Guthaben.

// Implementiert Serializable fuer die dauerhafte Speicherung auf der Festplatte.
public class User implements Serializable {
	
    // Eindeutige Versions ID fuer die Java Serialisierung zur Sicherstellung der Kompatibilitaet.
    private static final long serialVersionUID = 1L;

    // Eindeutiger Name des Benutzers.
    private String username;

    // Passwort des Benutzers im Klartext fuer diesen Prototyp.
    private String password;

    // Aktueller Chip Stand des Spielers.
    private int balance;

    // Konstruktor zur Erstellung eines neuen Benutzers mit Startguthaben.
    public User(String username, String password, int initialBalance) {
        this.username = username;
        this.password = password;
        this.balance = initialBalance;
    }

    // Liefert den Benutzernamen zurueck.
    public String getUsername() {
        return username;
    }

    // Prueft, ob das uebergebene Passwort mit dem gespeicherten Passwort uebereinstimmt.
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    // Liefert den aktuellen Chip Stand des Spielers zurueck.
    public int getBalance() {
        return balance;
    }

    // Aktualisiert den Chip Stand des Spielers nach Gewinn oder Verlust.
    public void setBalance(int balance) {
        this.balance = balance;
    }
}