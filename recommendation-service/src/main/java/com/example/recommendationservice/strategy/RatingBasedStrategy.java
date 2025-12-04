package com.example.recommendationservice.strategy;

import com.example.recommendationservice.dto.ContentResponseDTO;
import com.example.recommendationservice.model.UserPreference;
import com.example.recommendationservice.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RatingBasedStrategy - Concrete Strategy for users with ratings.
 * 
 * Returns highly-rated content in the user's preferred genres.
 * Filters content with rating >= (user's average rating - 0.5).
 * This strategy is used when averageRating is not null.
 * 
 * This is a CONCRETE STRATEGY in the Strategy Pattern.
 */
@Component
public class RatingBasedStrategy implements RecommendationStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(RatingBasedStrategy.class);
    
    private static final double RATING_TOLERANCE = 0.5;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Value("${content.service.url}")
    private String contentServiceUrl;
    
    @Override
    public List<Long> recommend(Long userId, int limit) {
        logger.info("RatingBasedStrategy: Generating recommendations for user {} based on ratings", userId);
        
        try {
            // Load user preferences
            UserPreference preferences = recommendationRepository.findByUserId(userId)
                    .orElse(new UserPreference(userId));
            
            Double averageRating = preferences.getAverageRating();
            if (averageRating == null) {
                logger.warn("RatingBasedStrategy: User {} has no average rating, using default threshold", userId);
                averageRating = 3.0; // Default threshold
            }
            
            String preferredGenres = preferences.getPreferredGenres();
            final List<String> genres;
            
            if (preferredGenres != null && !preferredGenres.isEmpty()) {
                genres = Arrays.asList(preferredGenres.split(","));
                logger.debug("RatingBasedStrategy: User {} preferred genres: {}", userId, genres);
            } else {
                genres = new ArrayList<>();
            }
            
            // Calculate minimum rating threshold
            double minRating = Math.max(0, averageRating - RATING_TOLERANCE);
            logger.debug("RatingBasedStrategy: User {} average rating: {}, minimum threshold: {}", 
                        userId, averageRating, minRating);
            
            // Get all content from Content Service
            String url = contentServiceUrl + "/api/content";
            logger.debug("RatingBasedStrategy: Fetching content from {}", url);
            
            ResponseEntity<List<ContentResponseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContentResponseDTO>>() {}
            );
            
            List<ContentResponseDTO> allContent = response.getBody();
            
            if (allContent == null || allContent.isEmpty()) {
                logger.warn("RatingBasedStrategy: No content available from Content Service");
                return new ArrayList<>();
            }
            
            logger.info("RatingBasedStrategy: Fetched {} content items from Content Service", allContent.size());
            
            // First, try to get content in preferred genres with good ratings
            List<Long> recommendations = new ArrayList<>();
            
            if (!genres.isEmpty()) {
                recommendations = allContent.stream()
                        .filter(content -> content.getRating() != null && content.getRating() >= minRating)
                        .filter(content -> genres.contains(content.getGenre()))
                        .sorted(Comparator.comparing(ContentResponseDTO::getRating).reversed())
                        .limit(limit)
                        .map(ContentResponseDTO::getId)
                        .collect(Collectors.toList());
                
                logger.debug("RatingBasedStrategy: Found {} items in preferred genres with rating >= {}", 
                           recommendations.size(), minRating);
            }
            
            // If not enough content in preferred genres, add highly-rated content from other genres
            if (recommendations.size() < limit) {
                logger.debug("RatingBasedStrategy: Adding more highly-rated content from other genres");
                
                List<Long> additionalContent = allContent.stream()
                        .filter(content -> content.getRating() != null && content.getRating() >= minRating)
                        .filter(content -> genres.isEmpty() || !genres.contains(content.getGenre()))
                        .sorted(Comparator.comparing(ContentResponseDTO::getRating).reversed())
                        .limit(limit - recommendations.size())
                        .map(ContentResponseDTO::getId)
                        .collect(Collectors.toList());
                
                recommendations.addAll(additionalContent);
            }
            
            logger.info("RatingBasedStrategy: Returning {} recommendations for user {}", 
                       recommendations.size(), userId);
            logger.debug("RatingBasedStrategy: Recommended content IDs: {}", recommendations);
            
            return recommendations;
            
        } catch (RestClientException e) {
            logger.error("RatingBasedStrategy: Failed to fetch content from Content Service: {}", 
                        e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Rating-Based";
    }
}
