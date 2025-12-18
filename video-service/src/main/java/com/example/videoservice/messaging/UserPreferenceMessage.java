package com.example.videoservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message DTO for user preference updates sent via RabbitMQ.
 * 
 * This message is published when users watch or rate content,
 * allowing Recommendation Service to update preferences asynchronously.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceMessage implements Serializable {
    
    private Long userId;
    private String eventType; // "WATCH" or "RATE"
    private Long contentId;
    private String genre;
    private Integer watchCount;
    private Double averageRating;
    private String preferredGenres;
    
    public static UserPreferenceMessage forWatchEvent(Long userId, Long contentId, String genre) {
        return new UserPreferenceMessage(
            userId,
            "WATCH",
            contentId,
            genre,
            1, // increment watch count
            null,
            genre
        );
    }
    
    public static UserPreferenceMessage forRatingEvent(Long userId, Long contentId, String genre, Double score) {
        return new UserPreferenceMessage(
            userId,
            "RATE",
            contentId,
            genre,
            null,
            score,
            genre
        );
    }
}

