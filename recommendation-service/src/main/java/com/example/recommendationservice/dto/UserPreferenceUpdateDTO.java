package com.example.recommendationservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user preferences.
 * Sent by Video Service when user watches content or rates it.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceUpdateDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    /**
     * Comma-separated list of genres (e.g., "ACTION,COMEDY,DRAMA")
     */
    private String preferredGenres;
    
    /**
     * Average rating given by the user (1.0 to 5.0)
     */
    private Double averageRating;
    
    /**
     * Total number of videos watched
     */
    @Min(value = 0, message = "Watch count must be non-negative")
    private Integer watchCount;
}
