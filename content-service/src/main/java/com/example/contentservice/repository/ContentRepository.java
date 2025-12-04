package com.example.contentservice.repository;

import com.example.contentservice.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Content entity
 * Provides CRUD operations and custom query methods
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    
    /**
     * Find content by title (case-insensitive)
     */
    List<Content> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find content by genre
     */
    List<Content> findByGenre(String genre);
    
    /**
     * Find content by release year
     */
    List<Content> findByReleaseYear(Integer year);
    
    /**
     * Find content with rating greater than or equal to specified value
     */
    List<Content> findByRatingGreaterThanEqual(Double minRating);
    
    /**
     * Find all movies (content_type = 'MOVIE')
     */
    @Query("SELECT c FROM Content c WHERE TYPE(c) = Movie")
    List<Content> findAllMovies();
    
    /**
     * Find all TV series (content_type = 'TV_SERIES')
     */
    @Query("SELECT c FROM Content c WHERE TYPE(c) = TVSeries")
    List<Content> findAllTVSeries();
    
    /**
     * Get top N most-viewed content
     */
    List<Content> findTop10ByOrderByViewCountDesc();
    
    /**
     * Search content by title and genre
     */
    List<Content> findByTitleContainingIgnoreCaseAndGenre(String title, String genre);
}
