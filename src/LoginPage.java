import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPage extends JFrame {

    public LoginPage() {
        try {
            InventorySystem system = new InventorySystem();
            system.initializeDatabases();
            if (system.isTableEmpty("users")){
                JTextField idField = new JTextField(15);
                JPasswordField passField = new JPasswordField(15);

                JPanel createPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;

                gbc.gridx = 0; gbc.gridy = 0;
                createPanel.add(new JLabel("Username:"), gbc);

                gbc.gridx = 1;
                createPanel.add(idField, gbc);

                gbc.gridx = 0; gbc.gridy = 1;
                createPanel.add(new JLabel("Password:"), gbc);

                gbc.gridx = 1;
                createPanel.add(passField, gbc);

                int result = JOptionPane.showConfirmDialog(this, createPanel, "Create owner login", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.CANCEL_OPTION) {System.exit(0);}
                if (result == JOptionPane.OK_OPTION) {
                    char[] passchar = passField.getPassword();
                    system.addUser(idField.getText(), new String(passchar), "Owner");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
        }
        setTitle("CartPilot - Login");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel title = new JLabel("CartPilot");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(100, 180, 255));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Inventory Management System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        // Username and Password label and TextFields
        JLabel usernamelabel = new JLabel("Username:");
        usernamelabel.setForeground(Color.WHITE);
        usernamelabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0;
        panel.add(usernamelabel, gbc);

        JLabel passwordlabel = new JLabel("Password:");
        passwordlabel.setForeground(Color.WHITE);
        passwordlabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridx = 0;
        panel.add(passwordlabel, gbc);
        
        JTextField username = new JTextField(15);
        JPasswordField password = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(username, gbc);
        gbc.gridy = 3; 
        panel.add(password, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setBackground(new Color(100, 180, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(200, 40));
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        // Login action
        loginBtn.addActionListener(e -> {
            String user = username.getText();
            char[] passchar = password.getPassword();
            String pass = new String(passchar);
            try {
                InventorySystem system = new InventorySystem();
                String role = system.login(user,pass);
                if (role == null) {
                    JOptionPane.showMessageDialog(null, "Invalid user.");
                }
                else if (role.toLowerCase().equals("owner") || role.toLowerCase().equals("manager")){
                    dispose();
                    new ManagerPage(system, role).setVisible(true);
                }
                else {
                    dispose();
                    new EmployeePage(system).setVisible(true);
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed!\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hit enter to login
        getRootPane().setDefaultButton(loginBtn);

        // Close button
        JButton closeBtn = new JButton("Exit");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        closeBtn.setBackground(new Color(200, 70, 70));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setPreferredSize(new Dimension(200, 40));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(closeBtn, gbc);

        // Close action
        closeBtn.addActionListener(e -> System.exit(0));

        add(panel);
    }

    public boolean validateRole(String role) {
        return role.equals("owner") || role.equals("manager") || role.equals("employee");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}