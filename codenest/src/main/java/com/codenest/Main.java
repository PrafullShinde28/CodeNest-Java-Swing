package com.codenest;

import com.codenest.ui.LoginWindow;
import com.codenest.util.DatabaseUtil;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Set Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize database
        DatabaseUtil.initializeDatabase();
        
        // Launch application
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}