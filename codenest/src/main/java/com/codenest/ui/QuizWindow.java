// ===== QUIZ WINDOW =====
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
    private int score = 0;
    private Timer quizTimer;
    private int timeRemaining;
    private JLabel timerLabel;
    private JLabel questionLabel;
    private ButtonGroup optionGroup;
    private JRadioButton[] optionButtons;
    private JButton nextButton;
    private JButton submitButton;
    
    public QuizWindow(User user, Long quizId, StudentDashboard parent) {
        this.currentUser = user;
        this.quizId = quizId;
        this.parentDashboard = parent;
        this.quizController = new QuizController();
        this.timeRemaining = 1800; // 30 minutes in seconds
        
        loadQuizQuestions();
        initializeComponents();
        startTimer();
    }
    
    private void loadQuizQuestions() {
        try {
            questions = quizController.getQuizQuestions(quizId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading quiz: " + e.getMessage());
            dispose();
        }
    }
    
    private void initializeComponents() {
        setTitle("CodeNest - Quiz");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
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
                submitQuiz();
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
            
            questionLabel.setText("<html><body style='width: 600px'>" + 
                                (currentQuestionIndex + 1) + ". " + question.getQuestionText() + 
                                "</body></html>");
            
            optionButtons[0].setText("A) " + question.getOption1());
            optionButtons[1].setText("B) " + question.getOption2());
            optionButtons[2].setText("C) " + question.getOption3());
            optionButtons[3].setText("D) " + question.getOption4());
            
            // Clear previous selection
            optionGroup.clearSelection();
            
            // Update button visibility
            nextButton.setVisible(currentQuestionIndex < questions.size() - 1);
            submitButton.setVisible(currentQuestionIndex == questions.size() - 1);
        }
    }
    
    private class NextQuestionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkAnswer();
            currentQuestionIndex++;
            loadCurrentQuestion();
        }
    }
    
    private class SubmitQuizListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkAnswer();
            submitQuiz();
        }
    }
    
    private void checkAnswer() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            
            for (int i = 0; i < optionButtons.length; i++) {
                if (optionButtons[i].isSelected()) {
                    if ((i + 1) == question.getCorrectAnswer()) {
                        score++;
                    }
                    break;
                }
            }
        }
    }
    
    private void submitQuiz() {
        quizTimer.stop();
        
        try {
            quizController.saveQuizResult(currentUser.getId(), quizId, score);
            
            JOptionPane.showMessageDialog(this, 
                String.format("Quiz completed!\nYour Score: %d/%d", score, questions.size()),
                "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
            
            parentDashboard.updateDashboardStats();
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving quiz results: " + e.getMessage());
        }
    }
}
