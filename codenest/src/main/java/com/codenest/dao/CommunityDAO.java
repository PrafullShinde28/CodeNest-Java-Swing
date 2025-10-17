package com.codenest.dao;

import com.codenest.model.Community;
import com.codenest.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommunityDAO {

    // ===== Fetch all communities =====
    public List<Community> findAll() {
        List<Community> communities = new ArrayList<>();
        String sql = "SELECT * FROM communities ORDER BY created_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Community community = new Community(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                communities.add(community);
            }
        } catch (SQLException e) {
            System.err.println("❌ [findAll] DB error: " + e.getMessage());
            e.printStackTrace();
        }

        return communities;
    }

    // ===== Find a community by ID =====
    public Community findById(Long id) {
        String sql = "SELECT * FROM communities WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Community(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [findById] DB error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ===== Create a new community =====
    public boolean save(Community community) {
        String sql = "INSERT INTO communities (name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("🟢 [save] Creating community: " + community.getName());
            ps.setString(1, community.getName());
            ps.setString(2, community.getDescription());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    System.out.println("✅ [save] Created community with ID: " + keys.getLong(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ [save] Failed to create community: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ===== Add a member to community =====
    public boolean addMember(long communityId, long userId) {
        String sql = "INSERT INTO community_members (community_id, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("🟢 [addMember] Joining user=" + userId + " to community=" + communityId);
            System.out.println("Connected to DB: " + conn.getCatalog());

            ps.setLong(1, communityId);
            ps.setLong(2, userId);

            int rows = ps.executeUpdate();
            System.out.println("✅ [addMember] Rows affected: " + rows);
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ [addMember] SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===== Remove member from community =====
    public boolean removeMember(Long communityId, int userId) {
        String sql = "DELETE FROM community_members WHERE community_id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, communityId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ [removeMember] Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===== Check membership =====
    public boolean isMember(Long communityId, int userId) {
        String sql = "SELECT 1 FROM community_members WHERE community_id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, communityId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("❌ [isMember] Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===== Find all communities joined by a user =====
    public List<Community> findByUserId(int userId) {
        List<Community> communities = new ArrayList<>();
        String sql = """
            SELECT c.id, c.name, c.description, c.created_at
            FROM communities c
            JOIN community_members m ON c.id = m.community_id
            WHERE m.user_id = ?
            ORDER BY c.created_at DESC
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Community community = new Community(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    communities.add(community);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [findByUserId] Error: " + e.getMessage());
            e.printStackTrace();
        }

        return communities;
    }

    // ===== Count members in community =====
    public int getMemberCount(Long communityId) {
        String sql = "SELECT COUNT(*) FROM community_members WHERE community_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, communityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("❌ [getMemberCount] Error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // ====== MAIN TEST HARNESS ======
    public static void main(String[] args) {
        DatabaseUtil.initializeDatabase(); // ensures DB ready
        CommunityDAO dao = new CommunityDAO();

        // 1️⃣ Test create community
        Community testCommunity = new Community(null, "TestCommunity", "Testing DAO operations", null);
        boolean created = dao.save(testCommunity);
        System.out.println("Create test community => " + created);

        // 2️⃣ Test join
        boolean joined = dao.addMember(1L, 1L);
        System.out.println("Join community 1 with user 1 => " + joined);

        // 3️⃣ Test fetch
        List<Community> list = dao.findAll();
        System.out.println("Fetched communities count => " + list.size());

        // 4️⃣ Test member count
        int count = dao.getMemberCount(1L);
        System.out.println("Member count for community 1 => " + count);
    }
}
