import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class ManagerPage extends JFrame {

    private InventorySystem system;
    private String role;
    private DefaultTableModel tableModel;
    private JTable table;

    public ManagerPage(InventorySystem system, String role) {
        this.system = system;
        this.role = role;
        system.initializeDatabases();

        setTitle("CartPilot - " + role.substring(0,1).toUpperCase() + role.substring(1) + " Dashboard");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(20, 20, 20));
        header.setPreferredSize(new Dimension(800, 60));
        JLabel headerLabel = new JLabel("CartPilot  |  " + role.toUpperCase() + " DASHBOARD");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(100, 180, 255));
        header.add(headerLabel);
        mainPanel.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Price", "Quantity", "Origin"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.getTableHeader().setBackground(new Color(100, 180, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(780, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addBtn = createButton("Add Item", new Color(70, 170, 70));
        JButton removeBtn = createButton("Remove Item", new Color(200, 70, 70));
        JButton updatePriceBtn = createButton("Update Price", new Color(70, 130, 200));
        JButton updateQtyBtn = createButton("Update Quantity", new Color(70, 130, 200));
        JButton refreshBtn = createButton("Refresh", new Color(100, 100, 100));
        JButton logBtn = createButton("View Log", new Color(150, 100, 200));
        JButton logoutBtn = createButton("Logout", new Color(80, 80, 80));

        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(updatePriceBtn);
        buttonPanel.add(updateQtyBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(logBtn);
        buttonPanel.add(logoutBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        addBtn.addActionListener(e -> addItem());
        removeBtn.addActionListener(e -> removeItem());
        updatePriceBtn.addActionListener(e -> updatePrice());
        updateQtyBtn.addActionListener(e -> updateQuantity());
        refreshBtn.addActionListener(e -> loadInventory());
        logBtn.addActionListener(e -> viewLog());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });

        loadInventory();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    private void loadInventory() {
        tableModel.setRowCount(0);
        // Load from DB via InventorySystem
        try {
            java.sql.Connection conn = system.getConnection();
            java.sql.ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT * FROM inventory");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("idinventory"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getInt("quantity"),
                        rs.getString("origin")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
        }
    }

    private void addItem() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField originField = new JTextField();

        Object[] fields = {
                "Item ID:", idField,
                "Name:", nameField,
                "Price:", priceField,
                "Quantity:", qtyField,
                "Origin:", originField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                system.addItem(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(qtyField.getText()),
                        originField.getText()
                );
                loadInventory();
                JOptionPane.showMessageDialog(this, "Item added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void removeItem() {
        try {    
            String name = (String) table.getValueAt(table.getSelectedRow(), 1);
            JPanel pane = new JPanel();
            pane.add(new JLabel("Are you sure you want to delete " + name + "?"));
            int result = JOptionPane.showConfirmDialog(
                null,
                pane,
                "Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.NO_OPTION) { return; }
            if (result == JOptionPane.YES_OPTION){
                system.removeItem(name);
                loadInventory();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select the row you would like to remove.");
        }
    }

    private void updatePrice() {
        try {
            String name = (String) table.getValueAt(table.getSelectedRow(), 1);
            JTextField newPrice = new JTextField();
            Object[] fields = {
                    "New Price for " + name + ":", newPrice
            };
            int result = JOptionPane.showConfirmDialog(this, fields, "Update Price", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.NO_OPTION) { return; }
            if (result == JOptionPane.YES_OPTION){
                system.updatePrice(name, Double.parseDouble(newPrice.getText()));
                JOptionPane.showMessageDialog(null, "New price for " + name + " updated to " + Double.parseDouble(newPrice.getText()));
                loadInventory();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select the row you would like to update.");
        }
    }

    private void updateQuantity() {
        try {
            String name = (String) table.getValueAt(table.getSelectedRow(), 1);
            JTextField newQuantity = new JTextField();
            Object[] fields = {
                    "New Quantity for " + name + ":", newQuantity
            };
            int result = JOptionPane.showConfirmDialog(this, fields, "Update Quantity", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.NO_OPTION) { return; }
            if (result == JOptionPane.YES_OPTION){
                system.updateQuantity(name, Integer.parseInt(newQuantity.getText()));
                JOptionPane.showMessageDialog(null, "New quantity for " + name + " updated to " + Integer.parseInt(newQuantity.getText()));
                loadInventory();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select the row you would like to update.");
        }
    }

    private void viewLog() {
        JTextArea logArea = new JTextArea(20, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(Color.WHITE);

        try {
            java.sql.Connection conn = system.getConnection();
            java.sql.ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT entries, timestamp FROM log ORDER BY idlog ASC");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append(rs.getString("timestamp"))
                        .append(" | ")
                        .append(rs.getString("entries"))
                        .append("\n");
            }
            logArea.setText(sb.toString());
        } catch (SQLException e) {
            logArea.setText("Error loading log: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(this,
                new JScrollPane(logArea), "Activity Log",
                JOptionPane.PLAIN_MESSAGE);
    }
}