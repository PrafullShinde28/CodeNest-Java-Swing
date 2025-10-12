// ===== DAO LAYER =====
package com.codenest.dao;

import com.codenest.model.User;
import com.codenest.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    public User findByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }
    
    public boolean save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().toString());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
                return true;
            }
        }
        return false;
    }
    
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }
    
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}