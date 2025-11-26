package com.codenest.ui;

import com.codenest.controller.CommunityController;
import com.codenest.model.Community;
import com.codenest.model.Post;
import com.codenest.model.User;
import com.codenest.service.CommunityService;
import com.codenest.util.SessionManager;
import com.codenest.ui.dialog.CommunityDetailsDialog;
import com.codenest.ui.dialog.CreateCommunityDialog;
import com.codenest.ui.dialog.CreatePostDialog;
import com.codenest.ui.dialog.ViewPostDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CommunityPanel extends JPanel {
    // === Fields ===
    private JList<Community> communityList;
    private DefaultListModel<Community> communityListModel;
    private JPanel postsPanel;
    private JScrollPane postsScrollPane;
    private JButton createCommunityBtn;
    private JButton joinCommunityBtn;
    private JButton leaveCommunityBtn;
    private JButton refreshBtn;
    private JButton createPostBtn;
    private JLabel communityTitleLabel;

    private CommunityController communityController;
    private Community selectedCommunity;
    private CommunityService communityService;
    private User currentUser;

 public CommunityPanel(User user) {
    this.currentUser = user;

    SessionManager.getInstance().login(user);

    this.communityService = new CommunityService();
    this.communityController = new CommunityController();
    initializeComponents();
    setupLayout();
    setupEventHandlers();
    loadCommunities();
}


    
    // === Initialization ===
    private void initializeComponents() {
        communityListModel = new DefaultListModel<>();
        communityList = new JList<>(communityListModel);
        communityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        communityList.setCellRenderer(new CommunityListRenderer());

        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        postsScrollPane = new JScrollPane(postsPanel);
        postsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        createCommunityBtn = new JButton("Create Community");
        joinCommunityBtn = new JButton("Join");
        leaveCommunityBtn = new JButton("Leave");
        refreshBtn = new JButton("Refresh");
        createPostBtn = new JButton("Create Post");

        communityTitleLabel = new JLabel("Select a community to view posts");
        communityTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        joinCommunityBtn.setEnabled(false);
        leaveCommunityBtn.setEnabled(false);
        createPostBtn.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left panel - communities
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setPreferredSize(new Dimension(300, 0));

        JLabel leftTitle = new JLabel("Communities");
        leftTitle.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        JScrollPane communityScrollPane = new JScrollPane(communityList);
        leftPanel.add(communityScrollPane, BorderLayout.CENTER);

        JPanel leftButtonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        leftButtonPanel.add(createCommunityBtn);
        leftButtonPanel.add(joinCommunityBtn);
        leftButtonPanel.add(leaveCommunityBtn);
        leftButtonPanel.add(refreshBtn);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

        // Right panel - posts
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(52, 152, 219));
        topBar.setBorder(new EmptyBorder(10, 15, 10, 15));

        communityTitleLabel.setForeground(Color.WHITE);
        topBar.add(communityTitleLabel, BorderLayout.WEST);

        createPostBtn.setBackground(new Color(46, 204, 113));
        createPostBtn.setForeground(Color.WHITE);
        createPostBtn.setFocusPainted(false);
        topBar.add(createPostBtn, BorderLayout.EAST);

        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(postsScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        communityList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onCommunitySelected();
        });

        createCommunityBtn.addActionListener(e -> createCommunity());
        joinCommunityBtn.addActionListener(e -> joinCommunity());
        leaveCommunityBtn.addActionListener(e -> leaveCommunity());
        refreshBtn.addActionListener(e -> refreshCommunities());
        createPostBtn.addActionListener(e -> createPost());

        communityList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) viewCommunityDetails();
            }
        });
    }

    // === Load & Update ===
    private void loadCommunities() {
        communityListModel.clear();
        List<Community> communities = communityController.getAllCommunities();
        for (Community community : communities) {
            communityListModel.addElement(community);
        }
    }

    private void onCommunitySelected() {
        selectedCommunity = communityList.getSelectedValue();
        if (selectedCommunity != null) {
            communityTitleLabel.setText(selectedCommunity.getName());
            loadPosts(selectedCommunity.getId().intValue());
            updateButtonStates();
        }
    }

    private void updateButtonStates() {
        if (selectedCommunity != null) {
            int userId = SessionManager.getInstance().getCurrentUserId();
            boolean isMember = communityController.isUserMember(selectedCommunity.getId().intValue(), userId);

            joinCommunityBtn.setEnabled(!isMember);
            leaveCommunityBtn.setEnabled(isMember);
            createPostBtn.setEnabled(isMember);
        } else {
            joinCommunityBtn.setEnabled(false);
            leaveCommunityBtn.setEnabled(false);
            createPostBtn.setEnabled(false);
        }
    }

    private void loadPosts(int communityId) {
        postsPanel.removeAll();
        List<Post> posts = communityController.getCommunityPosts(communityId);

        if (posts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts yet. Be the first to post!");
            noPostsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noPostsLabel.setForeground(Color.GRAY);
            noPostsLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
            postsPanel.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                postsPanel.add(createPostCard(post));
                postsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    private JPanel createPostCard(Post post) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel authorLabel = new JLabel(post.getAuthorName());
        authorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        authorLabel.setForeground(new Color(52, 73, 94));

        JLabel dateLabel = new JLabel(formatDate(post.getCreatedAt()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        dateLabel.setForeground(Color.GRAY);

        headerPanel.add(authorLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        // Title & content
        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(41, 128, 185));

        String contentPreview = post.getContent();
        if (contentPreview.length() > 200) contentPreview = contentPreview.substring(0, 200) + "...";

        JTextArea contentArea = new JTextArea(contentPreview);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 12));
        contentArea.setForeground(new Color(52, 73, 94));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(null);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);

        JButton viewBtn = new JButton("View Full Post");
        viewBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        viewBtn.setForeground(new Color(41, 128, 185));
        viewBtn.setBorderPainted(false);
        viewBtn.setContentAreaFilled(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> viewFullPost(post));

        actionPanel.add(viewBtn);

        if (SessionManager.getInstance().isAdmin() ||
            post.getAuthorId() == SessionManager.getInstance().getCurrentUserId()) {
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Arial", Font.PLAIN, 11));
            deleteBtn.setForeground(new Color(231, 76, 60));
            deleteBtn.setBorderPainted(false);
            deleteBtn.setContentAreaFilled(false);
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteBtn.addActionListener(e -> deletePost(post));
            actionPanel.add(deleteBtn);
        }

        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(contentArea, BorderLayout.CENTER);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.SOUTH);

        return card;
    }

    private String formatDate(java.time.LocalDateTime date) {
        if (date == null) return "";
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return date.format(formatter);
    }

    // === Action Methods ===
    private void createCommunity() {
        CreateCommunityDialog dialog = new CreateCommunityDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        if (dialog.isSuccess()) {
            loadCommunities();
            JOptionPane.showMessageDialog(this, "Community created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void joinCommunity() {
        if (selectedCommunity == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to join " + selectedCommunity.getName() + "?", "Join Community", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (communityController.joinCommunity(selectedCommunity.getId().intValue())) {
                JOptionPane.showMessageDialog(this, "Successfully joined the community!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to join community. You may already be a member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void leaveCommunity() {
        if (selectedCommunity == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to leave " + selectedCommunity.getName() + "?", "Leave Community", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (communityController.leaveCommunity(selectedCommunity.getId().intValue())) {
                JOptionPane.showMessageDialog(this, "You have left the community.", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to leave community.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshCommunities() {
        loadCommunities();
        if (selectedCommunity != null) loadPosts(selectedCommunity.getId().intValue());
        JOptionPane.showMessageDialog(this, "Communities refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createPost() {
        if (selectedCommunity == null) return;
        CreatePostDialog dialog = new CreatePostDialog((Frame) SwingUtilities.getWindowAncestor(this), selectedCommunity.getId().intValue());
        dialog.setVisible(true);
        if (dialog.isSuccess()) loadPosts(selectedCommunity.getId().intValue());
    }

    private void viewCommunityDetails() {
        if (selectedCommunity == null) return;
        CommunityDetailsDialog dialog = new CommunityDetailsDialog(selectedCommunity);
        dialog.setVisible(true);
    }

    private void viewFullPost(Post post) {
        ViewPostDialog dialog = new ViewPostDialog((Frame) SwingUtilities.getWindowAncestor(this), post);
        dialog.setVisible(true);
    }

   private void deletePost(Post post) {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete this post?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {
        // Convert Long to int only if your controller still expects int
        communityController.deletePost(post.getId().intValue());
        loadPosts(selectedCommunity.getId().intValue());
    }
}


    // === Renderer ===
    private class CommunityListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Community) {
                Community community = (Community) value;
                String html = "<html><b>" + community.getName() + "</b><br>" +
                        "<font size='2' color='gray'>" +
                        (community.getDescription() != null && !community.getDescription().isEmpty()
                                ? community.getDescription().substring(0, Math.min(50, community.getDescription().length()))
                                : "No description") +
                        "</font></html>";
                label.setText(html);
                label.setBorder(new EmptyBorder(8, 10, 8, 10));
            }

            return label;
        }
    }
}
