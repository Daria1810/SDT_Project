package com.example.contentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FACTORY PATTERN - Abstract Product
 * 
 * Content is the abstract base class representing streaming content.
 * Uses JPA Single Table Inheritance strategy to store all content types
 * (Movies and TV Series) in a single table with a discriminator column.
 * 
 * This demonstrates the FACTORY METHOD PATTERN where:
 * - Content = Abstract Product
 * - Movie/TVSeries = Concrete Products
 * - ContentFactory = Abstract Creator
 * - MovieFactory/TVSeriesFactory = Concrete Creators
 */
@Entity
@Table(name = "content")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "content_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class Content {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @NotBlank(message = "Genre is required")
    @Size(max = 50, message = "Genre must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String genre;
    
    @Min(value = 1900, message = "Release year must be after 1900")
    @Max(value = 2100, message = "Release year must be before 2100")
    @Column(name = "release_year")
    private Integer releaseYear;
    
    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 10, message = "Rating cannot exceed 10")
    @Column(nullable = false)
    private Double rating = 0.0;
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    /**
     * Abstract method to get content type
     * Must be implemented by concrete subclasses (Movie, TVSeries)
     */
    public abstract String getContentType();
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
