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
sqlCREATE TABLE inventory (
    idinventory INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    price DOUBLE NULL,
    quantity INT NULL,
    origin VARCHAR(255) NULL,
    PRIMARY KEY (idinventory)
);

CREATE TABLE log (
    idlog INT NOT NULL AUTO_INCREMENT,
    entries VARCHAR(255) NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idlog)
);

## Requirements

Java JDK 25+
MySQL Server 8.0+
MySQL Connector/J 9.6.0
IntelliJ IDEA (recommended)

## Project Structure
Hexabyte/
├── .idea/                          # IntelliJ IDEA project settings

├── lib/                            # External libraries

│   └── mysql-connector-j-9.6.0.jar

├── src/                            # Source code

│   ├── Main.java                   # Entry point, handles user input and menu

│   ├── InventorySystem.java        # Business logic and database operations

│   └── Item.java                   # Item data model

├── .gitignore

├── Hexabyte.iml                    # IntelliJ module file

├── UMLDiagram_Script               # PlantUML script for class diagram

├── UML_Diagram.png                 # UML class diagram

├── UseCaseUML.png                  # UML sequence diagram

├── UseCase_UML                     # PlantUML script for sequence diagram

└── hexabyte_burndown_chart.png     # Iteration 1 burndown chart

## How to Run

Clone or download the repository
Open the project in IntelliJ IDEA
Add mysql-connector-j-9.6.0.jar to your project libraries
Set up the MySQL database using the scripts above
Update the DB credentials in InventorySystem.java if needed:

javaconn = DriverManager.getConnection(
    "jdbc:mysql://127.0.0.1:3306/login", "root", "yourpassword"
);

Run Main.java
