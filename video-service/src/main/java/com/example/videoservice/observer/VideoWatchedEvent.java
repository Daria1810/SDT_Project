package com.example.videoservice.observer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * VideoWatchedEvent - Concrete event for video watch events.
 * 
 * This is a CONCRETE EVENT in the Observer Pattern.
 * Published when a user watches content, triggering notifications to all observers.
 */
@Data
@AllArgsConstructor
public class VideoWatchedEvent implements VideoEvent {
    
    private Long userId;
    private Long contentId;
    private Integer progress;
    private Boolean completed;
    private LocalDateTime timestamp;
    private String genre; // Added to help update preferred genres
    
    @Override
    public String getEventType() {
        return "VIDEO_WATCHED";
    }
    
    /**
     * Constructor without genre (will be fetched from Content Service)
     */
    public VideoWatchedEvent(Long userId, Long contentId, Integer progress, Boolean completed, LocalDateTime timestamp) {
        this.userId = userId;
        this.contentId = contentId;
        this.progress = progress;
        this.completed = completed;
        this.timestamp = timestamp;
    }
}
