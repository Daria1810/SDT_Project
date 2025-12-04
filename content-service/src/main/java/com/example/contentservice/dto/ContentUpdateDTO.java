package com.example.contentservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating existing content
 * All fields are optional
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentUpdateDTO {
    
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @Size(max = 50, message = "Genre must not exceed 50 characters")
    private String genre;
    
    @Min(value = 1900, message = "Release year must be after 1900")
    @Max(value = 2100, message = "Release year must be before 2100")
    private Integer releaseYear;
    
    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 10, message = "Rating cannot exceed 10")
    private Double rating;
}
