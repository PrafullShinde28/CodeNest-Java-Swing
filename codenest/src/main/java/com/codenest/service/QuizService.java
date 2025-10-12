package com.codenest.service;

import com.codenest.dao.QuizDAO;
import com.codenest.model.Quiz;
import com.codenest.model.Question;
import java.sql.SQLException;
import java.util.List;

public class QuizService {
    private QuizDAO quizDAO;
    
    public QuizService() {
        this.quizDAO = new QuizDAO();
    }
    
    public List<Quiz> getAllQuizzes() throws SQLException {
        return quizDAO.findAll();
    }
    
    public List<Question> getQuizQuestions(Long quizId) throws SQLException {
        return quizDAO.getQuizQuestions(quizId);
    }
    
    public boolean saveQuizResult(Long studentId, Long quizId, int score) throws SQLException {
        return quizDAO.saveQuizAttempt(studentId, quizId, score);
    }
    
    public int getTotalQuizScore(Long studentId) throws SQLException {
        return quizDAO.getTotalQuizScore(studentId);
    }
}
