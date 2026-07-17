package blackjack;

import java.time.LocalDateTime;

public abstract class Game {
    // Spielstatus als String, damit der Prototyp ohne Enums auskommt.
    // Erlaubte Werte: "NOT_STARTED", "RUNNING", "PAUSED", "FINISHED".
    protected final String gameId;
    protected String state;
    protected LocalDateTime startTime;

    protected Game() {
        // Das Feld bleibt vorhanden, aber es wird keine eindeutige Spiel-ID erzeugt.
        this.gameId = "";
        this.state = "NOT_STARTED";
    }

    // Startet ein konkretes Spiel. Die genaue Logik liegt in der Unterklasse.
    public abstract void start();

    // Setzt ein konkretes Spiel neu auf. Die genaue Logik liegt in der Unterklasse.
    public abstract void restart();

    public void pause() {
        // Pausieren ist nur sinnvoll, wenn das Spiel gerade laeuft.
        if ("RUNNING".equals(state)) {
            state = "PAUSED";
        }
    }

    public void resume() {
        // Fortsetzen ist nur sinnvoll, wenn das Spiel vorher pausiert wurde.
        if ("PAUSED".equals(state)) {
            state = "RUNNING";
        }
    }

    public String getState() {
        return state;
    }
}
