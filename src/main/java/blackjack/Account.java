package blackjack;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private int maxBetLimit; // 0 bedeutet kein individuelles Limit

    public Account(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Benutzername darf nicht leer sein.");
        }
        this.username = username;
        this.password = password != null ? password : "";
        this.maxBetLimit = 0;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public int getMaxBetLimit() {
        return maxBetLimit;
    }

    public void setMaxBetLimit(int maxBetLimit) {
        // Das Rundenlimit muss zwischen 0 (deaktiviert) und maximal 500 Chips liegen.
        if (maxBetLimit < 0 || maxBetLimit > 500) {
            throw new IllegalArgumentException("Das maximale Rundenlimit muss zwischen 0 und 500 Chips liegen.");
        }
        this.maxBetLimit = maxBetLimit;
    }
}