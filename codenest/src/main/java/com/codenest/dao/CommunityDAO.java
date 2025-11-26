package com.codenest.dao;

import com.codenest.model.Community;
import com.codenest.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommunityDAO {

    // ===== Get all communities =====
    public List<Community> findAll() {
        List<Community> communities = new ArrayList<>();
        String sql = "SELECT * FROM communities";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                communities.add(new Community(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return communities;
    }

    // ===== Find community by ID =====
    public Community findById(Long id) {
        String sql = "SELECT * FROM communities WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Community(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ===== Save new community =====
    public boolean save(Community community) {
        String sql = "INSERT INTO communities (name, description, created_at) VALUES (?, ?, NOW())";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, community.getName());
            ps.setString(2, community.getDescription());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    community.setId(rs.getLong(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

   public boolean addMember(Long communityId, Long userId) {
    String sql = "INSERT INTO community_members (community_id, user_id) VALUES (?, ?)";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setLong(1, communityId);
        ps.setLong(2, userId);

        int rows = ps.executeUpdate();
        System.out.println("Rows inserted: " + rows);  // Debug
        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    // ===== Remove member from a community =====
    public boolean removeMember(Long communityId, Long userId) {
        String sql = "DELETE FROM community_members WHERE community_id = ? AND user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, communityId);
            ps.setLong(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== Check if a user is a member of a community =====
    public boolean isMember(Long communityId, Long userId) {
        String sql = "SELECT COUNT(*) FROM community_members WHERE community_id = ? AND user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, communityId);
            ps.setLong(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== Find all communities a user has joined =====
    public List<Community> findByUserId(Long userId) {
        List<Community> communities = new ArrayList<>();
        String sql = """
            SELECT c.* FROM communities c
            JOIN community_members m ON c.id = m.community_id
            WHERE m.user_id = ?
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                communities.add(new Community(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return communities;
    }

    // ===== Get number of members in a community =====
    public int getMemberCount(Long communityId) {
        String sql = "SELECT COUNT(*) FROM community_members WHERE community_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, communityId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
