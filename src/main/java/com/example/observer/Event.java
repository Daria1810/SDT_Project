package com.example.observer;
import java.time.LocalDateTime;

//abstract base class for events in the observer pattern

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
