// ===== CreatePostDialog.java =====
package com.codenest.ui.dialog;

import com.codenest.controller.CommunityController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreatePostDialog extends JDialog {
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton postBtn;
    private JButton cancelBtn;
    private CommunityController communityController;
    private int communityId;
    private boolean success = false;
    
    public CreatePostDialog(Frame parent, int communityId) {
        super(parent, "Create New Post", true);
        this.communityId = communityId;
        this.communityController = new CommunityController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        titleField = new JTextField(40);
        contentArea = new JTextArea(10, 40);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        postBtn = new JButton("Post");
        postBtn.setBackground(new Color(41, 128, 185));
        postBtn.setForeground(Color.WHITE);
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Post Title
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Post Title:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        mainPanel.add(titleField, gbc);
        
        // Content
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Content:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        mainPanel.add(scrollPane, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(postBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void setupEventHandlers() {
        postBtn.addActionListener(e -> createPost());
        cancelBtn.addActionListener(e -> dispose());
        
        getRootPane().setDefaultButton(postBtn);
    }
    
    private void createPost() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a post title.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter post content.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (communityController.createPost(communityId, title, content)) {
            success = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create post. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSuccess() {
        return success;
    }
}