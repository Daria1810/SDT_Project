package com.example.recommendationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for recommendation response.
 * Contains the list of recommended content with full details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDTO {
    
    private Long userId;
    private String strategyUsed;
    private List<ContentResponseDTO> recommendations;
    private Integer totalRecommendations;
}
