// ===== CONTROLLER LAYER =====
package com.codenest.controller;

import com.codenest.service.UserService;
import com.codenest.model.User;
import java.sql.SQLException;
import java.util.List;

public class AuthController {
    private UserService userService;
    
    public AuthController() {
        this.userService = new UserService();
    }
    
    public User authenticate(String email, String password) throws SQLException {
        return userService.authenticate(email, password);
    }
    
    public boolean registerUser(User user) throws SQLException {
        return userService.registerUser(user);
    }
    
    public List<User> getAllUsers() throws SQLException {
        return userService.getAllUsers();
    }
}