# Blackjack Simulator (Java)

Ein konsolenbasierter Blackjack-Simulator, entwickelt im Rahmen der Belegarbeit für das Modul **Objektorientierte Programmierung** an der HWR Berlin.

## Wofür ist das Projekt?
Das Projekt demonstriert die praktische Anwendung objektorientierter Softwarearchitektur in Java am Beispiel des Kartenspiels Blackjack. Es bildet den kompletten Spielablauf zwischen einem menschlichen Spieler und einem automatisierten Dealer ab und setzt zentrale OOP-Konzepte wie Kapselung, Vererbung, Abstraktion über Interfaces sowie Dateipersistenz um.

## Inhalt & Features
- **Spieldomäne:** Objektorientierte Abbildung von Karten (`Card`), Kartendecks (`Deck`) und Spielerhänden (`Hand`) inklusive dynamischer Ass-Wertung (Soft/Hard Hand).
- **Spielsteuerung:** Regelbasierter Rundenablauf, automatische Dealer-KI (zieht obligatorisch bis 17 Punkte) und korrekte Gewinnabrechnung (1:1, 3:2 bei Natural Blackjack, Push).
- **Benutzerverwaltung & Persistenz:** Registrierung, Login und Gastmodus. Speicherung von Guthaben, individuellen Einsatzlimits und Rundenstatistiken über Java-Objektserialisierung (`accounts.ser`).
- **Entkoppelte Architektur:** Strikte Trennung von Spiellogik und Benutzeroberfläche über das `UserInterface`-Interface und die Konsolenumsetzung `ConsoleUI`.
- **Qualitätssicherung:** Abgesichert durch automatisierte Unit-Tests (JUnit 5) für die Kernklassen der Spieldomäne.

## Starten

Voraussetzung: installiertes JDK mit `javac` und `java` im PATH.

```powershell
javac -d out src/main/java/blackjack/*.java
java -cp out blackjack.Main
```
