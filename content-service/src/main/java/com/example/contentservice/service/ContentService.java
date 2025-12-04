package com.example.contentservice.service;

import com.example.contentservice.dto.ContentCreateDTO;
import com.example.contentservice.dto.ContentResponseDTO;
import com.example.contentservice.dto.ContentUpdateDTO;
import com.example.contentservice.exception.ContentNotFoundException;
import com.example.contentservice.exception.InvalidContentTypeException;
import com.example.contentservice.factory.ContentFactory;
import com.example.contentservice.model.Content;
import com.example.contentservice.model.Movie;
import com.example.contentservice.model.TVSeries;
import com.example.contentservice.repository.ContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Content Service - Business logic for content management
 * 
 * DEMONSTRATES FACTORY METHOD PATTERN:
 * - Uses MovieFactory and TVSeriesFactory to create content
 * - Factory selection based on content type
 * - Delegates validation and creation to appropriate factory
 * 
 * Benefits:
 * - Clean separation of creation logic
 * - Easy to add new content types
 * - Type-specific validation encapsulated in factories
 */
@Service
public class ContentService {
    
    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);
    
    @Autowired
    private ContentRepository contentRepository;
    
    // FACTORY PATTERN: Inject both factories using Spring's dependency injection
    @Autowired
    @Qualifier("movieFactory")
    private ContentFactory movieFactory;
    
    @Autowired
    @Qualifier("tvSeriesFactory")
    private ContentFactory tvSeriesFactory;
    
    /**
     * Create new content using appropriate factory
     * 
     * FACTORY PATTERN IN ACTION:
     * 1. Determine content type from request
     * 2. Select appropriate factory
     * 3. Factory validates and creates content
     * 4. Save to database
     * 
     * @param request Content creation data
     * @return Created content DTO
     */
    @Transactional
    public ContentResponseDTO createContent(ContentCreateDTO request) {
        logger.info("Creating content of type '{}' with title '{}'", request.getType(), request.getTitle());
        
        // FACTORY PATTERN: Select factory based on content type
        ContentFactory factory = getFactory(request.getType());
        
        // FACTORY PATTERN: Factory creates and validates content
        Content content = factory.createContent(request);
        
        // Save to database
        Content savedContent = contentRepository.save(content);
        
        logger.info("Content created successfully with ID: {} (Type: {})", 
            savedContent.getId(), savedContent.getContentType());
        
        return convertToDTO(savedContent);
    }
    
    /**
     * Get content by ID
     */
    public ContentResponseDTO getContentById(Long id) {
        Content content = contentRepository.findById(id)
            .orElseThrow(() -> new ContentNotFoundException(id));
        return convertToDTO(content);
    }
    
    /**
     * Get all content
     */
    public List<ContentResponseDTO> getAllContent() {
        return contentRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all movies
     */
    public List<ContentResponseDTO> getAllMovies() {
        return contentRepository.findAllMovies()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all TV series
     */
    public List<ContentResponseDTO> getAllTVSeries() {
        return contentRepository.findAllTVSeries()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Search content by title
     */
    public List<ContentResponseDTO> searchByTitle(String title) {
        return contentRepository.findByTitleContainingIgnoreCase(title)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get content by genre
     */
    public List<ContentResponseDTO> getByGenre(String genre) {
        return contentRepository.findByGenre(genre)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get top-rated content
     */
    public List<ContentResponseDTO> getTopRated(Double minRating) {
        return contentRepository.findByRatingGreaterThanEqual(minRating)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get most-viewed content
     */
    public List<ContentResponseDTO> getMostViewed() {
        return contentRepository.findTop10ByOrderByViewCountDesc()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Update content
     */
    @Transactional
    public ContentResponseDTO updateContent(Long id, ContentUpdateDTO updateDTO) {
        Content content = contentRepository.findById(id)
            .orElseThrow(() -> new ContentNotFoundException(id));
        
        if (updateDTO.getTitle() != null) {
            content.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            content.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getGenre() != null) {
            content.setGenre(updateDTO.getGenre());
        }
        if (updateDTO.getReleaseYear() != null) {
            content.setReleaseYear(updateDTO.getReleaseYear());
        }
        if (updateDTO.getRating() != null) {
            content.setRating(updateDTO.getRating());
        }
        
        Content updatedContent = contentRepository.save(content);
        logger.info("Content updated successfully: ID {}", id);
        
        return convertToDTO(updatedContent);
    }
    
    /**
     * Increment view count
     */
    @Transactional
    public void incrementViewCount(Long id) {
        Content content = contentRepository.findById(id)
            .orElseThrow(() -> new ContentNotFoundException(id));
        content.setViewCount(content.getViewCount() + 1);
        contentRepository.save(content);
    }
    
    /**
     * Delete content
     */
    @Transactional
    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new ContentNotFoundException(id);
        }
        contentRepository.deleteById(id);
        logger.info("Content deleted successfully: ID {}", id);
    }
    
    /**
     * FACTORY PATTERN: Select appropriate factory based on content type
     * 
     * @param type "MOVIE" or "TV_SERIES"
     * @return Appropriate factory instance
     * @throws InvalidContentTypeException if type is invalid
     */
    private ContentFactory getFactory(String type) {
        if ("MOVIE".equals(type)) {
            logger.debug("Selected MovieFactory for content creation");
            return movieFactory;
        } else if ("TV_SERIES".equals(type)) {
            logger.debug("Selected TVSeriesFactory for content creation");
            return tvSeriesFactory;
        } else {
            throw new InvalidContentTypeException(type);
        }
    }
    
    /**
     * Convert Content entity to DTO
     */
    private ContentResponseDTO convertToDTO(Content content) {
        ContentResponseDTO dto = ContentResponseDTO.builder()
            .id(content.getId())
            .type(content.getContentType())
            .title(content.getTitle())
            .description(content.getDescription())
            .genre(content.getGenre())
            .releaseYear(content.getReleaseYear())
            .rating(content.getRating())
            .viewCount(content.getViewCount())
            .createdAt(content.getCreatedAt())
            .updatedAt(content.getUpdatedAt())
            .build();
        
        // Add type-specific fields
        if (content instanceof Movie) {
            Movie movie = (Movie) content;
            dto.setDuration(movie.getDuration());
            dto.setDirector(movie.getDirector());
        } else if (content instanceof TVSeries) {
            TVSeries series = (TVSeries) content;
            dto.setSeasons(series.getSeasons());
            dto.setEpisodesPerSeason(series.getEpisodesPerSeason());
        }
        
        return dto;
    }
}
