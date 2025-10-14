// ===== CreateCommunityDialog.java =====
package com.codenest.ui.dialog;

import com.codenest.controller.CommunityController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateCommunityDialog extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton createBtn;
    private JButton cancelBtn;
    private CommunityController communityController;
    private boolean success = false;
    
    public CreateCommunityDialog(Frame parent) {
        super(parent, "Create New Community", true);
        this.communityController = new CommunityController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        nameField = new JTextField(30);
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        createBtn = new JButton("Create");
        createBtn.setBackground(new Color(46, 204, 113));
        createBtn.setForeground(Color.WHITE);
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Community Name
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Community Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setSize(500, 300);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void setupEventHandlers() {
        createBtn.addActionListener(e -> createCommunity());
        cancelBtn.addActionListener(e -> dispose());
        
        getRootPane().setDefaultButton(createBtn);
    }
    
    private void createCommunity() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a community name.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (communityController.createCommunity(name, description)) {
            success = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create community. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSuccess() {
        return success;
    }
}