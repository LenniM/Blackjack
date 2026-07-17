# Blackjack Java-Prototyp

Ein erster Konsolen-Prototyp fuer Blackjack mit deutscher Karten-Notation.

## Enthalten

- Immutable `Card` mit Codes wie `4H`, `10K`, `AP`, `BZ`
- `Deck` mit mehreren Kartendecks, Mischen und Ziehen
- `Hand` mit Ass-Sonderfall in `calculateValue()`
- einfache Konsolen-UI
- eine spielbare Runde mit `HIT` und `STAND`

Accounts, Persistenz und erweiterte Aktionen wie `DOUBLE`, `SPLIT` und `SURRENDER` sind noch nicht als Spielablauf implementiert.

Der Prototyp verwendet String-Statuswerte statt Enums und erzeugt keine eindeutige Spiel-ID.

## Starten

Voraussetzung: installiertes JDK mit `javac` und `java` im PATH.

```powershell
javac -d out src/main/java/blackjack/*.java
java -cp out blackjack.Main
```
