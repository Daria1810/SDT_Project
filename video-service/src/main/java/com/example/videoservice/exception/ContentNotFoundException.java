package com.example.videoservice.exception;

/**
 * Exception thrown when content is not found in Content Service.
 */
public class ContentNotFoundException extends RuntimeException {
    
    public ContentNotFoundException(Long contentId) {
        super("Content not found with ID: " + contentId);
    }
    
    public ContentNotFoundException(String message) {
        super(message);
    }
}
