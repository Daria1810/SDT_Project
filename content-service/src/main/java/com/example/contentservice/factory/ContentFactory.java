package com.example.contentservice.factory;

import com.example.contentservice.dto.ContentCreateDTO;
import com.example.contentservice.model.Content;

/**
 * FACTORY METHOD PATTERN - Abstract Creator
 * 
 * ContentFactory defines the interface for creating Content objects.
 * Concrete implementations (MovieFactory, TVSeriesFactory) will implement
 * the factory method to create specific types of content.
 * 
 * Benefits of Factory Pattern:
 * 1. Encapsulates object creation logic
 * 2. Validates type-specific fields before creation
 * 3. Makes code more maintainable and testable
 * 4. Follows Open/Closed Principle (open for extension, closed for modification)
 */
public interface ContentFactory {
    
    /**
     * Factory method to create Content objects
     * 
     * @param request DTO containing content creation data
     * @return Created Content instance (Movie or TVSeries)
     * @throws com.example.contentservice.exception.ContentValidationException
     *         if required fields are missing or invalid
     */
    Content createContent(ContentCreateDTO request);
    
    /**
     * Returns the content type this factory creates
     * 
     * @return "MOVIE" or "TV_SERIES"
     */
    String getContentType();
}
