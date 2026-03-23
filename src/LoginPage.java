import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPage extends JFrame {

    public LoginPage() {
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

        // Role label
        JLabel roleLabel = new JLabel("Login as:");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0;
        panel.add(roleLabel, gbc);

        // Role dropdown
        String[] roles = {"owner", "manager", "employee"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        roleDropdown.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        panel.add(roleDropdown, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setBackground(new Color(100, 180, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(200, 40));
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        // Login action
        loginBtn.addActionListener(e -> {
            String role = (String) roleDropdown.getSelectedItem();
            try {
                InventorySystem system = new InventorySystem();
                dispose();
                if (role.equals("employee")) {
                    new EmployeePage(system).setVisible(true);
                } else {
                    new ManagerPage(system, role).setVisible(true);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed!\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Close button
        JButton closeBtn = new JButton("Exit");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        closeBtn.setBackground(new Color(200, 70, 70));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setPreferredSize(new Dimension(200, 40));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
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