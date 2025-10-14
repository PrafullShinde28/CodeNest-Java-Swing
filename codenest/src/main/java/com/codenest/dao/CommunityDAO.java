package com.codenest.dao;

import com.codenest.model.Community;
import java.util.ArrayList;
import java.util.List;

public class CommunityDAO {
    private List<Community> communities = new ArrayList<>();
    private List<Integer> members = new ArrayList<>();

    public List<Community> findAll() {
        return communities;
    }

    public Community findById(Long id) {
        for (Community c : communities) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public boolean save(Community community) {
        return communities.add(community);
    }

    public boolean addMember(Long communityId, int userId) {
        // Dummy implementation
        return members.add(userId);
    }

    public boolean removeMember(Long communityId, int userId) {
        // Dummy implementation
        return members.remove((Integer)userId);
    }

    public boolean isMember(Long communityId, int userId) {
        // Dummy implementation
        return members.contains(userId);
    }

    public List<Community> findByUserId(int userId) {
        // Dummy implementation
        return communities;
    }

    public int getMemberCount(Long communityId) {
        // Dummy implementation
        return members.size();
    }
}
