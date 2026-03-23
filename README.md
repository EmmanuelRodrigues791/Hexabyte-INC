# CartPilot — Inventory Management 

A Java Swing desktop inventory management system built with Java and MySQL for CSCI2040U at Ontario Tech University.

---

## Features

- Role-based access control (Owner, Manager, Employee)
- Graphical UI with Login, Manager, and Employee dashboards
- Add, remove, update price, and update the quantity of items
- Persistent storage via MySQL database
- Activity log with timestamps also via MySQL database
- View full inventory from database

---

## Database Setup

Run the following SQL scripts in MySQL Workbench to set up the required tables:

```sql
CREATE TABLE inventory (
    idinventory INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    price DOUBLE NULL,
    quantity INT NULL,
    origin VARCHAR(255) NULL,
    PRIMARY KEY (idinventory)
);
```

```sql
CREATE TABLE log (
    idlog INT NOT NULL AUTO_INCREMENT,
    entries VARCHAR(255) NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idlog)
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
│   ├── LoginPage.java                  # Login UI
│   ├── ManagerPage.java                # Manager/Owner dashboard UI
│   └── EmployeePage.java               # Employee dashboard UI
├── test/
│   ├── ItemTest.java                   # Unit tests for Item class
│   └── InventorySystemTest.java        # Unit tests for InventorySystem class
├── build.bat                           # Build and run script
├── build_test.bat                      # Test runner script
├── .gitignore
├── Hexabyte.iml
├── UMLDiagram_Script                   # PlantUML script for class diagram
├── UML_Diagram.png                     # UML class diagram
├── UseCaseUML.png                      # UML sequence diagram
├── UseCase_UML                         # PlantUML script for sequence diagram
└── hexabyte_burndown_chart.png         # burndown chart
```

---

## How to Setup

1. Download/Clone the repository
2. Download Workbench from: https://dev.mysql.com/downloads/workbench/
3. Set up the MySQL database using the scripts above
4. Update DB credentials in `InventorySystem.java` if needed:

```java
conn = DriverManager.getConnection(
    "jdbc:mysql://127.0.0.1:3306/login", "root", "yourpassword"
);
```

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
| Add Item | ✅ | ✅ | ❌ |
| Remove Item | ✅ | ✅ | ❌ |
| Update Price | ✅ | ✅ | ❌ |
| Update Quantity | ✅ | ✅ | ✅ |
| View Inventory | ✅ | ✅ | ✅ |
| View Log | ✅ | ✅ | ❌ |

---

## Unit Tests

| Test Class | Tests | Result |
|------------|-------|--------|
| `ItemTest` | 9 tests — all getters and setters | ✅ Pass |
| `InventorySystemTest` | 7 tests — all DB methods with mock objects | ✅ Pass |

---



## Authors

Hexabyte Team — Ontario Tech University, CSCI2040U
