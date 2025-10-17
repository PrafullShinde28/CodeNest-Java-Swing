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
    
    public List<Community> getAllCommunities() {
        return communityDAO.findAll();
    }
    
    public Community getCommunityById(int id) {
    return communityDAO.findById(Long.valueOf(id));
    }
    
    public boolean createCommunity(String name, String description) {
        int userId = SessionManager.getInstance().getCurrentUserId();
        if (userId == -1) return false;
        
    Community community = new Community(name, description);
        boolean created = communityDAO.save(community);
        
        // Auto-join the creator to the community
        if (created) {
            communityDAO.addMember(community.getId(), userId);
        }
        
        return created;
    }
    
    public boolean joinCommunity(int communityId) {
        int userId = SessionManager.getInstance().getCurrentUserId();
        if (userId == -1) return false;
        
    return communityDAO.addMember(Long.valueOf(communityId), userId);
    }
    
    public boolean leaveCommunity(int communityId) {
        int userId = SessionManager.getInstance().getCurrentUserId();
        if (userId == -1) return false;
        
    return communityDAO.removeMember(Long.valueOf(communityId), userId);
    }
    
    public boolean createPost(int communityId, String title, String content) {
        int userId = SessionManager.getInstance().getCurrentUserId();
        if (userId == -1) return false;
        
        // Check if user is a member
        if (!isUserMember(communityId, userId)) {
            return false;
        }
        
        Post post = new Post(communityId, userId, title, content);
        return postDAO.save(post);
    }
    
    public List<Post> getCommunityPosts(int communityId) {
        return postDAO.findByCommunityId(communityId);
    }
    
    public boolean deletePost(int postId) {
        return postDAO.delete(postId);
    }
    
    public boolean isUserMember(int communityId, int userId) {
    return communityDAO.isMember(Long.valueOf(communityId), userId);
    }
    
    public List<Community> getUserCommunities(int userId) {
        return communityDAO.findByUserId(userId);
    }
    
    public int getMemberCount(int communityId) {
    return communityDAO.getMemberCount(Long.valueOf(communityId));
    }
    
    public int getPostCount(int communityId) {
        return postDAO.getPostCount(communityId);
    }
}