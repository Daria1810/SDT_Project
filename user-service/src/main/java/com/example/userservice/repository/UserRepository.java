package com.example.userservice.repository;

import com.example.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for User entity
 * Provides CRUD operations and custom query methods
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by username
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if email already exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if username already exists
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
