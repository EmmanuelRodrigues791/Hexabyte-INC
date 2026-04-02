# CartPilot — Inventory Management System

A Java Swing desktop inventory management system built with Java and MySQL for CSCI2040U at Ontario Tech University.

---

## Features

- Role-based access control (Owner, Manager, Employee)
- Graphical UI with Login, Manager, and Employee dashboards
- User registration and authentication with username and password
- First-time setup — automatically prompts to create owner account on fresh database
- Add, remove, update price, and update quantity of inventory items
- Persistent storage via MySQL database
- Activity log with timestamps — every action logged with the user who performed it
- Ability to clear the log
- View full inventory from database in a table
- Ability to search items by name
- All tables auto-created on first launch — no manual SQL required

---

## Database Setup

The application automatically creates all required tables on first launch. If you prefer to set them up manually, run the following SQL scripts in MySQL Workbench:

```sql
CREATE TABLE inventory (
    idinventory INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    origin VARCHAR(255) NOT NULL,
    PRIMARY KEY (idinventory)
);

CREATE TABLE log (
    idlog INT NOT NULL AUTO_INCREMENT,
    entries VARCHAR(255) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idlog)
);

CREATE TABLE users (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);
```

---

## Requirements

- Java JDK 25+
- MySQL Server 8.0+
- MySQL Connector/J 9.6.0
- IntelliJ IDEA (recommended)

---

## Project Structure

```
Hexabyte/
├── .idea/                              # IntelliJ IDEA project settings
├── lib/
│   ├── mysql-connector-j-9.6.0.jar    # MySQL JDBC driver
│   ├── junit-jupiter-api-5.10.0.jar
│   ├── junit-jupiter-engine-5.10.0.jar
│   ├── junit-platform-console-standalone-1.10.4.jar
│   ├── junit-platform-commons-1.10.0.jar
│   ├── junit-platform-engine-1.10.0.jar
│   ├── mockito-core-5.12.0.jar
│   ├── byte-buddy-1.14.19.jar
│   ├── byte-buddy-agent-1.14.19.jar
│   ├── objenesis-3.3.jar
│   ├── opentest4j-1.3.0.jar
│   └── apiguardian-api-1.1.2.jar
├── src/
│   ├── Main.java                       # Entry point — launches GUI
│   ├── InventorySystem.java            # Business logic and database operations
│   ├── Item.java                       # Item data model
│   ├── LoginPage.java                  # Login UI with Register New User support
│   ├── ManagerPage.java                # Manager/Owner dashboard UI
│   └── EmployeePage.java               # Employee dashboard UI
├── test/
│   ├── ItemTest.java                   # Unit tests for Item class (9 tests)
│   ├── InventorySystemTest.java        # Unit tests for InventorySystem class (7 tests)
│   └── RealTimeUpdateTest.java         # TDD red tests for upcoming feature (5 tests)
├── build.bat                           # Build and run script
├── build_test.bat                      # Test runner script
├── .gitignore
├── Hexabyte.iml
├── UMLDiagram_Script                   # PlantUML script for class diagram
├── UML_Diagram.png                     # UML class diagram
├── UseCaseUML.png                      # UML sequence diagram
├── UseCase_UML                         # PlantUML script for sequence diagram
└── hexabyte_burndown_chart.png         # Burndown chart
```

---

## How to Setup

1. Download/Clone the repository
2. Install MySQL Server and Workbench from: https://dev.mysql.com/downloads/workbench/
3. Update DB credentials in `InventorySystem.java` if needed:

```java
conn = DriverManager.getConnection(
    "jdbc:mysql://127.0.0.1:3306/login", "root", "yourpassword"
);
```

4. Run `build.bat` — tables are created automatically on first launch
5. On first launch you will be prompted to create an Owner account

---

## How to Run Via Build Script (Recommended)

Navigate to the project folder:

```
cd "C:\Users\your user\add necessary directories"
```

Build and launch the application:

```
build.bat
```

Run all unit tests:

```
build_test.bat
```

Expected test output:

```
16 tests found
16 tests successful
0 tests failed
```

---

## How to Run Via Command Prompt (Manual)

1. Navigate to the project folder:

```
cd "C:\Users\your user\add necessary directories"
```

2. Compile:

```
C:\Users\your user\.jdks\openjdk-25.0.2\bin\javac -cp "lib\mysql-connector-j-9.6.0.jar" -d out src\Main.java src\InventorySystem.java src\Item.java src\LoginPage.java src\ManagerPage.java src\EmployeePage.java
```

3. Run:

```
C:\Users\your user\.jdks\openjdk-25.0.2\bin\java -cp "out;lib\mysql-connector-j-9.6.0.jar" Main
```

---

## Role Permissions

| Feature | Owner | Manager | Employee |
|---------|-------|---------|----------|
| Register New User | ✅ | ✅ | ❌ |
| Remove User | ✅ | ✅ | ❌ |
| Add Item | ✅ | ✅ | ❌ |
| Remove Item | ✅ | ✅ | ❌ |
| Update Price | ✅ | ✅ | ❌ |
| Update Quantity | ✅ | ✅ | ✅ |
| View Inventory | ✅ | ✅ | ✅ |
| View Log | ✅ | ✅ | ❌ |
| Clear Log | ✅ | ✅ | ❌ |

* Note: Managers cannot add/remove the owner or other managers, only employees

---

## Unit Tests

| Test Class | Tests | Result |
|------------|-------|--------|
| `ItemTest` | 9 tests — all getters and setters | ✅ Pass |
| `InventorySystemTest` | 7 tests — all DB methods with mock objects | ✅ Pass |
| `RealTimeUpdateTest` | 5 tests — TDD red tests for upcoming feature | 🔴 Red (intentional) |

---



## Authors

Hexabyte Team — Ontario Tech University, CSCI2040U
