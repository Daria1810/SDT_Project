package com.example.contentservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating new content (Movie or TV Series)
 * Used as input to the Factory Pattern
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCreateDTO {
    
    @NotBlank(message = "Content type is required")
    @Pattern(regexp = "MOVIE|TV_SERIES", message = "Content type must be either MOVIE or TV_SERIES")
    private String type; // "MOVIE" or "TV_SERIES"
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Genre is required")
    @Size(max = 50, message = "Genre must not exceed 50 characters")
    private String genre;
    
    @Min(value = 1900, message = "Release year must be after 1900")
    @Max(value = 2100, message = "Release year must be before 2100")
    private Integer releaseYear;
    
    // Movie-specific fields
    @Min(value = 1, message = "Movie duration must be at least 1 minute")
    private Integer duration; // Required for MOVIE
    
    @Size(max = 100, message = "Director name must not exceed 100 characters")
    private String director; // Required for MOVIE
    
    // TV Series-specific fields
    @Min(value = 1, message = "TV series must have at least 1 season")
    private Integer seasons; // Required for TV_SERIES
    
    @Min(value = 1, message = "Each season must have at least 1 episode")
    private Integer episodesPerSeason; // Required for TV_SERIES
}
