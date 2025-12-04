package com.example.contentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for content responses
 * Contains all content information including type-specific fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponseDTO {
    
    private Long id;
    private String type; // "MOVIE" or "TV_SERIES"
    private String title;
    private String description;
    private String genre;
    private Integer releaseYear;
    private Double rating;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Movie-specific fields (null for TV Series)
    private Integer duration;
    private String director;
    
    // TV Series-specific fields (null for Movies)
    private Integer seasons;
    private Integer episodesPerSeason;
}
