import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class EmployeePage extends JFrame {

    private InventorySystem system;
    private DefaultTableModel tableModel;
    private JTable table;

    public EmployeePage(InventorySystem system) {
        this.system = system;

        setTitle("CartPilot - Employee Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(20, 20, 20));
        header.setPreferredSize(new Dimension(700, 60));
        JLabel headerLabel = new JLabel("CartPilot  |  EMPLOYEE DASHBOARD");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(100, 180, 255));
        header.add(headerLabel);
        mainPanel.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Price", "Quantity", "Origin"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.getTableHeader().setBackground(new Color(100, 180, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));

        JButton refreshBtn = createButton("Refresh Inventory", new Color(100, 100, 100));
        JButton updateQtyBtn = createButton("Update Quantity", new Color(70, 130, 200));
        JButton logoutBtn = createButton("Logout", new Color(80, 80, 80));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(updateQtyBtn);
        buttonPanel.add(logoutBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        refreshBtn.addActionListener(e -> loadInventory());
        updateQtyBtn.addActionListener(e -> updateQuantity());
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
        btn.setPreferredSize(new Dimension(160, 35));
        return btn;
    }

    private void loadInventory() {
        tableModel.setRowCount(0);
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
}