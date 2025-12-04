package com.example.videoservice.repository;

import com.example.videoservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Rating entity.
 * Provides database access for content ratings.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    /**
     * Find all ratings by a specific user.
     * 
     * @param userId User ID
     * @return List of ratings
     */
    List<Rating> findByUserIdOrderByTimestampDesc(Long userId);
    
    /**
     * Find all ratings for a specific content.
     * 
     * @param contentId Content ID
     * @return List of ratings
     */
    List<Rating> findByContentId(Long contentId);
    
    /**
     * Find a specific rating by user and content.
     * 
     * @param userId User ID
     * @param contentId Content ID
     * @return Optional containing the rating if found
     */
    Optional<Rating> findByUserIdAndContentId(Long userId, Long contentId);
    
    /**
     * Calculate average rating given by a user.
     * 
     * @param userId User ID
     * @return Average rating
     */
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.userId = :userId")
    Double calculateAverageRatingByUserId(@Param("userId") Long userId);
    
    /**
     * Calculate average rating for a content.
     * 
     * @param contentId Content ID
     * @return Average rating
     */
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.contentId = :contentId")
    Double calculateAverageRatingForContent(@Param("contentId") Long contentId);
    
    /**
     * Count ratings by a user.
     * 
     * @param userId User ID
     * @return Count of ratings
     */
    long countByUserId(Long userId);
}
