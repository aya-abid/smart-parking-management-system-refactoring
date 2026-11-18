# Smart Parking Management System

A JavaFX desktop application refactored by **Aya Abid** from
[Yogesh Bhalla's Swing project](https://github.com/yogesh-bhalla/smart-parking-management-system).

Administrators manage hourly rates, employee accounts, and daily sales. Employees record vehicle
entries and exits, view occupied slots, and print parking tickets. Bay A has 50 slots for two
wheelers; Bay B has 36 slots for four wheelers.

The JavaFX client communicates over HTTP with a separate Java server on port **8888**. The server
stores data in MySQL. Maven and its wrapper manage the build; JWebServer is included in `local-repo/`.

## Requirements

- **JDK 17 or newer**; JDK 21 is recommended and was used for the test run.
- **MySQL 8**, running locally on port 3306.
- Internet access for the first Maven run. A separate Maven installation is unnecessary.
- A graphical desktop session for the client and server windows.

Run commands from this directory, beside `pom.xml`. Check Java with `java -version`;
set `JAVA_HOME` to your JDK if needed.

## How to run

### 1. Initialize the database

The default connection is `jdbc:mysql://127.0.0.1:3306/db1`, user `root`, with an empty password.
Import the schema using the MySQL client:

**macOS / Linux**

```sh
mysql -u root < db/schema.sql
```

**Windows PowerShell**

```powershell
Get-Content db\schema.sql | mysql -u root
```

Add `-p` to the MySQL command if your database account has a password. The script creates four
tables and seeds the administrator login **`admin` / `admin`**. It does not seed employees or rates.

If your credentials differ, keep them and pass matching settings to the server JVM. For example,
set this in the terminal used to start the server (replace the sample values):

```sh
# macOS / Linux
export JAVA_TOOL_OPTIONS='-Ddb.user=parking -Ddb.password=your_password'
```

```powershell
# Windows PowerShell
$env:JAVA_TOOL_OPTIONS = '-Ddb.user=parking -Ddb.password=your_password'
```

The database user needs access to the `db1` tables. `-Ddb.url=...` can override the JDBC URL.

### 2. Start the server

```sh
./mvnw javafx:run@server
```

Windows: `.\mvnw.cmd javafx:run@server`. Click **start server** in the window and leave it open.
Opening the window alone does not start the HTTP listener.

### 3. Start the client

Open a second terminal in the same directory:

```sh
./mvnw javafx:run@client
```

Windows: `.\mvnw.cmd javafx:run@client`.

Log in as `admin` / `admin`. Add rates for **Two Wheeler** and **Four Wheeler**, then create an
employee with a numeric ID, a 10-digit mobile number, and a JPG, JPEG, or BMP photo. The generated
employee password is printed in the **client terminal**; no real SMS is sent. Use the employee login
to open a bay, select a free slot, and later exit the vehicle using its ticket number.

## Tests

```sh
./mvnw test
```

Windows: `.\mvnw.cmd test`. Once dependencies are cached, `./mvnw -o test` runs offline.
The suite has **12 unit tests** covering bay layouts, ticket construction, response parsing,
rent calculation, and observer notifications. These tests do not require MySQL or a printer.

## Practical notes

- Photos are stored under `src/content/`, relative to the server's working directory.
- Printing targets `THERMAL Receipt Printer`. A printing failure does not undo a successful entry;
  the error is logged in the client terminal.
- Charges use completed hours with a one-hour minimum: 25 and 119 minutes both cost one hourly
  rate; 125 minutes cost two.
- This remains a demonstration: passwords are stored in plain text, HTTP operations lack server-side
  authorization, and SQL needs further hardening. Logout and employee deletion are not implemented.
  Closing the exit window without completing an exit leaves the bay hidden; reopen it from employee home.

## Troubleshooting

| Symptom | Check |
|---|---|
| Java is missing or its version is wrong | Check `JAVA_HOME` and `java -version`. |
| MySQL access denied | Match `db.user` / `db.password` to your account; keep its existing password. |
| Connection refused or login fails | Start MySQL, import the schema, and click **start server**. |
| Port 8888 is already in use | Stop the other listener before starting this server. |
| Wrapper permission denied on macOS/Linux | Run `chmod +x mvnw`, then retry. |
| Ticket lookup fails after entry | Ensure a rate exists for the vehicle type. |

Class diagrams are available in [`docs/uml/`](docs/uml/).
