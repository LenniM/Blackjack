package blackjack;

public class Main {
    public static void main(String[] args) {
        // Einstiegspunkt fuer den Konsolen-Prototyp.
        // Spaeter koennte hier auch eine andere UI eingesetzt werden.
        UserInterface ui = new ConsoleUI();

        // Game ist der abstrakte Basistyp, Blackjack die konkrete Implementierung.
        Game game = new Blackjack(ui);
        game.start();
    }
}
