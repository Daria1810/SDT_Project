package com.example.recommendationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Recommendation Service.
 * 
 * This microservice implements the STRATEGY PATTERN for personalized content recommendations.
 * 
 * Design Pattern: STRATEGY PATTERN
 * - RecommendationStrategy (Strategy Interface)
 * - TrendingStrategy, HistoryBasedStrategy, RatingBasedStrategy (Concrete Strategies)
 * - RecommendationEngine (Context)
 * - RecommendationService (Client that selects strategy based on user data)
 * 
 * Inter-Service Communication:
 * - Communicates with Content Service to fetch content details
 * - Receives preference updates from Video Service
 * 
 * Port: 8084
 * Database: PostgreSQL on port 5435 (recommendationdb)
 */
@SpringBootApplication
public class RecommendationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RecommendationServiceApplication.class, args);
    }
}
