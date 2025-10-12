// ===== SERVICE LAYER =====
package com.codenest.service;

import com.codenest.dao.UserDAO;
import com.codenest.model.User;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public User authenticate(String email, String password) throws SQLException {
        return userDAO.findByEmailAndPassword(email, password);
    }
    
    public boolean registerUser(User user) throws SQLException {
        // Check if email already exists
        if (userDAO.emailExists(user.getEmail())) {
            return false;
        }
        
        return userDAO.save(user);
    }
    
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }
}