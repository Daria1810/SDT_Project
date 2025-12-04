package com.example.videoservice.service;

import com.example.videoservice.dto.*;
import com.example.videoservice.exception.ContentNotFoundException;
import com.example.videoservice.model.Rating;
import com.example.videoservice.model.WatchEvent;
import com.example.videoservice.observer.*;
import com.example.videoservice.repository.RatingRepository;
import com.example.videoservice.repository.WatchEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VideoService - Main business logic for watch events and ratings.
 * 
 * This service demonstrates the OBSERVER PATTERN in a microservices architecture:
 * 1. Validates content exists (calls Content Service)
 * 2. Saves watch event or rating to database
 * 3. Publishes event to observers via VideoEventPublisher
 * 4. Observers handle their own logic (REST calls, analytics, etc.)
 * 
 * Key Design Principles:
 * - Separation of concerns: persistence vs. notifications
 * - Graceful degradation: observer failures don't fail the main operation
 * - Inter-service communication: validates data with Content Service
 */
@Service
@Transactional
public class VideoService {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    
    @Autowired
    private WatchEventRepository watchEventRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private VideoEventPublisher eventPublisher;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${content.service.url}")
    private String contentServiceUrl;
    
    /**
     * Record a watch event.
     * 
     * Flow:
     * 1. Validate content exists (call Content Service)
     * 2. Save watch event to database
     * 3. Publish VideoWatchedEvent to observers
     * 4. Return response
     * 
     * @param createDTO Watch event creation data
     * @return WatchEventResponseDTO
     */
    public WatchEventResponseDTO recordWatchEvent(WatchEventCreateDTO createDTO) {
        logger.info("=== Recording watch event for user {} and content {} ===", 
                   createDTO.getUserId(), createDTO.getContentId());
        
        // Step 1: Validate content exists by calling Content Service
        ContentResponseDTO content = validateContentExists(createDTO.getContentId());
        logger.info("Content validated: {} ({})", content.getTitle(), content.getGenre());
        
        // Step 2: Save watch event to database
        WatchEvent watchEvent = new WatchEvent(
            createDTO.getUserId(),
            createDTO.getContentId(),
            createDTO.getProgress(),
            createDTO.getCompleted()
        );
        watchEvent.setTimestamp(LocalDateTime.now());
        
        WatchEvent saved = watchEventRepository.save(watchEvent);
        logger.info("Watch event saved with ID: {}", saved.getId());
        
        // Step 3: Publish event to observers (OBSERVER PATTERN)
        VideoWatchedEvent event = new VideoWatchedEvent(
            saved.getUserId(),
            saved.getContentId(),
            saved.getProgress(),
            saved.getCompleted(),
            saved.getTimestamp(),
            content.getGenre() // Include genre for preference updates
        );
        
        logger.info("Publishing VideoWatchedEvent to observers...");
        eventPublisher.notifyObservers(event);
        
        // Step 4: Return response
        WatchEventResponseDTO response = new WatchEventResponseDTO(
            saved.getId(),
            saved.getUserId(),
            saved.getContentId(),
            saved.getProgress(),
            saved.getCompleted(),
            saved.getTimestamp()
        );
        
        logger.info("=== Watch event recorded successfully: ID {} ===", saved.getId());
        return response;
    }
    
    /**
     * Submit a rating for content.
     * 
     * Flow:
     * 1. Validate content exists (call Content Service)
     * 2. Check if rating already exists (update if yes, create if no)
     * 3. Save rating to database
     * 4. Publish ContentRatedEvent to observers
     * 5. Return response
     * 
     * @param createDTO Rating creation data
     * @return RatingResponseDTO
     */
    public RatingResponseDTO submitRating(RatingCreateDTO createDTO) {
        logger.info("=== Submitting rating for user {} and content {} ===", 
                   createDTO.getUserId(), createDTO.getContentId());
        
        // Step 1: Validate content exists
        ContentResponseDTO content = validateContentExists(createDTO.getContentId());
        logger.info("Content validated: {} ({})", content.getTitle(), content.getGenre());
        
        // Step 2: Check if rating already exists
        Rating rating = ratingRepository.findByUserIdAndContentId(
            createDTO.getUserId(), 
            createDTO.getContentId()
        ).orElse(new Rating(createDTO.getUserId(), createDTO.getContentId(), createDTO.getScore()));
        
        // Update score if rating already exists
        if (rating.getId() != null) {
            logger.info("Updating existing rating ID {} from {} to {}", 
                       rating.getId(), rating.getScore(), createDTO.getScore());
            rating.setScore(createDTO.getScore());
            rating.setTimestamp(LocalDateTime.now());
        } else {
            logger.info("Creating new rating with score {}", createDTO.getScore());
        }
        
        // Step 3: Save rating
        Rating saved = ratingRepository.save(rating);
        logger.info("Rating saved with ID: {}", saved.getId());
        
        // Step 4: Calculate user's average rating for recommendations
        Double averageRating = ratingRepository.calculateAverageRatingByUserId(saved.getUserId());
        logger.info("User {} average rating: {}", saved.getUserId(), averageRating);
        
        // Step 5: Publish event to observers (OBSERVER PATTERN)
        ContentRatedEvent event = new ContentRatedEvent(
            saved.getUserId(),
            saved.getContentId(),
            saved.getScore(),
            saved.getTimestamp(),
            content.getGenre() // Include genre for preference updates
        );
        
        logger.info("Publishing ContentRatedEvent to observers...");
        eventPublisher.notifyObservers(event);
        
        // Step 6: Return response
        RatingResponseDTO response = new RatingResponseDTO(
            saved.getId(),
            saved.getUserId(),
            saved.getContentId(),
            saved.getScore(),
            saved.getTimestamp()
        );
        
        logger.info("=== Rating submitted successfully: ID {} ===", saved.getId());
        return response;
    }
    
    /**
     * Validate that content exists by calling Content Service.
     * 
     * @param contentId Content ID to validate
     * @return ContentResponseDTO with content details
     * @throws ContentNotFoundException if content doesn't exist
     */
    private ContentResponseDTO validateContentExists(Long contentId) {
        try {
            String url = contentServiceUrl + "/api/content/" + contentId;
            logger.debug("Validating content exists: GET {}", url);
            
            ContentResponseDTO content = restTemplate.getForObject(url, ContentResponseDTO.class);
            
            if (content == null) {
                throw new ContentNotFoundException(contentId);
            }
            
            return content;
            
        } catch (RestClientException e) {
            logger.error("Failed to validate content {}: {}", contentId, e.getMessage());
            throw new ContentNotFoundException("Failed to validate content with ID: " + contentId);
        }
    }
    
    /**
     * Get all watch events for a user.
     * 
     * @param userId User ID
     * @return List of watch event responses
     */
    public List<WatchEventResponseDTO> getUserWatchHistory(Long userId) {
        logger.info("Fetching watch history for user {}", userId);
        
        List<WatchEvent> events = watchEventRepository.findByUserIdOrderByTimestampDesc(userId);
        
        return events.stream()
                .map(e -> new WatchEventResponseDTO(
                    e.getId(), e.getUserId(), e.getContentId(), 
                    e.getProgress(), e.getCompleted(), e.getTimestamp()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all ratings by a user.
     * 
     * @param userId User ID
     * @return List of rating responses
     */
    public List<RatingResponseDTO> getUserRatings(Long userId) {
        logger.info("Fetching ratings for user {}", userId);
        
        List<Rating> ratings = ratingRepository.findByUserIdOrderByTimestampDesc(userId);
        
        return ratings.stream()
                .map(r -> new RatingResponseDTO(
                    r.getId(), r.getUserId(), r.getContentId(), 
                    r.getScore(), r.getTimestamp()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get user's average rating.
     * 
     * @param userId User ID
     * @return Average rating or null if no ratings
     */
    public Double getUserAverageRating(Long userId) {
        return ratingRepository.calculateAverageRatingByUserId(userId);
    }
    
    /**
     * Get content's average rating.
     * 
     * @param contentId Content ID
     * @return Average rating or null if no ratings
     */
    public Double getContentAverageRating(Long contentId) {
        return ratingRepository.calculateAverageRatingForContent(contentId);
    }
}
