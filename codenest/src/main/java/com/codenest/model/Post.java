package com.codenest.model;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private int communityId;   // corresponds to community_id in DB
    private int authorId;      // corresponds to user_id in DB
    private String authorName; // fetched from users table JOIN
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---------------- Constructors ----------------

    public Post() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(int communityId, int authorId, String title, String content) {
        this.communityId = communityId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ---------------- Getters & Setters ----------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ---------------- Utility Methods ----------------

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", communityId=" + communityId +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + (content != null && content.length() > 50 ? content.substring(0, 50) + "..." : content) + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
