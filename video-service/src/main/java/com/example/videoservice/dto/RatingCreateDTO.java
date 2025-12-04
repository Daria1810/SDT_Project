package com.example.videoservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating ratings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingCreateDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Content ID is required")
    private Long contentId;
    
    @NotNull(message = "Score is required")
    @DecimalMin(value = "1.0", message = "Score must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Score must be at most 5.0")
    private Double score;
}
