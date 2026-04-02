import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ManagerPage extends JFrame {

    private InventorySystem system;
    private String role;
    private String user;
    private DefaultTableModel tableModel;
    private JTable table;

    public ManagerPage(InventorySystem system, String role, String user) {
        this.system = system;
        this.role = role;
        this.user = user;

        setTitle("CartPilot - " + role.substring(0,1).toUpperCase() + role.substring(1) + " Dashboard");
        setSize(1100, 800);
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
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {return false;}
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
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(780, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        System.out.println(role);

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

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("settings");
        JMenuItem addUser = new JMenuItem("Add New User");
        JMenuItem removeUser = new JMenuItem("Remove User");
        menu.add(addUser);
        menu.add(removeUser);
        menubar.add(menu);

        this.setJMenuBar(menubar);

        // Pop up to add new user to database, Managers can only add employees
        addUser.addActionListener(e -> {
            JPanel addUserPane = new JPanel();
            addUserPane.setLayout(new BoxLayout(addUserPane, BoxLayout.Y_AXIS));

            JPanel usernamePane = new JPanel();
            usernamePane.add(new JLabel("Username: "));
            JTextField usernameInput = new JTextField(15);
            usernamePane.add(usernameInput);

            JPanel passwordPane = new JPanel();
            passwordPane.add(new JLabel("Password: "));
            JPasswordField passwordInput = new JPasswordField(15);
            passwordPane.add(passwordInput);

            JPanel rolePane = new JPanel();
            rolePane.add(new JLabel("Role: "));
            String [] roles = new String[] {"Employee", "Manager"};
            JComboBox<String> roleInput = new JComboBox<>(roles);
            rolePane.add(roleInput);

            addUserPane.add(usernamePane);
            addUserPane.add(passwordPane);
            addUserPane.add(rolePane);

            int result = JOptionPane.showConfirmDialog(
                null,
                addUserPane,
                "create new user",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.CANCEL_OPTION) {return;}

            if (result == JOptionPane.OK_OPTION) {
                String uname = usernameInput.getText().trim();
                char[] passChar = passwordInput.getPassword();
                String addPass = new String(passChar);
                String roleToAdd = roleInput.getSelectedItem().toString();

                // field validation
                if (uname.isEmpty() || addPass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty.");
                    return;
                }
                // 4 or more check for passwords
                if (addPass.length() < 4) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 4 characters.");
                    return;
                }
                if (system.loginExist(uname)) {
                    JOptionPane.showMessageDialog(null, "Username '" + uname + "' already exists.");
                    return;
                }

                if (!role.toLowerCase().equals("owner")) { // Manager can only add employees
                    if (roleToAdd.equals("Employee")) {
                        system.addUser(uname, addPass, roleToAdd, user);
                        JOptionPane.showMessageDialog(null, "Login created for: " + uname + " (" + roleToAdd + ")");
                    } else {
                        JOptionPane.showMessageDialog(null, "You do not have permission to add " + roleToAdd + " users.");
                    }
                } else { // Owner can add anyone
                    system.addUser(uname, addPass, roleToAdd, user);
                    JOptionPane.showMessageDialog(null, "Login created for: " + uname + " (" + roleToAdd + ")");
                }
            }
        });

        // Pop up to remove users from database with role awareness
        removeUser.addActionListener(e -> {
            JPanel removePane = new JPanel();
            removePane.add(new JLabel("Username: "));
            JTextField userRemove = new JTextField(15);
            removePane.add(userRemove);
                    
            int result = JOptionPane.showConfirmDialog(
            null,
            removePane,
            "Remove user",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.CANCEL_OPTION){return;}
            if (result == JOptionPane.OK_OPTION){
                if (system.loginExist(userRemove.getText())){
                    if (role.toLowerCase().equals("manager")){
                        if ((system.getRole(userRemove.getText()).toLowerCase().equals("employee"))) {
                            system.removeUser(userRemove.getText(), user);
                            JOptionPane.showMessageDialog(null, "Successfully removed user " + userRemove.getText() + ".");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "You do not have permission to remove " + system.getRole(userRemove.getText()) + " users.");
                        }
                    }
                    else {
                        system.removeUser(userRemove.getText(), user);
                        JOptionPane.showMessageDialog(null, "Successfully removed user " + userRemove.getText() + ".");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Login doesn't exist.");
                }
            }
        });

        table.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '='){
                    try {
                        system.updateQuantity( (String) table.getValueAt(table.getSelectedRow(), 1), (int) table.getValueAt(table.getSelectedRow(), 3) + 1, user);
                        loadInventory();
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "Please select a row.");
                    }
                }
                else if (e.getKeyChar() == '-'){
                    try {
                        if ((int) table.getValueAt(table.getSelectedRow(), 3) - 1 >= 0){
                            system.updateQuantity( (String) table.getValueAt(table.getSelectedRow(), 1), (int) table.getValueAt(table.getSelectedRow(), 3) - 1, user);
                            loadInventory();
                        }
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "Please select a row.");
                    }
                }
                else if (e.getKeyChar() == 'r'){
                    removeBtn.doClick();
                }
                else if (e.getKeyChar() == 'p') {
                    updatePriceBtn.doClick();
                }
                else if (e.getKeyChar() == 'q') {
                    updateQtyBtn.doClick();
                }
            }
        });
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
            String name = nameField.getText().trim();
            String origin = originField.getText().trim();

            // Empty field checks
            if (idField.getText().trim().isEmpty() || name.isEmpty() ||
                    priceField.getText().trim().isEmpty() || qtyField.getText().trim().isEmpty() ||
                    origin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            try {
                int id =  Integer.parseInt(idField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(qtyField.getText().trim());

                // negative value checks
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "Item ID must be greater than 0.");
                    return;
                }
                if (price < 0) {
                    JOptionPane.showMessageDialog(this, "Price cannot be negative.");
                    return;
                }
                if (quantity < 0) {
                    JOptionPane.showMessageDialog(this, "Quantity cannot be negative.");
                    return;
                }
                system.addItem(id, name, price, quantity, origin, user);
                loadInventory();
                JOptionPane.showMessageDialog(this, "Item added successfully!");

            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID and Quantity must be whole numbers. Price must be a number.");
            }
            catch (Exception e) {
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
                system.removeItem(name, user);
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
            Object[] fields = { "New Price for " + name + ":", newPrice };

            int result = JOptionPane.showConfirmDialog(this, fields, "Update Price", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) { return; }

            // Empty check
            if (newPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Price cannot be empty.");
                return;
            }

            // negative price check
            double price = Double.parseDouble(newPrice.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(null, "Price cannot be negative.");
                return;
            }

            system.updatePrice(name, price, user);
            JOptionPane.showMessageDialog(null, "Price for " + name + " updated to " + price);
            loadInventory();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number for price.");
        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "Please select a row.");
        }
    }

    private void updateQuantity() {
        try {
            String name = (String) table.getValueAt(table.getSelectedRow(), 1);
            JTextField newQuantity = new JTextField();
            Object[] fields = { "New Quantity for " + name + ":", newQuantity };

            int result = JOptionPane.showConfirmDialog(this, fields, "Update Quantity", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) { return; }

            // Empty check
            if (newQuantity.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Quantity cannot be empty.");
                return;
            }
            // qty negative check
            int qty = Integer.parseInt(newQuantity.getText().trim());
            if (qty < 0) {
                JOptionPane.showMessageDialog(null, "Quantity cannot be negative.");
                return;
            }

            system.updateQuantity(name, qty, user);
            JOptionPane.showMessageDialog(null, "Quantity for " + name + " updated to " + qty);
            loadInventory();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid whole number for quantity.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select a row.");
        }
    }

    private void viewLog() {
        JTextArea logArea = new JTextArea(30, 70);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(Color.WHITE);

        try {
            java.sql.Connection conn = system.getConnection();
            java.sql.ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT entries, timestamp FROM log ORDER BY idlog DESC");
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