package com.example.userservice.exception;

/**
 * Exception thrown when a user is not found in the database
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }
    
    public UserNotFoundException(String field, String value) {
        super(String.format("User not found with %s: %s", field, value));
    }
}
