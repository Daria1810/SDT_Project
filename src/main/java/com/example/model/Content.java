package com.example.model;
import java.time.LocalDateTime;


//base type for all content (movies, series)

public abstract class Content {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private int releaseYear;
    private double averageRating;
    private int viewCount;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    /**
     * Content type label (e.g., MOVIE, TV_SERIES).
     */
    public abstract String getContentType();
    
    /**
     * Human-friendly duration info for UI.
     */
    public abstract String getDurationInfo();
}
