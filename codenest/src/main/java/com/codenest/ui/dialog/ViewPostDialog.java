
package com.codenest.ui.dialog;

import com.codenest.model.Post;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ViewPostDialog extends JDialog {
    private Post post;
    
    public ViewPostDialog(Frame parent, Post post) {
        super(parent, "View Post", true);
        this.post = post;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // No additional components needed
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        // Header with author and date
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel authorLabel = new JLabel("Posted by: " + post.getAuthorName());
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        authorLabel.setForeground(Color.WHITE);
        JLabel dateLabel = new JLabel(formatDate(post.getCreatedAt()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.WHITE);
        headerPanel.add(authorLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        // Title
        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(new EmptyBorder(15, 0, 10, 0));
        // Content
        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setForeground(new Color(52, 73, 94));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(new EmptyBorder(10, 0, 10, 0));
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);
        // Assemble
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private String formatDate(java.time.LocalDateTime date) {
        if (date == null) return "";
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return date.format(formatter);
    }
}
