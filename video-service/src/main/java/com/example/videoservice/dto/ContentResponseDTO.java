package com.example.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for receiving content details from Content Service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponseDTO {
    
    private Long id;
    private String type;
    private String title;
    private String description;
    private String genre;
    private Integer releaseYear;
    private Double rating;
    private Long viewCount;
    private Integer duration;
    private String director;
    private Integer seasons;
    private Integer episodesPerSeason;
}
