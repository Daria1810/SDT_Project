package com.example.videoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a user rating for content.
 * Used by the Observer pattern to update recommendations based on user preferences.
 */
@Entity
@Table(name = "ratings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "content_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reference to the user from User Service
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * Reference to the content from Content Service
     */
    @Column(name = "content_id", nullable = false)
    private Long contentId;
    
    /**
     * Rating score (1.0 to 5.0)
     */
    @Column(nullable = false)
    private Double score;
    
    /**
     * Timestamp of when the rating was given
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * Constructor for creating new ratings
     */
    public Rating(Long userId, Long contentId, Double score) {
        this.userId = userId;
        this.contentId = contentId;
        this.score = score;
        this.timestamp = LocalDateTime.now();
    }
}
