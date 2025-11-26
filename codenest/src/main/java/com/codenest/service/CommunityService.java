package com.codenest.service;

import com.codenest.dao.CommunityDAO;
import com.codenest.dao.PostDAO;
import com.codenest.model.Community;
import com.codenest.model.Post;
import com.codenest.util.SessionManager;
import java.util.List;

public class CommunityService {
    private final CommunityDAO communityDAO;
    private final PostDAO postDAO;

    public CommunityService() {
        this.communityDAO = new CommunityDAO();
        this.postDAO = new PostDAO();
    }

    // ===== Get all communities =====
    public List<Community> getAllCommunities() {
        return communityDAO.findAll();
    }

    // ===== Get a community by ID =====
    public Community getCommunityById(int id) {
        return communityDAO.findById(Long.valueOf(id));
    }

    // ===== Create a new community =====
    public boolean createCommunity(String name, String description) {
        Long userId = Long.valueOf(SessionManager.getInstance().getCurrentUserId());
        if (userId == -1) return false;

        Community community = new Community(name, description);
        boolean created = communityDAO.save(community);

        // Auto-join the creator to the community
        if (created) {
            communityDAO.addMember(community.getId(), userId);
        }

        return created;
    }

    // ===== Join a community =====
    public boolean joinCommunity(int communityId) {
    Long userId = Long.valueOf(SessionManager.getInstance().getCurrentUserId());
    System.out.println("Current user ID: " + SessionManager.getInstance().getCurrentUserId());

    if (userId == -1) return false;

    return communityDAO.addMember(Long.valueOf(communityId), userId);
}


    // ===== Leave a community =====
    public boolean leaveCommunity(int communityId) {
        Long userId = Long.valueOf(SessionManager.getInstance().getCurrentUserId());
        if (userId == -1) return false;

        return communityDAO.removeMember(Long.valueOf(communityId), userId);
    }

    // ===== Create a post in a community =====
    public boolean createPost(int communityId, String title, String content) {
        Long userId = Long.valueOf(SessionManager.getInstance().getCurrentUserId());
        if (userId == -1) return false;

        // Check if user is a member
        if (!isUserMember(communityId, userId)) {
            return false;
        }

        Post post = new Post(Long.valueOf(communityId), userId, title, content);
        return postDAO.save(post);
    }

    // ===== Get posts for a community =====
    public List<Post> getCommunityPosts(long communityId) {
        return postDAO.findByCommunityId(communityId);
    }

    // ===== Delete a post =====
    public boolean deletePost(long postId) {
        return postDAO.delete(postId);
    }

    // ===== Check if user is a member =====
    public boolean isUserMember(int communityId, Long userId) {
        return communityDAO.isMember(Long.valueOf(communityId), userId);
    }

    public boolean isUserMember(int communityId, int userId) {
        return isUserMember(communityId, Long.valueOf(userId));
    }

    // ===== Get all communities a user has joined =====
    public List<Community> getUserCommunities(int userId) {
        return communityDAO.findByUserId(Long.valueOf(userId));
    }

    // ===== Get member count of a community =====
    public int getMemberCount(int communityId) {
        return communityDAO.getMemberCount(Long.valueOf(communityId));
    }

    // ===== Get post count of a community =====
    public int getPostCount(long communityId) {
        return postDAO.getPostCount(communityId);
    }
}
