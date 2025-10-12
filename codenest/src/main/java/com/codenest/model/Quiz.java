package com.codenest.model;

import java.time.LocalDateTime;

public class Quiz {
    private Long id;
    private String subject;
    private String title;
    private int timeLimit; // in minutes
    private LocalDateTime createdAt;
    
    // Constructors
    public Quiz() {}
    
    public Quiz(String subject, String title, int timeLimit) {
        this.subject = subject;
        this.title = title;
        this.timeLimit = timeLimit;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
