package com.example.observer;
import java.time.LocalDateTime;

/**
 * Base class for domain events emitted by the app.
 */
public abstract class Event {
    private LocalDateTime timestamp;
    private Long userId;
    
    public Event(Long userId) {
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public Long getUserId() { return userId; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public abstract String getEventType();
}
