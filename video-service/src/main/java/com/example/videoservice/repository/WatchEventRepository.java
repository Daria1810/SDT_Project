package com.example.videoservice.repository;

import com.example.videoservice.model.WatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for WatchEvent entity.
 * Provides database access for watch history tracking.
 */
@Repository
public interface WatchEventRepository extends JpaRepository<WatchEvent, Long> {
    
    /**
     * Find all watch events for a specific user.
     * 
     * @param userId User ID
     * @return List of watch events
     */
    List<WatchEvent> findByUserIdOrderByTimestampDesc(Long userId);
    
    /**
     * Find all watch events for a specific content.
     * 
     * @param contentId Content ID
     * @return List of watch events
     */
    List<WatchEvent> findByContentId(Long contentId);
    
    /**
     * Find completed watch events for a user.
     * 
     * @param userId User ID
     * @param completed Completion status
     * @return List of completed watch events
     */
    List<WatchEvent> findByUserIdAndCompleted(Long userId, Boolean completed);
    
    /**
     * Count watch events for a user.
     * 
     * @param userId User ID
     * @return Count of watch events
     */
    long countByUserId(Long userId);
    
    /**
     * Get user's watch count (for recommendation service).
     * 
     * @param userId User ID
     * @return Watch count
     */
    @Query("SELECT COUNT(DISTINCT w.contentId) FROM WatchEvent w WHERE w.userId = :userId")
    long countDistinctContentByUserId(@Param("userId") Long userId);
}
