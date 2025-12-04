package com.example.videoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a video watch event.
 * Tracks when users watch content, their progress, and completion status.
 */
@Entity
@Table(name = "watch_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reference to the user from User Service (not a foreign key since it's a different database)
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * Reference to the content from Content Service (not a foreign key since it's a different database)
     */
    @Column(nullable = false)
    private Long contentId;
    
    /**
     * Watch progress in seconds
     */
    @Column(nullable = false)
    private Integer progress;
    
    /**
     * Whether the user completed watching the content
     */
    @Column(nullable = false)
    private Boolean completed = false;
    
    /**
     * Timestamp of when the watch event occurred
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * Constructor for creating new watch events
     */
    public WatchEvent(Long userId, Long contentId, Integer progress, Boolean completed) {
        this.userId = userId;
        this.contentId = contentId;
        this.progress = progress;
        this.completed = completed;
        this.timestamp = LocalDateTime.now();
    }
}
