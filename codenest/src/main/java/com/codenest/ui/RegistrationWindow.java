package com.codenest.ui;

import com.codenest.controller.AuthController;
import com.codenest.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationWindow extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JRadioButton studentRadio;
    private JRadioButton adminRadio;
    private AuthController authController;
    private LoginWindow parentWindow;
    
    public RegistrationWindow(LoginWindow parent) {
        this.parentWindow = parent;
        this.authController = new AuthController();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("CodeNest - Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Email field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Confirm password field
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);
        
        // Role selection
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
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
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        
        registerButton.addActionListener(new RegisterActionListener());
        backButton.addActionListener(e -> {
            parentWindow.setVisible(true);
            dispose();
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            User.Role selectedRole = studentRadio.isSelected() ? User.Role.STUDENT : User.Role.ADMIN;
            
            // Validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationWindow.this, 
                    "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegistrationWindow.this, 
                    "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(RegistrationWindow.this, 
                    "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                User user = new User(name, email, password, selectedRole);
                boolean success = authController.registerUser(user);
                
                if (success) {
                    JOptionPane.showMessageDialog(RegistrationWindow.this, 
                        "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    parentWindow.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegistrationWindow.this, 
                        "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RegistrationWindow.this, 
                    "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}