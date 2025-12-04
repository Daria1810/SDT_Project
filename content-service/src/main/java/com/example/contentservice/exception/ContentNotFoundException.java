package com.example.contentservice.exception;

/**
 * Exception thrown when content is not found in the database
 */
public class ContentNotFoundException extends RuntimeException {
    
    public ContentNotFoundException(String message) {
        super(message);
    }
    
    public ContentNotFoundException(Long contentId) {
        super("Content not found with ID: " + contentId);
    }
}
