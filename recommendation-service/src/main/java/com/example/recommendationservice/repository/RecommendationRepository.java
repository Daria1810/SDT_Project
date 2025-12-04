package com.example.recommendationservice.repository;

import com.example.recommendationservice.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for UserPreference entity.
 * Provides database access for user preference data.
 */
@Repository
public interface RecommendationRepository extends JpaRepository<UserPreference, Long> {
    
    /**
     * Find user preferences by user ID.
     * 
     * @param userId The user ID to search for
     * @return Optional containing UserPreference if found
     */
    Optional<UserPreference> findByUserId(Long userId);
    
    /**
     * Check if preferences exist for a user.
     * 
     * @param userId The user ID to check
     * @return true if preferences exist, false otherwise
     */
    boolean existsByUserId(Long userId);
}
