package com.codenest.model;

public class Problem {
    private Long id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private Pattern pattern;
    
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
    
    public enum Pattern {
        ARRAYS, STRINGS, TREES, GRAPHS, DYNAMIC_PROGRAMMING, SORTING, SEARCHING
    }
    
    // Constructors
    public Problem() {}
    
    public Problem(String title, String description, Difficulty difficulty, Pattern pattern) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.pattern = pattern;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    
    public Pattern getPattern() { return pattern; }
    public void setPattern(Pattern pattern) { this.pattern = pattern; }
}