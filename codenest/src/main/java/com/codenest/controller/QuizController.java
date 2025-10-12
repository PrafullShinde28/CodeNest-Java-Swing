package com.codenest.controller;

import com.codenest.service.QuizService;
import com.codenest.model.Quiz;
import com.codenest.model.Question;
import java.sql.SQLException;
import java.util.List;

public class QuizController {
    private QuizService quizService;
    
    public QuizController() {
        this.quizService = new QuizService();
    }
    
    public List<Quiz> getAllQuizzes() throws SQLException {
        return quizService.getAllQuizzes();
    }
    
    public List<Question> getQuizQuestions(Long quizId) throws SQLException {
        return quizService.getQuizQuestions(quizId);
    }
    
    public boolean saveQuizResult(Long studentId, Long quizId, int score) throws SQLException {
        return quizService.saveQuizResult(studentId, quizId, score);
    }
    
    public int getTotalQuizScore(Long studentId) throws SQLException {
        return quizService.getTotalQuizScore(studentId);
    }
}