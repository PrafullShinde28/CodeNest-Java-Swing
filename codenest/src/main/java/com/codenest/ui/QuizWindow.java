package com.codenest.ui;

import com.codenest.controller.QuizController;
import com.codenest.model.User;
import com.codenest.model.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuizWindow extends JFrame {
    private User currentUser;
    private Long quizId;
    private StudentDashboard parentDashboard;
    private QuizController quizController;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Timer quizTimer;
    private int timeRemaining;
    private JLabel timerLabel;
    private JLabel questionLabel;
    private ButtonGroup optionGroup;
    private JRadioButton[] optionButtons;
    private JButton nextButton;
    private JButton submitButton;

    // NEW: keep users' answers (1..4) or -1 if none
    private int[] userAnswers;

    public QuizWindow(User user, Long quizId, StudentDashboard parent) {
        this.currentUser = user;
        this.quizId = quizId;
        this.parentDashboard = parent;
        this.quizController = new QuizController();
        this.timeRemaining = 1800; // 30 minutes in seconds

        loadQuizQuestions();
        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for this quiz.");
            dispose();
            return;
        }

        userAnswers = new int[questions.size()];
        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;

        initializeComponents();
        startTimer();
    }

    private void loadQuizQuestions() {
        try {
            questions = quizController.getQuizQuestions(quizId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading quiz: " + e.getMessage());
            questions = null;
        }
    }

    private void initializeComponents() {
        setTitle("CodeNest - Quiz");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with timer
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Quiz in Progress", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        timerLabel = new JLabel("Time: 30:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(timerLabel, BorderLayout.EAST);

        // Question panel
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createTitledBorder("Question"));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        questionPanel.add(questionLabel, BorderLayout.NORTH);

        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 12));
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        questionPanel.add(optionsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        nextButton = new JButton("Next Question");
        submitButton = new JButton("Submit Quiz");

        nextButton.addActionListener(new NextQuestionListener());
        submitButton.addActionListener(new SubmitQuizListener());

        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load first question
        loadCurrentQuestion();
    }

    private void startTimer() {
        quizTimer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimerDisplay();

            if (timeRemaining <= 0) {
                quizTimer.stop();
                // auto-submit when time runs out
                autoSubmitDueToTime();
            }
        });
        quizTimer.start();
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));

        if (timeRemaining <= 300) { // 5 minutes warning
            timerLabel.setForeground(Color.RED);
        }
    }

    private void loadCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);

            questionLabel.setText("<html><body style='width: 700px'>" +
                    (currentQuestionIndex + 1) + ". " + question.getQuestionText() +
                    "</body></html>");

            optionButtons[0].setText("A) " + question.getOption1());
            optionButtons[1].setText("B) " + question.getOption2());
            optionButtons[2].setText("C) " + question.getOption3());
            optionButtons[3].setText("D) " + question.getOption4());

            // restore any previously selected answer for this question
            int prev = userAnswers[currentQuestionIndex];
            optionGroup.clearSelection();
            if (prev >= 1 && prev <= 4) {
                optionButtons[prev - 1].setSelected(true);
            }

            // Update button visibility
            nextButton.setVisible(currentQuestionIndex < questions.size() - 1);
            submitButton.setVisible(currentQuestionIndex == questions.size() - 1);
        }
    }

    private class NextQuestionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            storeCurrentSelection();
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                loadCurrentQuestion();
            }
        }
    }

    private class SubmitQuizListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            storeCurrentSelection();
            submitQuiz();
        }
    }

    // NEW: store the user's selection for the current question
    private void storeCurrentSelection() {
        int selected = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selected = i + 1; // 1..4
                break;
            }
        }
        userAnswers[currentQuestionIndex] = selected;
    }

    private void submitQuiz() {
        if (quizTimer != null && quizTimer.isRunning()) quizTimer.stop();

        int score = 0;
        StringBuilder feedback = new StringBuilder();
        feedback.append("Quiz Feedback\n\n");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int correct = q.getCorrectAnswer(); // 1..4
            int chosen = userAnswers[i];        // -1 or 1..4
            if (chosen == correct) {
                score++;
            } else {
                feedback.append(String.format("Q%d: %s\n", i + 1, q.getQuestionText()));
                String chosenText = (chosen >= 1 && chosen <= 4) ? getOptionText(q, chosen) : "No answer";
                String correctText = getOptionText(q, correct);
                feedback.append("  Your answer: ").append(chosenText).append("\n");
                feedback.append("  Correct   : ").append(correctText).append("\n\n");
            }
        }

        // Save the result
        try {
            quizController.saveQuizResult(currentUser.getId(), quizId, score);
        } catch (Exception ex) {
            // log but continue to show feedback
            System.err.println("Error saving quiz result: " + ex.getMessage());
        }

        // Show results + detailed feedback in a scrollable text area
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.append(String.format("Quiz completed!\nYour Score: %d/%d\n\n", score, questions.size()));
        if (feedback.toString().contains("Your answer")) {
            area.append(feedback.toString());
        } else {
            area.append("All answers correct — great job!");
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this, scroll, "Quiz Results & Feedback", JOptionPane.INFORMATION_MESSAGE);

        // Update dashboard and close
        try {
            parentDashboard.updateDashboardStats();
        } catch (Exception ignored) {}
        dispose();
    }

    // Called when time runs out
    private void autoSubmitDueToTime() {
        storeCurrentSelection();
        JOptionPane.showMessageDialog(this, "Time's up! Submitting quiz now.", "Time Up", JOptionPane.WARNING_MESSAGE);
        submitQuiz();
    }

    private String getOptionText(Question q, int optionNumber) {
        switch (optionNumber) {
            case 1: return "A) " + q.getOption1();
            case 2: return "B) " + q.getOption2();
            case 3: return "C) " + q.getOption3();
            case 4: return "D) " + q.getOption4();
            default: return "No answer";
        }
    }
}
