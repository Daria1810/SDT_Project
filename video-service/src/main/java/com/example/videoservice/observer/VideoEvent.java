package com.example.videoservice.observer;

import java.time.LocalDateTime;

/**
 * VideoEvent interface - Base interface for all video-related events.
 * 
 * This is part of the OBSERVER PATTERN implementation.
 * Events are published by the VideoEventPublisher and consumed by EventObservers.
 */
public interface VideoEvent {
    
    /**
     * Get the type of event (e.g., "VIDEO_WATCHED", "CONTENT_RATED")
     */
    String getEventType();
    
    /**
     * Get the user ID associated with this event
     */
    Long getUserId();
    
    /**
     * Get the content ID associated with this event
     */
    Long getContentId();
    
    /**
     * Get the timestamp when this event occurred
     */
    LocalDateTime getTimestamp();
}
