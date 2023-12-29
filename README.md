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

### Schritt 4: Einrichten der .run-Konfiguration (in diesem Projekt bereits enthalten)

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

1. **Download Glassfish Server**:
    - Laden Sie
      den [Eclipse GlassFish 7.0.7, Jakarta EE Web Profile, 10](https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/web-7.0.7.zip)
      von der offiziellen [Glassfish-Website](https://glassfish.org/download_gf7.html) herunter.
    - Entpacken Sie das Archiv in ein Verzeichnis Ihrer Wahl.
2. **PostgreSQL JDBC-Treiber**:
    - Der PostgreSQL JDBC-Treiber wird benötigt. Dieser kann von der
      offiziellen [PostgreSQL-Website](https://jdbc.postgresql.org/download/) heruntergeladen
      werden.
    - Nach dem Herunterladen ist die **`.jar`**-Datei in das Verzeichnis *
      *`GLASSFISH_HOME/glassfish/domains/domain1/lib/`** zu kopieren. **`GLASSFISH_HOME`**
      bezeichnet das Hauptverzeichnis der GlassFish-Installation, und **`domain1`** ist das
      Standarddomänenverzeichnis. Bei Verwendung einer anderen Domäne ist der Pfad entsprechend
      anzupassen.
3. Starten Sie den Glassfish Server:
    - Öffnen Sie das Terminal und navigieren Sie zum Verzeichnis **`GLASSFISH_HOME/bin/`**.
    - Führen Sie den folgenden Befehl aus, um den Glassfish Server zu starten:

```bash
./asadmin start-domain domain1
```

4. **Datenquelle in GlassFish einrichten**:
    - Öffnung der GlassFish Admin Console (normalerweise unter **`http://localhost:4848/`**
      erreichbar).
        - Navigation zu "Resources" > "JDBC" > "Connection Pools".
        - Klicken auf "New..." und Erstellung eines neuen Connection Pools:
            - Pool Name: **`PostgreSQLPool`**
            - Resource Type: **`javax.sql.ConnectionPoolDataSource`**
            - Database Driver Vendor: **`Postgresql`**
        - Auf der zweiten Seite ist der Datasource Classname wie folgt auszufüllen:
            - **`org.postgresql.ds.PGConnectionPoolDataSource`**
        - Klicken auf "Finish".
        - Jetzt ist der soeben erstellte Pool (z.B. **`PostgreSQLPool`**) auszuwählen und auf "Edit"
          zu klicken.
        - Unter dem Tab "Additional Properties" sind die Datenbank-Verbindungsdetails zu setzen,
          z.B.:
            - **`User`**: **`postgres`**
            - **`Password`**: **`mysecretpassword`**
            - **`Url`**: **`jdbc:postgresql://localhost:5432/referencease`**
        - Speichern der Änderungen und Sicherstellen, dass die Verbindung mit dem Button "Ping"
          getestet werden kann.
5. **JNDI-Name für die Datenquelle erstellen**:
    - Navigation zu "Resources" > "JDBC" > "JDBC Resources".
    - Klicken auf "New...".
    - JNDI Name: **`jdbc/myPostgresDS`**
    - Pool Name: Auswahl des zuvor erstellten Pools (**`PostgreSQLPool`**).
    - Klicken auf "OK".
6. **persistence.xml**:
    - In der **`persistence.xml`** sollte nun folgender **`jta-data-source`** Eintrag verwendet
      werden:
        - **`<jta-data-source>jdbc/myPostgresDS</jta-data-source>`**
    - Die **`persistence.xml`** befindet sich im Verzeichnis **`src/main/resources/META-INF/`**.
    - In diesem Projekt ist die **`persistence.xml`** bereits entsprechend konfiguriert.

## Ausführen der Anwendung

1. Wählen Sie die konfigurierte `.run`-Konfiguration in IntelliJ.
2. Klicken Sie auf edit configuration und korrigieren Sie den Pfad zum Glassfish Server, falls
   erforderlich.
    - Der Pfad zum Glassfish Server ist **`GLASSFISH_HOME/`**.
        - Bspw. **`C:/Users/username/Downloads/web-7.0.7/glassfish7/`**.
    - Der Pfad zum Glassfish Logfile ist *
      *`GLASSFISH_HOME/glassfish/domains/domain1/logs/server.log`**.
    - Der Pfad zum log4j Logfile, also zu den Applikation-Logs, ist *
      *`GLASSFISH_HOME/glassfish/domains/domain1/config/logs/log4j.log`**.
3. Klicken Sie auf "Run" oder "Debug", um die Anwendung zu starten.

## Informationen zu den Datenbank-Tabellen

Die Datenbank-Tabellen und einige initialen Daten, werden automatisch beim Starten der Anwendung
erstellt. Dazu wird die Klasse java/com/ffhs/referencease/AppInitSingleton.java verwendet.
Um die Applikation zu redeployen, muss die Klasse AppInitSingleton.java auskommentiert werden.
Wird die Klasse nicht auskommentiert, ist nur ein Restart der Applikation möglich.
Dies ist der Verwendung von Singleton und EJB geschuldet, da die Injections sonst nicht korrekt
funktionieren.

## Support

Bei Problemen oder Fragen zur Einrichtung oder Nutzung des ReferencEase-Projekts können Sie gerne
ein Issue im Projekt-Repository erstellen.

---

Für weitere Informationen und detaillierte Anweisungen beziehen Sie sich bitte auf die offizielle
Dokumentation der verwendeten Technologien.
