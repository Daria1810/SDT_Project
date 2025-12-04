package com.example.recommendationservice.exception;

/**
 * Exception thrown when user preferences are not found.
 */
public class UserPreferenceNotFoundException extends RuntimeException {
    
    public UserPreferenceNotFoundException(Long userId) {
        super("User preferences not found for user ID: " + userId);
    }
    
    public UserPreferenceNotFoundException(String message) {
        super(message);
    }
}
