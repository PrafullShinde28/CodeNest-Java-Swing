package com.codenest.ui;
import com.codenest.util.SessionManager;

import com.codenest.controller.ProblemController;
import com.codenest.controller.QuizController;
import com.codenest.model.User;
import com.codenest.model.Problem;
import com.codenest.model.Quiz;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.codenest.ui.CommunityPanel;

public class StudentDashboard extends JFrame {
    private User currentUser;
    private ProblemController problemController;
    private QuizController quizController;
    private JLabel solvedProblemsLabel;
    private JLabel quizScoreLabel;

    // Filtering-related fields
    private List<Problem> allProblems = new ArrayList<>();
    private DefaultListModel<Problem> problemListModel = new DefaultListModel<>();
    private JList<Problem> problemsList;             // will be set in createProgrammingPanel
    private JComboBox<String> difficultyFilter;      // will be set in createProgrammingPanel
    private JComboBox<String> patternFilter;         // will be set in createProgrammingPanel
    private JTextArea problemDetails;                // reuse the details area

    // split pane and controls
    private JSplitPane split;                  // make split pane a field
    private JSlider dividerSlider;
    private JSpinner dividerSpinner;
    private JLabel dividerLabel;

    // optional: persist user choice
    private static final String PREF_DIVIDER = "problems_split_percent";
    private java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(StudentDashboard.class);

  public StudentDashboard(User user) {
    this.currentUser = user;

    // ⭐ REQUIRED FIX ⭐
    SessionManager.getInstance().login(user);

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
        tabbedPane.addTab("Community", new CommunityPanel(currentUser));

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

        difficultyFilter = new JComboBox<>(new String[]{"All", "EASY", "MEDIUM", "HARD"});
        // use the same enum names (EASY etc) to match Problem.Difficulty
        patternFilter = new JComboBox<>(new String[]{
            "All", "ARRAYS", "STRINGS", "TREES", "GRAPHS", "DYNAMIC_PROGRAMMING"
        });

        filterPanel.add(new JLabel("Difficulty:"));
        filterPanel.add(difficultyFilter);
        filterPanel.add(new JLabel("Pattern:"));
        filterPanel.add(patternFilter);

        // --- Divider controls (user chooses left pane percentage) ---
        dividerLabel = new JLabel("Left pane:");

        // Slider 10..90 (initial value loaded from prefs or default 40)
        int initialPercent = prefs.getInt(PREF_DIVIDER, 40);
        dividerSlider = new JSlider(10, 90, initialPercent); // limit extremes to keep UI usable
        dividerSlider.setMajorTickSpacing(10);
        dividerSlider.setPaintTicks(true);
        dividerSlider.setSnapToTicks(false); // smooth change

        // Spinner to show exact percent and allow typing
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(initialPercent, 10, 90, 1);
        dividerSpinner = new JSpinner(spinnerModel);

        // Put them compactly in a small panel
        JPanel dividerControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dividerControlPanel.add(dividerLabel);
        dividerControlPanel.add(dividerSlider);
        dividerControlPanel.add(dividerSpinner);

        // Add to filter panel (after existing filter items)
        filterPanel.add(new JLabel(" | ")); // small separator
        filterPanel.add(dividerControlPanel);

        // Problems list (use model of Problems)
        JPanel problemsPanel = new JPanel(new BorderLayout());
        problemsPanel.setBorder(BorderFactory.createTitledBorder("Coding Problems"));

        problemsList = new JList<>(problemListModel);
        problemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // cell renderer to show title + difficulty
        problemsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Problem) {
                    Problem p = (Problem) value;
                    setText(p.getTitle() + " [" + p.getDifficulty() + "]");
                }
                return this;
            }
        });

        // Problem details panel
        problemDetails = new JTextArea();
        problemDetails.setEditable(false);
        problemDetails.setWrapStyleWord(true);
        problemDetails.setLineWrap(true);
        // initial border/title
        problemDetails.setBorder(BorderFactory.createTitledBorder("Select a problem"));

        // Create scroll panes
        JScrollPane problemsListScroll = new JScrollPane(problemsList);
        problemsListScroll.setPreferredSize(new Dimension(350, 600));
        problemsListScroll.setMinimumSize(new Dimension(200, 100));

        JScrollPane detailsScroll = new JScrollPane(problemDetails);
        detailsScroll.setMinimumSize(new Dimension(300, 100));

        // Put them into a JSplitPane for a dynamic ratio
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, problemsListScroll, detailsScroll);
        split.setOneTouchExpandable(true);
        split.setResizeWeight(initialPercent / 100.0);
        split.setDividerLocation(initialPercent / 100.0);
        split.setContinuousLayout(true);

        // Buttons and listeners for details pane
        JButton solveButton = new JButton("Mark as Solved");
        solveButton.addActionListener(e -> {
            Problem selected = problemsList.getSelectedValue();
            if (selected != null) {
                markProblemAsSolved(selected.getId()); // ensure Problem.id is set by controller
                updateDashboardStats();
            }
        });

        // Put solve button under details by using a small wrapper panel to preserve layout
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.add(detailsScroll, BorderLayout.CENTER);
        rightWrapper.add(solveButton, BorderLayout.SOUTH);

        // Replace split's right component with rightWrapper to include the button
        split.setRightComponent(rightWrapper);

        // List selection listener
        problemsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Problem selected = problemsList.getSelectedValue();
                if (selected != null) {
                    showProblemDetails(selected, problemDetails);
                } else {
                    problemDetails.setText("");
                    problemDetails.setBorder(BorderFactory.createTitledBorder("Select a problem"));
                }
            }
        });

        problemsPanel.add(split, BorderLayout.CENTER);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(problemsPanel, BorderLayout.CENTER);

        // Initialize filtering (loads problems and wires combo boxes)
        initMyFiltering();

        // Wire slider & spinner to update split live and persist preference
        ChangeListener sliderSpinnerListener = ev -> {
            int v = dividerSlider.getValue();
            // keep spinner and slider in sync
            if ((int) dividerSpinner.getValue() != v) dividerSpinner.setValue(v);
            split.setDividerLocation(v / 100.0);
        };
        dividerSlider.addChangeListener(sliderSpinnerListener);

        dividerSpinner.addChangeListener(ev -> {
            int v = (Integer) dividerSpinner.getValue();
            if (dividerSlider.getValue() != v) dividerSlider.setValue(v);
            split.setDividerLocation(v / 100.0);
        });

        // persist after slider finished adjusting
        dividerSlider.addChangeListener(e -> {
            if (!dividerSlider.getValueIsAdjusting()) {
                prefs.putInt(PREF_DIVIDER, dividerSlider.getValue());
            }
        });

        // Ensure divider sits correctly after initial layout
        SwingUtilities.invokeLater(() -> split.setDividerLocation(initialPercent / 100.0));

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

    private void initMyFiltering() {
        // Load all problems from controller into allProblems
        try {
            allProblems = problemController.getAllProblems();
        } catch (Exception e) {
            allProblems = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Error loading problems: " + e.getMessage());
        }

        // Populate initial model with all problems
        applyFilters();

        // Wire combo boxes to filter action
        difficultyFilter.addActionListener(e -> applyFilters());
        patternFilter.addActionListener(e -> applyFilters());
    }

    private void applyFilters() {
        String diffSel = (String) difficultyFilter.getSelectedItem();
        String patternSel = (String) patternFilter.getSelectedItem();

        problemListModel.clear();

        for (Problem p : allProblems) {
            boolean matchDiff = "All".equalsIgnoreCase(diffSel) ||
                p.getDifficulty() != null && p.getDifficulty().name().equalsIgnoreCase(diffSel);

            boolean matchPattern = "All".equalsIgnoreCase(patternSel) ||
                p.getPattern() != null && p.getPattern().name().equalsIgnoreCase(patternSel);

            if (matchDiff && matchPattern) {
                problemListModel.addElement(p);
            }
        }

        // Auto-select the first item and show details
        if (!problemListModel.isEmpty()) {
            problemsList.setSelectedIndex(0);
        } else {
            problemDetails.setText("");
            problemDetails.setBorder(BorderFactory.createTitledBorder("Select a problem"));
        }
    }

    private void loadProblemsToAll() {
        try {
            allProblems = problemController.getAllProblems();
        } catch (Exception e) {
            allProblems = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Error loading problems: " + e.getMessage());
        }
        applyFilters();
    }

    private void showProblemDetails(Problem problem, JTextArea detailsArea) {
        if (problem == null) {
            detailsArea.setText("");
            detailsArea.setBorder(BorderFactory.createTitledBorder("Select a problem"));
            return;
        }
        detailsArea.setBorder(BorderFactory.createTitledBorder("Title: " + problem.getTitle()));

        StringBuilder sb = new StringBuilder();
        sb.append("\nDifficulty: ").append(problem.getDifficulty()).append("\n");
        sb.append("Pattern: ").append(problem.getPattern()).append("\n\n");
        sb.append("Description:\n").append(problem.getDescription() == null ? "" : problem.getDescription());

        detailsArea.setText(sb.toString());
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
