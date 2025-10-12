// ===== ADMIN DASHBOARD =====
package com.codenest.ui;

import com.codenest.model.User;
import com.codenest.model.Problem;
import com.codenest.controller.AuthController;
import com.codenest.controller.ProblemController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private AuthController authController;
    private ProblemController problemController;
    
    public AdminDashboard(User user) {
        this.currentUser = user;
        this.authController = new AuthController();
        this.problemController = new ProblemController();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("CodeNest - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Dashboard tab
        tabbedPane.addTab("Dashboard", createAdminDashboardPanel());
        
        // Manage Problems tab
        tabbedPane.addTab("Manage Problems", createManageProblemsPanel());
        
        // Manage Users tab
        tabbedPane.addTab("Manage Users", createManageUsersPanel());
        
        // Manage Quizzes tab
        tabbedPane.addTab("Manage Quizzes", createManageQuizzesPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createAdminDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Admin Dashboard - Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Statistics"));
        
        JLabel totalUsersLabel = new JLabel("Total Users: 156");
        totalUsersLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel totalProblemsLabel = new JLabel("Total Problems: 30");
        totalProblemsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel totalQuizzesLabel = new JLabel("Total Quizzes: 5");
        totalQuizzesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel activeUsersLabel = new JLabel("Active Users (Today): 42");
        activeUsersLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        statsPanel.add(totalUsersLabel);
        statsPanel.add(totalProblemsLabel);
        statsPanel.add(totalQuizzesLabel);
        statsPanel.add(activeUsersLabel);
        
        // Recent activity panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Recent Activity"));
        
        JTextArea activityArea = new JTextArea(10, 50);
        activityArea.setEditable(false);
        activityArea.setText("Recent System Activity:\n\n" +
                           "• John Doe solved 'Two Sum' problem\n" +
                           "• Jane Smith completed 'Java Programming' quiz\n" +
                           "• Mike Johnson joined 'Competitive Programming' community\n" +
                           "• Alice Brown achieved 'Problem Solver' badge\n" +
                           "• 3 new users registered today\n");
        
        activityPanel.add(new JScrollPane(activityArea));
        
        panel.add(welcomePanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(activityPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createManageProblemsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Manage Programming Problems");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addProblemButton = new JButton("Add New Problem");
        JButton editProblemButton = new JButton("Edit Problem");
        JButton deleteProblemButton = new JButton("Delete Problem");
        
        addProblemButton.addActionListener(e -> openAddProblemDialog());
        
        buttonPanel.add(addProblemButton);
        buttonPanel.add(editProblemButton);
        buttonPanel.add(deleteProblemButton);
        
        // Problems table
        String[] columnNames = {"ID", "Title", "Difficulty", "Pattern", "Created"};
        String[][] data = {
            {"1", "Two Sum", "Easy", "Arrays", "2024-01-15"},
            {"2", "Reverse String", "Easy", "Strings", "2024-01-15"},
            {"3", "Maximum Depth of Binary Tree", "Easy", "Trees", "2024-01-15"},
            {"4", "3Sum", "Medium", "Arrays", "2024-01-16"},
            {"5", "N-Queens", "Hard", "Arrays", "2024-01-16"}
        };
        
        JTable problemsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(problemsTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createManageUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Manage Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Users table
        String[] columnNames = {"ID", "Name", "Email", "Role", "Joined", "Status"};
        String[][] data = {
            {"1", "Admin User", "admin@codenest.com", "Admin", "2024-01-01", "Active"},
            {"2", "John Doe", "john@example.com", "Student", "2024-01-15", "Active"},
            {"3", "Jane Smith", "jane@example.com", "Student", "2024-01-16", "Active"},
            {"4", "Mike Johnson", "mike@example.com", "Student", "2024-01-17", "Active"}
        };
        
        JTable usersTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createManageQuizzesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Manage Quizzes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setText("Quiz Management Features:\n\n" +
                        "• Add new quizzes\n" +
                        "• Edit existing quizzes\n" +
                        "• Manage quiz questions\n" +
                        "• Set time limits\n" +
                        "• View quiz statistics\n\n" +
                        "This feature is under development.");
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void openAddProblemDialog() {
        JDialog dialog = new JDialog(this, "Add New Problem", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField titleField = new JTextField(20);
        panel.add(titleField, gbc);
        
        // Difficulty combo
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Difficulty:"), gbc);
        gbc.gridx = 1;
        JComboBox<Problem.Difficulty> difficultyCombo = new JComboBox<>(Problem.Difficulty.values());
        panel.add(difficultyCombo, gbc);
        
        // Pattern combo
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Pattern:"), gbc);
        gbc.gridx = 1;
        JComboBox<Problem.Pattern> patternCombo = new JComboBox<>(Problem.Pattern.values());
        panel.add(patternCombo, gbc);
        
        // Description area
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        JTextArea descriptionArea = new JTextArea(10, 20);
        panel.add(new JScrollPane(descriptionArea), gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            Problem.Difficulty difficulty = (Problem.Difficulty) difficultyCombo.getSelectedItem();
            Problem.Pattern pattern = (Problem.Pattern) patternCombo.getSelectedItem();
            
            if (!title.isEmpty() && !description.isEmpty()) {
                Problem problem = new Problem(title, description, difficulty, pattern);
                try {
                    problemController.addProblem(problem);
                    JOptionPane.showMessageDialog(dialog, "Problem added successfully!");
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error adding problem: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}