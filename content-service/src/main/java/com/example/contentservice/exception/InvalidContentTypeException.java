package com.example.contentservice.exception;

/**
 * Exception thrown when an invalid content type is provided
 */
public class InvalidContentTypeException extends RuntimeException {
    
    public InvalidContentTypeException(String type) {
        super("Invalid content type: " + type + ". Must be either MOVIE or TV_SERIES");
    }
    
    public InvalidContentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
