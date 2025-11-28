package com.codenest.ui;

import com.codenest.dao.*;
import com.codenest.model.*;
import com.codenest.util.DatabaseUtil;
import com.codenest.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private final ProblemDAO problemDAO = new ProblemDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final CommunityDAO communityDAO = new CommunityDAO();
    private final PostDAO postDAO = new PostDAO();

    private User currentUser;

    public AdminDashboard(User user) {
        // ensure session is set so controllers/services can read current user
        if (user != null) {
            SessionManager.getInstance().login(user);
        }
        this.currentUser = user;

        setTitle("CodeNest - Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // top bar with logout on right
        addTopRightLogoutButton();

        // tabs with admin pages
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Dashboard", buildStatsPanel());
        tabs.add("Manage Users", buildUsersPanel());
        tabs.add("Manage Problems", buildProblemsPanel());
        tabs.add("Manage Quizzes", buildQuizzesPanel());
        tabs.add("Manage Communities", buildCommunitiesPanel());
        tabs.add("Manage Posts", buildPostsPanel());

        add(tabs, BorderLayout.CENTER);
    }

    // Top bar (title left, logout button right)
    private void addTopRightLogoutButton() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        topBar.setBackground(null);

        JLabel title = new JLabel("CodeNest - Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    AdminDashboard.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                SessionManager.getInstance().logout();

                SwingUtilities.invokeLater(() -> {
                    AdminDashboard.this.dispose();
                    LoginWindow login = new LoginWindow();
                    login.setVisible(true);
                    login.toFront();
                    login.requestFocus();
                });
            }
        });

        topBar.add(title, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
    }

    // ===========================
    // 1️⃣ ADMIN DASHBOARD STATS
    // ===========================
    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel welcome = new JLabel("Admin Dashboard - Welcome, " + (currentUser != null ? currentUser.getName() : "Admin"));
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

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshUsers(model));

        JButton delete = new JButton("Delete User");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            deleteUser(id);
            refreshUsers(model);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(refreshBtn);
        bottom.add(delete);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
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

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshProblems(model));

        JButton delete = new JButton("Delete Problem");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            deleteProblem(id);
            refreshProblems(model);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(refreshBtn);
        bottom.add(delete);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
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

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshQuizzes(model));
        top.add(refreshBtn);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
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

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshCommunities(model));

        JButton delete = new JButton("Delete Community");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            deleteCommunity(id);
            refreshCommunities(model);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(refreshBtn);
        bottom.add(delete);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
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

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshPosts(model));

        JButton delete = new JButton("Delete Post");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            postDAO.delete(id);
            refreshPosts(model);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(refreshBtn);
        bottom.add(delete);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
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
