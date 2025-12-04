package com.example.recommendationservice.exception;

/**
 * General exception for recommendation service errors.
 */
public class RecommendationException extends RuntimeException {
    
    public RecommendationException(String message) {
        super(message);
    }
    
    public RecommendationException(String message, Throwable cause) {
        super(message, cause);
    }
}
