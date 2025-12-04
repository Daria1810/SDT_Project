package com.example.contentservice.factory;

import com.example.contentservice.dto.ContentCreateDTO;
import com.example.contentservice.exception.ContentValidationException;
import com.example.contentservice.model.Content;
import com.example.contentservice.model.TVSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * FACTORY METHOD PATTERN - Concrete Creator
 * 
 * TVSeriesFactory creates TVSeries objects with proper validation.
 * 
 * Demonstrates Factory Pattern benefits:
 * - Validates TV series-specific fields (seasons, episodesPerSeason)
 * - Encapsulates TVSeries creation logic
 * - Ensures all TV Series are created consistently
 * 
 * Used by ContentService through dependency injection with @Qualifier("tvSeriesFactory")
 */
@Component("tvSeriesFactory")
public class TVSeriesFactory implements ContentFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(TVSeriesFactory.class);
    
    /**
     * Creates a TVSeries object from the request DTO
     * 
     * Validates:
     * - Seasons must be positive
     * - Episodes per season must be positive
     * 
     * @param request DTO containing TV series data
     * @return Created TVSeries instance
     * @throws ContentValidationException if validation fails
     */
    @Override
    public Content createContent(ContentCreateDTO request) {
        logger.info("TVSeriesFactory: Creating TV series with title '{}'", request.getTitle());
        
        // FACTORY PATTERN: Validate TV series-specific fields
        if (request.getSeasons() == null || request.getSeasons() <= 0) {
            logger.error("TVSeriesFactory: Validation failed - Seasons is invalid: {}", request.getSeasons());
            throw new ContentValidationException("TV series must have at least 1 season");
        }
        
        if (request.getEpisodesPerSeason() == null || request.getEpisodesPerSeason() <= 0) {
            logger.error("TVSeriesFactory: Validation failed - Episodes per season is invalid: {}", 
                request.getEpisodesPerSeason());
            throw new ContentValidationException("TV series must have at least 1 episode per season");
        }
        
        // FACTORY PATTERN: Create and configure TVSeries object
        TVSeries series = new TVSeries();
        series.setTitle(request.getTitle());
        series.setDescription(request.getDescription());
        series.setGenre(request.getGenre());
        series.setReleaseYear(request.getReleaseYear());
        series.setSeasons(request.getSeasons());
        series.setEpisodesPerSeason(request.getEpisodesPerSeason());
        series.setRating(0.0);
        series.setViewCount(0L);
        
        logger.info("TVSeriesFactory: TV series created successfully - Title: '{}', Seasons: {}, Episodes/Season: {}",
            series.getTitle(), series.getSeasons(), series.getEpisodesPerSeason());
        
        return series;
    }
    
    /**
     * Returns the content type this factory creates
     */
    @Override
    public String getContentType() {
        return "TV_SERIES";
    }
}
