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
                "jdbc:mysql://127.0.0.1:3306/login", "root", "raposisnice"
        );
    }

    // constructor for unit testing with mock objects
    public InventorySystem(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {return conn;}

    // method to add item to database
    public void addItem(int id, String name, double price, int quantity, String origin) {

        logToDB("Added item: " + name);

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
    public void removeItem(String name) {

        logToDB("Removed item: " + name);

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

    public void updatePrice(String name, double newPrice) {

        logToDB("Updated price for: " + name);

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

    public void updateQuantity(String name, int newQuantity) {

        logToDB("Updated quantity for: " + name);

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
                    "SELECT entries, timestamp FROM log ORDER BY idlog ASC"
            );
            while (rs.next()) {
                System.out.println(rs.getString("timestamp") + " | " + rs.getString("entries"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}