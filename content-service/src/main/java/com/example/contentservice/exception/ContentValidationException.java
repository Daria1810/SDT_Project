package com.example.contentservice.exception;

/**
 * Exception thrown when factory validation fails
 * (e.g., missing required fields for Movie or TV Series)
 */
public class ContentValidationException extends RuntimeException {
    
    public ContentValidationException(String message) {
        super(message);
    }
    
    public ContentValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
