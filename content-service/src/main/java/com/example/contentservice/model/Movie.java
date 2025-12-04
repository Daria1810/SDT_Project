package com.example.contentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * FACTORY PATTERN - Concrete Product
 * 
 * Movie is a concrete implementation of Content representing a movie.
 * Created by MovieFactory using the Factory Method Pattern.
 * 
 * Stored in the same table as Content with discriminator value "MOVIE".
 */
@Entity
@DiscriminatorValue("MOVIE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Movie extends Content {
    
    @Min(value = 1, message = "Movie duration must be at least 1 minute")
    @Column(nullable = true) // Nullable for single table inheritance
    private Integer duration; // Duration in minutes
    
    @NotBlank(message = "Director name is required")
    @Size(max = 100, message = "Director name must not exceed 100 characters")
    @Column(nullable = true, length = 100) // Nullable for single table inheritance
    private String director;
    
    /**
     * Returns the content type identifier for movies
     * Used by the Factory Pattern to identify this product type
     */
    @Override
    public String getContentType() {
        return "MOVIE";
    }
}
