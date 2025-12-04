package com.example.contentservice.factory;

import com.example.contentservice.dto.ContentCreateDTO;
import com.example.contentservice.exception.ContentValidationException;
import com.example.contentservice.model.Content;
import com.example.contentservice.model.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * FACTORY METHOD PATTERN - Concrete Creator
 * 
 * MovieFactory creates Movie objects with proper validation.
 * 
 * Demonstrates Factory Pattern benefits:
 * - Validates movie-specific fields (duration, director)
 * - Encapsulates Movie creation logic
 * - Ensures all Movies are created consistently
 * 
 * Used by ContentService through dependency injection with @Qualifier("movieFactory")
 */
@Component("movieFactory")
public class MovieFactory implements ContentFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(MovieFactory.class);
    
    /**
     * Creates a Movie object from the request DTO
     * 
     * Validates:
     * - Duration must be positive
     * - Director must not be blank
     * 
     * @param request DTO containing movie data
     * @return Created Movie instance
     * @throws ContentValidationException if validation fails
     */
    @Override
    public Content createContent(ContentCreateDTO request) {
        logger.info("MovieFactory: Creating movie with title '{}'", request.getTitle());
        
        // FACTORY PATTERN: Validate movie-specific fields
        if (request.getDuration() == null || request.getDuration() <= 0) {
            logger.error("MovieFactory: Validation failed - Duration is invalid: {}", request.getDuration());
            throw new ContentValidationException("Movie duration must be positive");
        }
        
        if (request.getDirector() == null || request.getDirector().isBlank()) {
            logger.error("MovieFactory: Validation failed - Director is missing");
            throw new ContentValidationException("Movie director is required");
        }
        
        // FACTORY PATTERN: Create and configure Movie object
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenre(request.getGenre());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setDuration(request.getDuration());
        movie.setDirector(request.getDirector());
        movie.setRating(0.0);
        movie.setViewCount(0L);
        
        logger.info("MovieFactory: Movie created successfully - Title: '{}', Duration: {} min, Director: '{}'",
            movie.getTitle(), movie.getDuration(), movie.getDirector());
        
        return movie;
    }
    
    /**
     * Returns the content type this factory creates
     */
    @Override
    public String getContentType() {
        return "MOVIE";
    }
}
