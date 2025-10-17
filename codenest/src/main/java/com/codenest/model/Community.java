package com.codenest.model;

import java.time.LocalDateTime;

public class Community {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    // ===== Default constructor =====
    public Community() {}

    // ===== Constructor for new community creation =====
    public Community(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // ===== Full constructor (used by DAO) =====
    public Community(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // ===== Getters and Setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name + " — " + description;
    }
}
