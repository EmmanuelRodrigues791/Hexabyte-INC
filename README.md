# CartPilot — Inventory Management System
A terminal-based inventory management system built with Java and MySQL for CSCI2040U at Ontario Tech University.

## Features

Role-based access control (Owner, Manager, Employee)
Add, remove, and update inventory items
Persistent storage via MySQL database
Activity log with timestamps
View full inventory from database

## Database Setup
Run the following SQL scripts in MySQL Workbench to set up the required tables:
```
CREATE TABLE inventory (
    idinventory INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    price DOUBLE NULL,
    quantity INT NULL,
    origin VARCHAR(255) NULL,
    PRIMARY KEY (idinventory)
);
```
```
CREATE TABLE log (
    idlog INT NOT NULL AUTO_INCREMENT,
    entries VARCHAR(255) NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idlog)
);
```

## Requirements

Java JDK 25+
MySQL Server 8.0+
MySQL Connector/J 9.6.0
IntelliJ IDEA (recommended)

## Project Structure
```
Hexabyte/
├── .idea/                           # IntelliJ IDEA project settings
├── lib/
│   └── mysql-connector-j-9.6.0.jar # MySQL JDBC driver
├── src/
│   ├── Main.java                    # Entry point, handles user input and menu
│   ├── InventorySystem.java         # Business logic and database operations
│   └── Item.java                    # Item data model
├── .gitignore
├── Hexabyte.iml                     # IntelliJ module file
├── UMLDiagram_Script                # PlantUML script for class diagram
├── UML_Diagram.png                  # UML class diagram
├── UseCaseUML.png                   # UML sequence diagram
├── UseCase_UML                      # PlantUML script for sequence diagram
└── hexabyte_burndown_chart.png      # Iteration 1 burndown chart  
```

How to Run

1. Clone or download the repository
2. Open the project in IntelliJ IDEA
3. Add mysql-connector-j-9.6.0.jar to your project libraries
4. Set up the MySQL database using the scripts above
5. Update DB credentials in InventorySystem.java if needed:

javaconn = DriverManager.getConnection(
    "jdbc:mysql://127.0.0.1:3306/login", "root", "yourpassword"
);

6. Run Main.java

Authors
Hexabyte Team — Ontario Tech University, CSCI2040U
