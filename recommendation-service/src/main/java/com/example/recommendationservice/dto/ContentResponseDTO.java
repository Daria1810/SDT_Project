package com.example.recommendationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for receiving content details from Content Service.
 * Matches the ContentResponseDTO from Content Service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponseDTO {
    
    private Long id;
    private String type; // "MOVIE" or "TV_SERIES"
    private String title;
    private String description;
    private String genre;
    private Integer releaseYear;
    private Double rating;
    private Long viewCount;
    
    // Movie-specific fields
    private Integer duration;
    private String director;
    
    // TV Series-specific fields
    private Integer seasons;
    private Integer episodesPerSeason;
}
