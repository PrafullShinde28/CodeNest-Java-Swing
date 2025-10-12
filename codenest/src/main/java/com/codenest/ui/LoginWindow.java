package com.codenest.ui;

import com.codenest.controller.AuthController;
import com.codenest.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JRadioButton studentRadio;
    private JRadioButton adminRadio;
    private AuthController authController;
    
    public LoginWindow() {
        this.authController = new AuthController();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("CodeNest - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("CodeNest Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Email field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Role selection
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentRadio = new JRadioButton("Student", true);
        adminRadio = new JRadioButton("Admin");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(adminRadio);
        rolePanel.add(studentRadio);
        rolePanel.add(adminRadio);
        formPanel.add(rolePanel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        loginButton.addActionListener(new LoginActionListener());
        registerButton.addActionListener(e -> openRegistrationWindow());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            User.Role selectedRole = studentRadio.isSelected() ? User.Role.STUDENT : User.Role.ADMIN;
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginWindow.this, 
                    "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                User user = authController.authenticate(email, password);
                if (user != null && user.getRole() == selectedRole) {
                    JOptionPane.showMessageDialog(LoginWindow.this, 
                        "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Open appropriate dashboard
                    if (user.getRole() == User.Role.STUDENT) {
                        new StudentDashboard(user).setVisible(true);
                    } else {
                        new AdminDashboard(user).setVisible(true);
                    }
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this, 
                        "Invalid credentials or role", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginWindow.this, 
                    "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openRegistrationWindow() {
        new RegistrationWindow(this).setVisible(true);
        setVisible(false);
    }
}