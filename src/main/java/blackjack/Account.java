package blackjack;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    // Anmeldedaten des Benutzers privat gekapselt.
    private String username;
    private String password;

    // Maximales Einsatzlimit pro Runde in Chips (0 bedeutet kein Limit).
    private int maxBetLimit;

    public Account(String username, String password) {
        // Null oder leere Eingaben sind fuer ein Konto nicht zulaessig.
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Benutzername darf nicht leer sein.");
        }
        this.username = username;
        this.password = password != null ? password : "";
        this.maxBetLimit = 0; // Standardmaessig ist kein Rundenlimit gesetzt.
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String inputPassword) {
        // Prueft, ob das eingegebene Passwort mit dem gespeicherten uebereinstimmt.
        return this.password.equals(inputPassword);
    }

    public int getMaxBetLimit() {
        return maxBetLimit;
    }

    public void setMaxBetLimit(int maxBetLimit) {
        // Ein negatives Limit ist fachlich nicht sinnvoll.
        if (maxBetLimit < 0) {
            throw new IllegalArgumentException("Das maximale Einsatzlimit darf nicht negativ sein.");
        }
        this.maxBetLimit = maxBetLimit;
    }
}