package com.example.recommendationservice.strategy;

import com.example.recommendationservice.dto.ContentResponseDTO;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TrendingStrategy - Concrete Strategy for new users with no watch history.
 * 
 * Returns the most-viewed content from the last 7 days across all users.
 * This strategy is used when watchCount = 0.
 * 
 * This is a CONCRETE STRATEGY in the Strategy Pattern.
 */
@Component
public class TrendingStrategy implements RecommendationStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(TrendingStrategy.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${content.service.url}")
    private String contentServiceUrl;
    
    @Override
    public List<Long> recommend(Long userId, int limit) {
        logger.info("TrendingStrategy: Generating recommendations for new user {}", userId);
        
        try {
            // Get all content from Content Service
            String url = contentServiceUrl + "/api/content";
            logger.debug("TrendingStrategy: Fetching content from {}", url);
            
            ResponseEntity<List<ContentResponseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContentResponseDTO>>() {}
            );
            
            List<ContentResponseDTO> allContent = response.getBody();
            
            if (allContent == null || allContent.isEmpty()) {
                logger.warn("TrendingStrategy: No content available from Content Service");
                return new ArrayList<>();
            }
            
            logger.info("TrendingStrategy: Fetched {} content items from Content Service", allContent.size());
            
            // Sort by view count (descending) and return top N
            List<Long> recommendations = allContent.stream()
                    .sorted(Comparator.comparing(ContentResponseDTO::getViewCount).reversed())
                    .limit(limit)
                    .map(ContentResponseDTO::getId)
                    .collect(Collectors.toList());
            
            logger.info("TrendingStrategy: Returning {} trending recommendations for user {}", 
                       recommendations.size(), userId);
            logger.debug("TrendingStrategy: Recommended content IDs: {}", recommendations);
            
            return recommendations;
            
        } catch (RestClientException e) {
            logger.error("TrendingStrategy: Failed to fetch content from Content Service: {}", 
                        e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Trending";
    }
}
