package com.example.contentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * FACTORY PATTERN - Concrete Product
 * 
 * TVSeries is a concrete implementation of Content representing a TV series.
 * Created by TVSeriesFactory using the Factory Method Pattern.
 * 
 * Stored in the same table as Content with discriminator value "TV_SERIES".
 */
@Entity
@DiscriminatorValue("TV_SERIES")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TVSeries extends Content {
    
    @Min(value = 1, message = "TV series must have at least 1 season")
    @Column(nullable = true) // Nullable for single table inheritance
    private Integer seasons;
    
    @Min(value = 1, message = "Each season must have at least 1 episode")
    @Column(name = "episodes_per_season", nullable = true) // Nullable for single table inheritance
    private Integer episodesPerSeason;
    
    /**
     * Returns the content type identifier for TV series
     * Used by the Factory Pattern to identify this product type
     */
    @Override
    public String getContentType() {
        return "TV_SERIES";
    }
}
