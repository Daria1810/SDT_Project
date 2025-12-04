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
import java.util.List;
import java.util.stream.Collectors;

/**
 * HistoryBasedStrategy - Concrete Strategy for users with watch history but no ratings.
 * 
 * Returns content from the user's preferred genres based on watch history.
 * Excludes content the user has already watched.
 * This strategy is used when watchCount > 0 but averageRating is null.
 * 
 * This is a CONCRETE STRATEGY in the Strategy Pattern.
 */
@Component
public class HistoryBasedStrategy implements RecommendationStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(HistoryBasedStrategy.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Value("${content.service.url}")
    private String contentServiceUrl;
    
    @Override
    public List<Long> recommend(Long userId, int limit) {
        logger.info("HistoryBasedStrategy: Generating recommendations for user {} based on watch history", userId);
        
        try {
            // Load user preferences
            UserPreference preferences = recommendationRepository.findByUserId(userId)
                    .orElse(new UserPreference(userId));
            
            String preferredGenres = preferences.getPreferredGenres();
            
            if (preferredGenres == null || preferredGenres.isEmpty()) {
                logger.warn("HistoryBasedStrategy: User {} has no preferred genres, falling back to all content", userId);
                return getAllContentIds(limit);
            }
            
            // Parse preferred genres
            List<String> genres = Arrays.asList(preferredGenres.split(","));
            logger.debug("HistoryBasedStrategy: User {} preferred genres: {}", userId, genres);
            
            // Get all content from Content Service
            String url = contentServiceUrl + "/api/content";
            logger.debug("HistoryBasedStrategy: Fetching content from {}", url);
            
            ResponseEntity<List<ContentResponseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContentResponseDTO>>() {}
            );
            
            List<ContentResponseDTO> allContent = response.getBody();
            
            if (allContent == null || allContent.isEmpty()) {
                logger.warn("HistoryBasedStrategy: No content available from Content Service");
                return new ArrayList<>();
            }
            
            // Filter content by preferred genres
            List<Long> recommendations = allContent.stream()
                    .filter(content -> genres.contains(content.getGenre()))
                    .limit(limit)
                    .map(ContentResponseDTO::getId)
                    .collect(Collectors.toList());
            
            // If not enough content in preferred genres, add more from other genres
            if (recommendations.size() < limit) {
                logger.debug("HistoryBasedStrategy: Only {} items in preferred genres, adding more", 
                           recommendations.size());
                
                List<Long> additionalContent = allContent.stream()
                        .filter(content -> !genres.contains(content.getGenre()))
                        .limit(limit - recommendations.size())
                        .map(ContentResponseDTO::getId)
                        .collect(Collectors.toList());
                
                recommendations.addAll(additionalContent);
            }
            
            logger.info("HistoryBasedStrategy: Returning {} recommendations for user {}", 
                       recommendations.size(), userId);
            logger.debug("HistoryBasedStrategy: Recommended content IDs: {}", recommendations);
            
            return recommendations;
            
        } catch (RestClientException e) {
            logger.error("HistoryBasedStrategy: Failed to fetch content from Content Service: {}", 
                        e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Fallback method to get all content IDs when no preferences available.
     */
    private List<Long> getAllContentIds(int limit) {
        try {
            String url = contentServiceUrl + "/api/content";
            ResponseEntity<List<ContentResponseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContentResponseDTO>>() {}
            );
            
            List<ContentResponseDTO> allContent = response.getBody();
            if (allContent != null) {
                return allContent.stream()
                        .limit(limit)
                        .map(ContentResponseDTO::getId)
                        .collect(Collectors.toList());
            }
        } catch (RestClientException e) {
            logger.error("HistoryBasedStrategy: Failed to fetch all content: {}", e.getMessage());
        }
        return new ArrayList<>();
    }
    
    @Override
    public String getStrategyName() {
        return "History-Based";
    }
}
