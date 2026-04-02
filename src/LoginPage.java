import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPage extends JFrame {

    private InventorySystem system;

    public LoginPage() {
        try {
            system = new InventorySystem();
            system.initializeDatabases();

            // If no users exist, force create first owner account
            if (system.isTableEmpty("users")) {
                createFirstOwner();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        setTitle("CartPilot - Login");
        setSize(800, 500);
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

        // Username label and field
        JLabel usernamelabel = new JLabel("Username:");
        usernamelabel.setForeground(Color.WHITE);
        usernamelabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0;
        panel.add(usernamelabel, gbc);

        JTextField username = new JTextField(15);
        gbc.gridx = 1;
        panel.add(username, gbc);

        // Password label and field
        JLabel passwordlabel = new JLabel("Password:");
        passwordlabel.setForeground(Color.WHITE);
        passwordlabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3; gbc.gridx = 0;
        panel.add(passwordlabel, gbc);

        JPasswordField password = new JPasswordField(15);
        gbc.gridx = 1;
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

        // Register New User button
        JButton registerBtn = new JButton("Register New User");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        registerBtn.setBackground(new Color(70, 170, 70));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setPreferredSize(new Dimension(200, 40));
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        gbc.gridy = 5;


        // Exit button
        JButton closeBtn = new JButton("Exit");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        closeBtn.setBackground(new Color(200, 70, 70));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setPreferredSize(new Dimension(200, 40));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        gbc.gridy = 6;
        panel.add(closeBtn, gbc);

        // Login action
        loginBtn.addActionListener(e -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword());

            // username and password field check
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password.");
                return;
            }

            String role = system.login(user, pass);
            if (role == null) {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            } else if (role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("manager")) {
                dispose();
                new ManagerPage(system, role, user).setVisible(true);
            } else {
                dispose();
                new EmployeePage(system, user).setVisible(true);
            }
        });

        // Register action
        registerBtn.addActionListener(e -> {
            String loggedInUser = username.getText();
            String pass = new String(password.getPassword());

            // Must be logged in as owner to register
            String role = system.login(loggedInUser, pass);
            if (role == null) {
                JOptionPane.showMessageDialog(this,
                        "You must log in as an Owner first to register new users.",
                        "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!role.equalsIgnoreCase("owner")) {
                JOptionPane.showMessageDialog(this,
                        "Only Owners can register new users.",
                        "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }
            showRegisterDialog(loggedInUser);
        });

        // Hit enter to login
        getRootPane().setDefaultButton(loginBtn);

        // Close action
        closeBtn.addActionListener(e -> System.exit(0));

        add(panel);
    }

    private void createFirstOwner() {
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

        int result = JOptionPane.showConfirmDialog(this, createPanel,
                "No users found — Create Owner Account", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.CANCEL_OPTION) System.exit(0);
        if (result == JOptionPane.OK_OPTION) {
            String username = idField.getText().trim();
            String userpassword = new String(passField.getPassword());

            // empty field check
            if (username.isEmpty() || userpassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password must both be filled.");
                createFirstOwner(); // re-show the dialog
                return;
            }
            if (userpassword.length() < 4) {
                JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.");
                createFirstOwner(); // re-show the dialog
                return;
            }
            system.addUser(username, userpassword, "Owner", "System");
        }
    }

    private void showRegisterDialog(String loggedInUser) {
        JTextField newUsername = new JTextField(15);
        JPasswordField newPassword = new JPasswordField(15);
        String[] roles = {"owner", "manager", "employee"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);

        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setPreferredSize(new Dimension(300, 150));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        registerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(newUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(newPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        registerPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(roleDropdown, gbc);

        int result = JOptionPane.showConfirmDialog(this, registerPanel,
                "Register New User", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String uname = newUsername.getText().trim();
            String upass = new String(newPassword.getPassword());
            String urole = (String) roleDropdown.getSelectedItem();

            if (uname.isEmpty() || upass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
                return;
            }
            if (system.loginExist(uname)) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
                return;
            }

            system.addUser(uname, upass, urole, loggedInUser);
            JOptionPane.showMessageDialog(this,
                    "User '" + uname + "' registered as " + urole + " successfully!");
        }
    }

    public boolean validateRole(String role) {
        return role.equals("owner") || role.equals("manager") || role.equals("employee");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}