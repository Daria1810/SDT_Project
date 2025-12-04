package com.example.videoservice.observer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ContentRatedEvent - Concrete event for content rating events.
 * 
 * This is a CONCRETE EVENT in the Observer Pattern.
 * Published when a user rates content, triggering notifications to update recommendations.
 */
@Data
@AllArgsConstructor
public class ContentRatedEvent implements VideoEvent {
    
    private Long userId;
    private Long contentId;
    private Double score;
    private LocalDateTime timestamp;
    private String genre; // Added to help update preferred genres
    
    @Override
    public String getEventType() {
        return "CONTENT_RATED";
    }
    
    /**
     * Constructor without genre
     */
    public ContentRatedEvent(Long userId, Long contentId, Double score, LocalDateTime timestamp) {
        this.userId = userId;
        this.contentId = contentId;
        this.score = score;
        this.timestamp = timestamp;
    }
}
