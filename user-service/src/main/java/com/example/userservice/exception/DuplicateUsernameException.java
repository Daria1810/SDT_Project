package com.example.userservice.exception;

/**
 * Exception thrown when attempting to register with a username that already exists
 */
public class DuplicateUsernameException extends RuntimeException {
    
    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
    }
    
    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
