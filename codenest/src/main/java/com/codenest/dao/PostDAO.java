package com.codenest.dao;

import com.codenest.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDAO extends BaseDAO {

    // ===== Get all posts for a community =====
    public List<Post> findByCommunityId(Long communityId) {
        String sql = "SELECT p.*, u.name as author_name FROM posts p " +
                     "JOIN users u ON p.user_id = u.id " +
                     "WHERE p.community_id = ? ORDER BY p.created_at DESC";
        List<Post> posts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, communityId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                posts.add(mapResultSetToPost(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding posts by community id: " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeResources(conn, stmt);
        }
        return posts;
    }

    // ===== Save a new post =====
    public boolean save(Post post) {
        String sql = "INSERT INTO posts (community_id, user_id, title, content) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, post.getCommunityId());
            stmt.setLong(2, post.getAuthorId());
            stmt.setString(3, post.getTitle());
            stmt.setString(4, post.getContent());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving post: " + e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
        return false;
    }

    // ===== Delete a post =====
    public boolean delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting post: " + e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
        return false;
    }

    // ===== Get post count for a community =====
    public int getPostCount(Long communityId) {
        String sql = "SELECT COUNT(*) FROM posts WHERE community_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, communityId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting post count: " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeResources(conn, stmt);
        }
        return 0;
    }

    // ===== Map ResultSet to Post object =====
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setCommunityId(rs.getLong("community_id"));
        post.setAuthorId(rs.getLong("user_id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setAuthorName(rs.getString("author_name"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            post.setCreatedAt(timestamp.toLocalDateTime());
        }

        return post;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }

    private String formatDate(LocalDateTime date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return date.format(formatter);
    }
}
