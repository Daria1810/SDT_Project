package com.example.userservice.exception;

/**
 * Exception thrown when attempting to register with an email that already exists
 */
public class DuplicateEmailException extends RuntimeException {
    
    public DuplicateEmailException(String email) {
        super("Email already exists: " + email);
    }
    
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
