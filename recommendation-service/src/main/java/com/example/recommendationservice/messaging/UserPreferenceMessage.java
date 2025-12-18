package com.example.recommendationservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message DTO for user preference updates received from RabbitMQ.
 * 
 * This message is consumed when users watch or rate content,
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
}

