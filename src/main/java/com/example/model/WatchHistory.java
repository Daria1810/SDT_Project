package com.example.model;
import java.time.LocalDateTime;

//tracks user's watch progress for content

public class WatchHistory {
    private Long id;
    private Long userId;
    private Long contentId;
    private int progressSeconds; // Current position in video
    private LocalDateTime lastWatched;
    private boolean completed;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public int getProgressSeconds() { return progressSeconds; }
    public void setProgressSeconds(int progressSeconds) { this.progressSeconds = progressSeconds; }
    public LocalDateTime getLastWatched() { return lastWatched; }
    public void setLastWatched(LocalDateTime lastWatched) { this.lastWatched = lastWatched; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
