package com.codenest.ui;

import com.codenest.controller.ProblemController;
import com.codenest.controller.QuizController;
import com.codenest.model.User;
import com.codenest.model.Problem;
import com.codenest.model.Quiz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class StudentDashboard extends JFrame {
    private User currentUser;
    private ProblemController problemController;
    private QuizController quizController;
    private JLabel solvedProblemsLabel;
    private JLabel quizScoreLabel;
    
    public StudentDashboard(User user) {
        this.currentUser = user;
        this.problemController = new ProblemController();
        this.quizController = new QuizController();
        initializeComponents();
        updateDashboardStats();
    }
    
    private void initializeComponents() {
        setTitle("CodeNest - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Dashboard tab
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        
        // Programming tab
        tabbedPane.addTab("Programming", createProgrammingPanel());
        
        // Quiz tab
        tabbedPane.addTab("Quiz", createQuizPanel());
        
        // Community tab
        tabbedPane.addTab("Community", createCommunityPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Your Progress"));
        
        solvedProblemsLabel = new JLabel("Solved Problems: Loading...");
        solvedProblemsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        quizScoreLabel = new JLabel("Quiz Score: Loading...");
        quizScoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel streakLabel = new JLabel("Current Streak: 5 days");
        streakLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel badgesLabel = new JLabel("Badges Earned: 3");
        badgesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        statsPanel.add(solvedProblemsLabel);
        statsPanel.add(quizScoreLabel);
        statsPanel.add(streakLabel);
        statsPanel.add(badgesLabel);
        
        // Leaderboard panel
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder("Leaderboard"));
        
        String[] columnNames = {"Rank", "Name", "Score"};
        String[][] data = {
            {"1", "Alice Smith", "850"},
            {"2", "Bob Johnson", "720"},
            {"3", currentUser.getName(), "650"},
            {"4", "Carol Brown", "580"},
            {"5", "David Wilson", "520"}
        };
        
        JTable leaderboardTable = new JTable(data, columnNames);
        leaderboardTable.setRowSelectionAllowed(false);
        leaderboardPanel.add(new JScrollPane(leaderboardTable));
        
        panel.add(welcomePanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(leaderboardPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createProgrammingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by:"));
        
        JComboBox<String> difficultyFilter = new JComboBox<>(new String[]{"All", "Easy", "Medium", "Hard"});
        JComboBox<String> patternFilter = new JComboBox<>(new String[]{"All", "Arrays", "Strings", "Trees", "Graphs", "Dynamic Programming"});
        
        filterPanel.add(new JLabel("Difficulty:"));
        filterPanel.add(difficultyFilter);
        filterPanel.add(new JLabel("Pattern:"));
        filterPanel.add(patternFilter);
        
        // Problems list
        JPanel problemsPanel = new JPanel(new BorderLayout());
        problemsPanel.setBorder(BorderFactory.createTitledBorder("Coding Problems"));
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> problemsList = new JList<>(listModel);
        problemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Load problems
        loadProblems(listModel);
        
        // Problem details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JTextArea problemDetails = new JTextArea();
        problemDetails.setEditable(false);
        problemDetails.setWrapStyleWord(true);
        problemDetails.setLineWrap(true);
        detailsPanel.add(new JScrollPane(problemDetails));
        
        JButton solveButton = new JButton("Mark as Solved");
        solveButton.addActionListener(e -> {
            int selectedIndex = problemsList.getSelectedIndex();
            if (selectedIndex != -1) {
                markProblemAsSolved(selectedIndex + 1);
                updateDashboardStats();
            }
        });
        
        detailsPanel.add(solveButton, BorderLayout.SOUTH);
        
        // List selection listener
        problemsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = problemsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    loadProblemDetails(selectedIndex, problemDetails);
                }
            }
        });
        
        problemsPanel.add(new JScrollPane(problemsList), BorderLayout.WEST);
        problemsPanel.add(detailsPanel, BorderLayout.CENTER);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(problemsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Available Quizzes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // Quiz list
        DefaultListModel<String> quizListModel = new DefaultListModel<>();
        JList<String> quizList = new JList<>(quizListModel);
        
        loadQuizzes(quizListModel);
        
        JButton startQuizButton = new JButton("Start Quiz");
        startQuizButton.addActionListener(e -> {
            int selectedIndex = quizList.getSelectedIndex();
            if (selectedIndex != -1) {
                startQuiz(selectedIndex + 1);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(startQuizButton);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(quizList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCommunityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Community Features");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextArea communityInfo = new JTextArea();
        communityInfo.setEditable(false);
        communityInfo.setText("Community features:\n\n" +
                             "• Join programming communities\n" +
                             "• Share solutions and discuss problems\n" +
                             "• Get help from peers and mentors\n" +
                             "• Participate in coding challenges\n\n" +
                             "This feature is coming soon!");
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(communityInfo), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadProblems(DefaultListModel<String> listModel) {
        try {
            List<Problem> problems = problemController.getAllProblems();
            for (Problem problem : problems) {
                listModel.addElement(problem.getTitle() + " [" + problem.getDifficulty() + "]");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading problems: " + e.getMessage());
        }
    }
    
    private void loadProblemDetails(int index, JTextArea detailsArea) {
        try {
            List<Problem> problems = problemController.getAllProblems();
            if (index < problems.size()) {
                Problem problem = problems.get(index);
                detailsArea.setText("Title: " + problem.getTitle() + "\n\n" +
                                  "Difficulty: " + problem.getDifficulty() + "\n" +
                                  "Pattern: " + problem.getPattern() + "\n\n" +
                                  "Description:\n" + problem.getDescription());
            }
        } catch (Exception e) {
            detailsArea.setText("Error loading problem details.");
        }
    }
    
    private void loadQuizzes(DefaultListModel<String> listModel) {
        try {
            List<Quiz> quizzes = quizController.getAllQuizzes();
            for (Quiz quiz : quizzes) {
                listModel.addElement(quiz.getTitle() + " (" + quiz.getSubject() + ") - " + quiz.getTimeLimit() + " mins");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading quizzes: " + e.getMessage());
        }
    }
    
    private void markProblemAsSolved(long problemId) {
        try {
            boolean success = problemController.markProblemAsSolved(currentUser.getId(), problemId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Problem marked as solved!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void startQuiz(long quizId) {
        try {
            new QuizWindow(currentUser, quizId, this).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error starting quiz: " + e.getMessage());
        }
    }
    
    void updateDashboardStats() {
        try {
            int solvedCount = problemController.getSolvedProblemsCount(currentUser.getId());
            int quizScore = quizController.getTotalQuizScore(currentUser.getId());
            
            solvedProblemsLabel.setText("Solved Problems: " + solvedCount);
            quizScoreLabel.setText("Quiz Score: " + quizScore);
        } catch (Exception e) {
            solvedProblemsLabel.setText("Solved Problems: Error");
            quizScoreLabel.setText("Quiz Score: Error");
        }
    }
}