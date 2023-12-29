# ReferencEase Projekt

## Überblick

ReferencEase ist eine Jakarta EE-Anwendung, die in IntelliJ entwickelt wurde. Dieses README bietet
eine Anleitung zur Einrichtung der Anwendung, einschließlich der Konfiguration des `.run`-Files, der
Einrichtung einer PostgreSQL-Datenbank mittels Docker, sowie der Verwendung des Glassfish Servers
7.0.7. Das Projekt nutzt Gradle als Build-Automatisierungstool.

## Voraussetzungen

- Java JDK 17 (on PATH)
- IntelliJ IDEA
- Docker
- Gradle
- Glassfish Server 7.0.7 (bereits in der Projektstruktur enthalten)

## Einrichtung der Jakarta EE-Anwendung in IntelliJ

### Schritt 1: Klonen des Projekts

Klonen Sie das ReferencEase-Projekt in Ihr lokales Verzeichnis:

```bash
git clone https://github.com/R3dW1ne/referencease_
```

### Schritt 2: Importieren des Projekts in IntelliJ

1. Öffnen Sie IntelliJ IDEA.
2. Wählen Sie "Open" und navigieren Sie zum geklonten Projektordner.
3. Wählen Sie den Projektordner und klicken Sie auf "OK", um das Projekt zu öffnen.

### Schritt 3: Konfigurieren des Gradle-Projekts

1. Warten Sie, bis IntelliJ das Projekt als Gradle-Projekt erkennt und alle erforderlichen
   Abhängigkeiten herunterlädt.
2. Führen Sie `gradlew build` aus, um sicherzustellen, dass das Projekt erfolgreich gebaut wird.

### Schritt 4: Einrichten der .run-Konfiguration

1. Gehen Sie zu "Run" > "Edit Configurations".
2. Erstellen Sie eine neue Konfiguration, indem Sie "+" klicken und "Jakarta EE" auswählen.
3. Konfigurieren Sie die erforderlichen Einstellungen (z.B. Server-Details, Deploy-Pfade).

## Einrichtung der PostgreSQL-Datenbank mit Docker

### Schritt 1: Erstellen eines Docker-Containers für PostgreSQL

1. Stellen Sie sicher, dass Docker auf Ihrem System installiert ist.
2. Stellen Sie sicher, dass Docker auf Ihrem System ausgeführt wird.
3. Öffnen Sie das Terminal und navigieren Sie zum Projektordner.
4. Führen Sie den Docker-Befehl aus, um die PostgreSQL-Datenbank zu erstellen und zu starten:

```bash
docker compose up
```

## Einrichten des Glassfish Servers 7.0.7

1. Der Glassfish Server 7.0.7 ist bereits in der Projektstruktur enthalten. Stellen Sie sicher, dass
   er in IntelliJ korrekt konfiguriert ist.
2. Unter "Run" > "Edit Configurations" wählen Sie den Glassfish Server und stellen Sie den Pfad zur
   Installation und die erforderlichen Einstellungen ein. Sie können dafür den absoluten Pfad zum
   Glassfish-Server-Ordner verwenden, der sich im Projektordner befindet.

Bspw.: "C:\Users\USER\IdeaProjects\referencease\Glassfish\glassfish-7.0.7\glassfish7"

## Ausführen der Anwendung

1. Wählen Sie die konfigurierte `.run`-Konfiguration in IntelliJ.
2. Klicken Sie auf "Run" oder "Debug", um die Anwendung zu starten.

## Support

Bei Problemen oder Fragen zur Einrichtung oder Nutzung des ReferencEase-Projekts können Sie gerne
ein Issue im Projekt-Repository erstellen.

---

Für weitere Informationen und detaillierte Anweisungen beziehen Sie sich bitte auf die offizielle
Dokumentation der verwendeten Technologien.
