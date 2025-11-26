package com.codenest.ui;

import com.codenest.dao.*;
import com.codenest.model.*;
import com.codenest.util.DatabaseUtil;
import com.codenest.controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private final ProblemDAO problemDAO = new ProblemDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final CommunityDAO communityDAO = new CommunityDAO();
    private final PostDAO postDAO = new PostDAO();

    private User currentUser;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("CodeNest - Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Dashboard", buildStatsPanel());
        tabs.add("Manage Users", buildUsersPanel());
        tabs.add("Manage Problems", buildProblemsPanel());
        tabs.add("Manage Quizzes", buildQuizzesPanel());
        tabs.add("Manage Communities", buildCommunitiesPanel());
        tabs.add("Manage Posts", buildPostsPanel());

        add(tabs);
    }

    // ===========================
    // 1️⃣ ADMIN DASHBOARD STATS
    // ===========================
    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel welcome = new JLabel("Admin Dashboard - Welcome, " + currentUser.getName());
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcome, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(2,2,20,20));
        int users = 0, problems = 0, quizzes = 0;

        try {
            users = userDAO.findAll().size();
            problems = problemDAO.findAll().size();
            quizzes = quizDAO.findAll().size();
        } catch (Exception e) { e.printStackTrace(); }

        stats.add(new JLabel("Total Users: " + users));
        stats.add(new JLabel("Total Problems: " + problems));
        stats.add(new JLabel("Total Quizzes: " + quizzes));
        stats.add(new JLabel("Active Users (Today): feature coming soon"));

        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }

    // ===========================
    // 2️⃣ MANAGE USERS
    // ===========================
    private JPanel buildUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] col = {"ID", "Name", "Email", "Role", "Joined"};
        DefaultTableModel model = new DefaultTableModel(col, 0);

        JTable table = new JTable(model);
        refreshUsers(model);

        JButton delete = new JButton("Delete User");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            deleteUser(id);
            refreshUsers(model);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(delete, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshUsers(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (User u : userDAO.findAll()) {
                model.addRow(new Object[]{
                        u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getCreatedAt()
                });
            }
        } catch (Exception ignored) {}
    }

    private void deleteUser(long id) {
        try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id=?")){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ===========================
    // 3️⃣ MANAGE PROBLEMS
    // ===========================
    private JPanel buildProblemsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] col = {"ID", "Title", "Difficulty", "Pattern"};
        DefaultTableModel model = new DefaultTableModel(col, 0);

        JTable table = new JTable(model);
        refreshProblems(model);

        JButton delete = new JButton("Delete Problem");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            deleteProblem(id);
            refreshProblems(model);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(delete, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshProblems(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Problem p : problemDAO.findAll()) {
                model.addRow(new Object[]{
                        p.getId(), p.getTitle(), p.getDifficulty(), p.getPattern()
                });
            }
        } catch (Exception ignored) {}
    }

    private void deleteProblem(long id) {
        try (var conn = DatabaseUtil.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM problems WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ===========================
    // 4️⃣ MANAGE QUIZZES
    // ===========================
    private JPanel buildQuizzesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] col = {"ID", "Title", "Subject", "Time Limit"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        JTable table = new JTable(model);
        refreshQuizzes(model);

        panel.add(new JScrollPane(table));
        return panel;
    }

    private void refreshQuizzes(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Quiz q : quizDAO.findAll()) {
                model.addRow(new Object[]{
                        q.getId(), q.getTitle(), q.getSubject(), q.getTimeLimit()
                });
            }
        } catch (Exception ignored) {}
    }

    // ===========================
    // 5️⃣ MANAGE COMMUNITIES
    // ===========================
    private JPanel buildCommunitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] col = {"ID", "Name", "Description", "Created"};
        DefaultTableModel model = new DefaultTableModel(col, 0);

        JTable table = new JTable(model);
        refreshCommunities(model);

        JButton delete = new JButton("Delete Community");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            deleteCommunity(id);
            refreshCommunities(model);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(delete, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshCommunities(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Community c : communityDAO.findAll()) {
                model.addRow(new Object[]{
                        c.getId(), c.getName(), c.getDescription(), c.getCreatedAt()
                });
            }
        } catch (Exception ignored) {}
    }

    private void deleteCommunity(long id) {
        try (var conn = DatabaseUtil.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM communities WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ===========================
    // 6️⃣ MANAGE POSTS
    // ===========================
    private JPanel buildPostsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] col = {"ID", "Community", "Author", "Title", "Created"};
        DefaultTableModel model = new DefaultTableModel(col, 0);

        JTable table = new JTable(model);
        refreshPosts(model);

        JButton delete = new JButton("Delete Post");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            postDAO.delete(id);
            refreshPosts(model);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(delete, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshPosts(DefaultTableModel model) {
        model.setRowCount(0);

        try {
            for (Community c : communityDAO.findAll()) {
                for (Post p : postDAO.findByCommunityId(c.getId())) {
                    model.addRow(new Object[]{
                            p.getId(), c.getName(), p.getAuthorName(), p.getTitle(), p.getCreatedAt()
                    });
                }
            }
        } catch (Exception ignored) {}
    }
}
