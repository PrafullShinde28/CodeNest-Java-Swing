package com.codenest.dao;

import com.codenest.model.Quiz;
import com.codenest.model.Question;
import com.codenest.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    
    public List<Quiz> findAll() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quizzes ORDER BY subject, title";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                quizzes.add(mapResultSetToQuiz(rs));
            }
        }
        return quizzes;
    }
    
    public List<Question> getQuizQuestions(Long quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE quiz_id = ? ORDER BY id";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        }
        return questions;
    }
    
    public boolean saveQuizAttempt(Long studentId, Long quizId, int score) throws SQLException {
        String sql = "INSERT INTO quiz_attempts (student_id, quiz_id, score) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, studentId);
            stmt.setLong(2, quizId);
            stmt.setInt(3, score);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public int getTotalQuizScore(Long studentId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(score), 0) FROM quiz_attempts WHERE student_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Quiz mapResultSetToQuiz(ResultSet rs) throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setId(rs.getLong("id"));
        quiz.setSubject(rs.getString("subject"));
        quiz.setTitle(rs.getString("title"));
        quiz.setTimeLimit(rs.getInt("time_limit"));
        quiz.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return quiz;
    }
    
    private Question mapResultSetToQuestion(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("id"));
        question.setQuizId(rs.getLong("quiz_id"));
        question.setQuestionText(rs.getString("question_text"));
        question.setOption1(rs.getString("option1"));
        question.setOption2(rs.getString("option2"));
        question.setOption3(rs.getString("option3"));
        question.setOption4(rs.getString("option4"));
        question.setCorrectAnswer(rs.getInt("correct_answer"));
        return question;
    }
}
