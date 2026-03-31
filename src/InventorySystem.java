import java.sql.*;
import java.util.*;

class InventorySystem {
    private Connection conn;

    // error handling if conn is closed
    public void close() throws SQLException {
        conn.close();
    }
    // open connection to database using constructor
    public InventorySystem() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/login", "root", "abc"
        );
    }

    // constructor for unit testing with mock objects
    public InventorySystem(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {return conn;}

    // method to add item to database
    public void addItem(int id, String name, double price, int quantity, String origin, String user) {

        logToDB(user + " Added item: " + name);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO inventory (idinventory, name, price, quantity, origin) Values (?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, origin);

            int rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to remove an item from database
    public void removeItem(String name, String user) {

        logToDB(user + " Removed item: " + name);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM inventory WHERE name = ?"
            );
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePrice(String name, double newPrice, String user) {

        logToDB(user + " Updated price for: " + name);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE inventory SET price = ? WHERE name = ?"
            );
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantity(String name, int newQuantity, String user) {

        logToDB(user + " Updated quantity for: " + name);

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE inventory SET quantity = ? WHERE name = ?"
            );
            ps.setInt(1, newQuantity);
            ps.setString(2, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewInventory() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM inventory");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("idinventory") + " | " +
                                rs.getString("name") + " | " +
                                rs.getFloat("price") + " | " +
                                rs.getInt("quantity") + " | " +
                                rs.getString("origin")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void logToDB(String entries) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO log (entries) VALUES (?)"
            );
            ps.setString(1, entries);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewLog() {
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT entries, timestamp FROM log ORDER BY idlog DESC"
            );
            while (rs.next()) {
                System.out.println(rs.getString("timestamp") + " | " + rs.getString("entries"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create all necessary tables
    public void initializeDatabases(){
        try {
            Statement createstatement = conn.createStatement();

            // Create inventory table
            createstatement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS inventory (
                    idinventory INT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    price DOUBLE NOT NULL,
                    quantity INT NOT NULL,
                    origin VARCHAR(255) NOT NULL,
                    PRIMARY KEY (idinventory)
                )                
            """);

            // Create log table
            createstatement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS log (
                    idlog INT NOT NULL AUTO_INCREMENT,
                    entries VARCHAR(255) NOT NULL,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (idlog)
                )                
            """);

            // Create users table
            createstatement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) PRIMARY KEY,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(255) NOT NULL
                )                
            """);

        } catch (Exception e) {
            return;
        }
    }

    public boolean isTableEmpty(String tableName) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " LIMIT 1");

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
        }
        return true;
    }

    // Add user to database
    public void addUser(String username, String password, String role, String user) {

        logToDB(user + " created login for " + username + " (" + role + ")");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO users (username, password, role) Values (?, ?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove user from database
    public void removeUser(String username, String user) {

        logToDB(user + " Removed user: " + username);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?"
            );
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();

        
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get role of username
    public String getRole(String username) {
        String role = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT role FROM users WHERE username = ?"
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    // If login match return role
    public String login(String username, String password) {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                if (rs.getString("username").equals(username)){
                    if (rs.getString("password").equals(password)){
                        return rs.getString("role");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean loginExist(String username){
        try {
            String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
        }
        return false;
    }
}