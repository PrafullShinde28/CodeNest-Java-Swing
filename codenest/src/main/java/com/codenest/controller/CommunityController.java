
package com.codenest.controller;

import com.codenest.model.Community;
import com.codenest.model.Post;
import com.codenest.service.CommunityService;
import java.util.List;

public class CommunityController {
    private final CommunityService communityService;
    
    public CommunityController() {
        this.communityService = new CommunityService();
    }
    
    public List<Community> getAllCommunities() {
        return communityService.getAllCommunities();
    }
    
    public Community getCommunityById(int id) {
        return communityService.getCommunityById(id);
    }
    
    public boolean createCommunity(String name, String description) {
        return communityService.createCommunity(name, description);
    }
    
    public boolean joinCommunity(int communityId) {
        return communityService.joinCommunity(communityId);
    }
    
    public boolean leaveCommunity(int communityId) {
        return communityService.leaveCommunity(communityId);
    }
    
    public boolean createPost(int communityId, String title, String content) {
        return communityService.createPost(communityId, title, content);
    }
    
    public List<Post> getCommunityPosts(int communityId) {
        return communityService.getCommunityPosts(communityId);
    }
    
    public boolean deletePost(int postId) {
        return communityService.deletePost(postId);
    }
    
    public boolean isUserMember(int communityId, int userId) {
        return communityService.isUserMember(communityId, userId);
    }
    
    public List<Community> getUserCommunities(int userId) {
        return communityService.getUserCommunities(userId);
    }
    
    public int getMemberCount(int communityId) {
        return communityService.getMemberCount(communityId);
    }
    
    public int getPostCount(int communityId) {
        return communityService.getPostCount(communityId);
    }
} 