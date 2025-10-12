package com.codenest.dao;

import com.codenest.model.Problem;
import com.codenest.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemDAO {
    
    public List<Problem> findAll() throws SQLException {
        List<Problem> problems = new ArrayList<>();
        String sql = "SELECT * FROM problems ORDER BY difficulty, pattern";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        }
        return problems;
    }
    
    public List<Problem> findByDifficulty(Problem.Difficulty difficulty) throws SQLException {
        List<Problem> problems = new ArrayList<>();
        String sql = "SELECT * FROM problems WHERE difficulty = ? ORDER BY pattern";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, difficulty.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        }
        return problems;
    }
    
    public List<Problem> findByPattern(Problem.Pattern pattern) throws SQLException {
        List<Problem> problems = new ArrayList<>();
        String sql = "SELECT * FROM problems WHERE pattern = ? ORDER BY difficulty";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pattern.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        }
        return problems;
    }
    
    public boolean save(Problem problem) throws SQLException {
        String sql = "INSERT INTO problems (title, description, difficulty, pattern) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, problem.getTitle());
            stmt.setString(2, problem.getDescription());
            stmt.setString(3, problem.getDifficulty().toString());
            stmt.setString(4, problem.getPattern().toString());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    problem.setId(keys.getLong(1));
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean markAsSolved(Long studentId, Long problemId) throws SQLException {
        String sql = """
            INSERT INTO problem_progress (student_id, problem_id, status, solved_at) 
            VALUES (?, ?, 'SOLVED', CURRENT_TIMESTAMP) 
            ON DUPLICATE KEY UPDATE status = 'SOLVED', solved_at = CURRENT_TIMESTAMP
        """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, studentId);
            stmt.setLong(2, problemId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public int getSolvedCount(Long studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM problem_progress WHERE student_id = ? AND status = 'SOLVED'";
        
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
    
    private Problem mapResultSetToProblem(ResultSet rs) throws SQLException {
        Problem problem = new Problem();
        problem.setId(rs.getLong("id"));
        problem.setTitle(rs.getString("title"));
        problem.setDescription(rs.getString("description"));
        problem.setDifficulty(Problem.Difficulty.valueOf(rs.getString("difficulty")));
        problem.setPattern(Problem.Pattern.valueOf(rs.getString("pattern")));
        return problem;
    }
}
