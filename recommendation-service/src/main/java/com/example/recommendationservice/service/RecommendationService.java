package com.example.recommendationservice.service;

import com.example.recommendationservice.dto.ContentResponseDTO;
import com.example.recommendationservice.dto.RecommendationResponseDTO;
import com.example.recommendationservice.dto.UserPreferenceUpdateDTO;
import com.example.recommendationservice.exception.RecommendationException;
import com.example.recommendationservice.model.UserPreference;
import com.example.recommendationservice.repository.RecommendationRepository;
import com.example.recommendationservice.strategy.HistoryBasedStrategy;
import com.example.recommendationservice.strategy.RatingBasedStrategy;
import com.example.recommendationservice.strategy.RecommendationStrategy;
import com.example.recommendationservice.strategy.TrendingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RecommendationService - Main business logic for recommendation generation.
 * 
 * This service:
 * 1. Selects the appropriate recommendation strategy based on user data
 * 2. Uses RecommendationEngine to execute the strategy
 * 3. Enriches recommendations with content details from Content Service
 * 
 * Strategy Selection Logic:
 * - TrendingStrategy: if watchCount == 0 (new users)
 * - HistoryBasedStrategy: if watchCount > 0 && averageRating == null (users with history but no ratings)
 * - RatingBasedStrategy: if averageRating != null (users who have rated content)
 */
@Service
public class RecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    
    @Autowired
    private TrendingStrategy trendingStrategy;
    
    @Autowired
    private HistoryBasedStrategy historyBasedStrategy;
    
    @Autowired
    private RatingBasedStrategy ratingBasedStrategy;
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${content.service.url}")
    private String contentServiceUrl;
    
    @Value("${recommendation.default-limit:10}")
    private int defaultLimit;
    
    /**
     * Get personalized recommendations for a user.
     * 
     * @param userId User ID to generate recommendations for
     * @param limit Maximum number of recommendations (optional)
     * @return RecommendationResponseDTO with list of recommended content
     */
    public RecommendationResponseDTO getRecommendations(Long userId, Integer limit) {
        logger.info("=== Generating recommendations for user {} ===", userId);
        
        int effectiveLimit = (limit != null && limit > 0) ? limit : defaultLimit;
        
        // 1. Load user preferences (or create default if not exists)
        UserPreference preferences = recommendationRepository.findByUserId(userId)
                .orElseGet(() -> {
                    logger.info("No preferences found for user {}, creating default", userId);
                    return new UserPreference(userId);
                });
        
        logger.debug("User {} preferences: watchCount={}, averageRating={}, preferredGenres={}", 
                    userId, preferences.getWatchCount(), preferences.getAverageRating(), 
                    preferences.getPreferredGenres());
        
        // 2. Select strategy based on user data
        RecommendationStrategy strategy = selectStrategy(preferences);
        logger.info("Selected strategy: '{}' for user {}", strategy.getStrategyName(), userId);
        
        // 3. Create engine and set strategy
        RecommendationEngine engine = new RecommendationEngine();
        engine.setStrategy(strategy);
        
        // 4. Get content IDs from strategy
        List<Long> contentIds = engine.getRecommendations(userId, effectiveLimit);
        logger.info("Strategy returned {} content IDs", contentIds.size());
        
        // 5. Enrich with content details from Content Service
        List<ContentResponseDTO> recommendations = enrichWithContentDetails(contentIds);
        
        logger.info("=== Successfully generated {} recommendations for user {} using {} strategy ===", 
                   recommendations.size(), userId, strategy.getStrategyName());
        
        return new RecommendationResponseDTO(
            userId,
            strategy.getStrategyName(),
            recommendations,
            recommendations.size()
        );
    }
    
    /**
     * Select the appropriate recommendation strategy based on user preferences.
     * 
     * @param preferences User preference data
     * @return Selected RecommendationStrategy
     */
    private RecommendationStrategy selectStrategy(UserPreference preferences) {
        // Strategy selection logic
        if (preferences.getWatchCount() == 0) {
            logger.debug("User has no watch history -> TrendingStrategy");
            return trendingStrategy;
        } else if (preferences.getAverageRating() == null) {
            logger.debug("User has watch history but no ratings -> HistoryBasedStrategy");
            return historyBasedStrategy;
        } else {
            logger.debug("User has ratings -> RatingBasedStrategy");
            return ratingBasedStrategy;
        }
    }
    
    /**
     * Enrich content IDs with full details from Content Service.
     * 
     * @param contentIds List of content IDs
     * @return List of ContentResponseDTO with full details
     */
    private List<ContentResponseDTO> enrichWithContentDetails(List<Long> contentIds) {
        List<ContentResponseDTO> enrichedContent = new ArrayList<>();
        
        for (Long contentId : contentIds) {
            try {
                String url = contentServiceUrl + "/api/content/" + contentId;
                logger.debug("Fetching content details from: {}", url);
                
                ContentResponseDTO content = restTemplate.getForObject(url, ContentResponseDTO.class);
                if (content != null) {
                    enrichedContent.add(content);
                    logger.debug("Successfully fetched content: {} ({})", content.getTitle(), content.getType());
                } else {
                    logger.warn("Content Service returned null for content ID {}", contentId);
                }
            } catch (RestClientException e) {
                logger.error("Failed to fetch content {}: {}", contentId, e.getMessage());
                // Continue with other content items
            }
        }
        
        return enrichedContent;
    }
    
    /**
     * Update user preferences (called by Video Service).
     * 
     * @param updateDTO User preference update data
     * @return Updated UserPreference
     */
    public UserPreference updateUserPreferences(UserPreferenceUpdateDTO updateDTO) {
        logger.info("Updating preferences for user {}", updateDTO.getUserId());
        
        UserPreference preferences = recommendationRepository.findByUserId(updateDTO.getUserId())
                .orElseGet(() -> new UserPreference(updateDTO.getUserId()));
        
        // Update fields if provided
        if (updateDTO.getPreferredGenres() != null) {
            preferences.updatePreferredGenres(updateDTO.getPreferredGenres());
            logger.debug("Updated preferred genres: {}", updateDTO.getPreferredGenres());
        }
        
        if (updateDTO.getAverageRating() != null) {
            preferences.updateAverageRating(updateDTO.getAverageRating());
            logger.debug("Updated average rating: {}", updateDTO.getAverageRating());
        }
        
        if (updateDTO.getWatchCount() != null) {
            preferences.setWatchCount(updateDTO.getWatchCount());
            preferences.setLastUpdated(LocalDateTime.now());
            logger.debug("Updated watch count: {}", updateDTO.getWatchCount());
        }
        
        UserPreference saved = recommendationRepository.save(preferences);
        logger.info("Successfully updated preferences for user {}", updateDTO.getUserId());
        
        return saved;
    }
    
    /**
     * Get user preferences.
     * 
     * @param userId User ID
     * @return UserPreference or null if not found
     */
    public UserPreference getUserPreferences(Long userId) {
        return recommendationRepository.findByUserId(userId).orElse(null);
    }
}
