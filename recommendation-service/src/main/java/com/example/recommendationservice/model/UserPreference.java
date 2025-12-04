package com.example.recommendationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing user preferences for personalized recommendations.
 * Used by the Strategy Pattern to select the appropriate recommendation algorithm.
 */
@Entity
@Table(name = "user_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reference to the user from User Service (not a foreign key since it's a different database)
     */
    @Column(unique = true, nullable = false)
    private Long userId;
    
    /**
     * User's preferred genres stored as comma-separated values.
     * Example: "ACTION,COMEDY,DRAMA"
     */
    @Column(length = 500)
    private String preferredGenres;
    
    /**
     * Average rating given by the user (1.0 to 5.0).
     * Null if user has not rated any content yet.
     */
    @Column
    private Double averageRating;
    
    /**
     * Total number of videos watched by the user.
     * Used to determine if user is new (watchCount = 0)
     */
    @Column(nullable = false)
    private Integer watchCount = 0;
    
    /**
     * Timestamp of the last preference update.
     * Updated whenever watch history or ratings change.
     */
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    /**
     * Constructor for creating new user preferences.
     */
    public UserPreference(Long userId) {
        this.userId = userId;
        this.watchCount = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Update watch count when user watches a video.
     */
    public void incrementWatchCount() {
        this.watchCount++;
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Update average rating when user rates content.
     */
    public void updateAverageRating(Double newAverageRating) {
        this.averageRating = newAverageRating;
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Update preferred genres based on watch history.
     */
    public void updatePreferredGenres(String genres) {
        this.preferredGenres = genres;
        this.lastUpdated = LocalDateTime.now();
    }
}
