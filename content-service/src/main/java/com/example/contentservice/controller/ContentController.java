package com.example.contentservice.controller;

import com.example.contentservice.dto.ContentCreateDTO;
import com.example.contentservice.dto.ContentResponseDTO;
import com.example.contentservice.dto.ContentUpdateDTO;
import com.example.contentservice.service.ContentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Content Controller - REST API endpoints for content management
 * 
 * Base URL: /api/content
 * Port: 8082
 * 
 * Demonstrates Factory Pattern through content creation endpoint
 */
@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "*")
public class ContentController {
    
    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);
    
    @Autowired
    private ContentService contentService;
    
    /**
     * Health check endpoint
     * GET /api/content/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Content Service is running");
    }
    
    /**
     * Create new content (Movie or TV Series)
     * POST /api/content
     * 
     * FACTORY PATTERN DEMONSTRATION:
     * This endpoint uses the Factory Pattern to create content.
     * Based on the "type" field, the appropriate factory is selected.
     * 
     * Request Body for Movie:
     * {
     *   "type": "MOVIE",
     *   "title": "Inception",
     *   "description": "A mind-bending thriller",
     *   "genre": "Sci-Fi",
     *   "releaseYear": 2010,
     *   "duration": 148,
     *   "director": "Christopher Nolan"
     * }
     * 
     * Request Body for TV Series:
     * {
     *   "type": "TV_SERIES",
     *   "title": "Breaking Bad",
     *   "description": "A high school teacher turned meth cook",
     *   "genre": "Drama",
     *   "releaseYear": 2008,
     *   "seasons": 5,
     *   "episodesPerSeason": 13
     * }
     * 
     * Response: 201 Created with ContentResponseDTO
     */
    @PostMapping
    public ResponseEntity<ContentResponseDTO> createContent(@Valid @RequestBody ContentCreateDTO createDTO) {
        logger.info("POST /api/content - Creating {} with title: {}", createDTO.getType(), createDTO.getTitle());
        ContentResponseDTO content = contentService.createContent(createDTO);
        return new ResponseEntity<>(content, HttpStatus.CREATED);
    }
    
    /**
     * Get content by ID
     * GET /api/content/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponseDTO> getContentById(@PathVariable Long id) {
        logger.info("GET /api/content/{}", id);
        ContentResponseDTO content = contentService.getContentById(id);
        return ResponseEntity.ok(content);
    }
    
    /**
     * Get all content
     * GET /api/content
     */
    @GetMapping
    public ResponseEntity<List<ContentResponseDTO>> getAllContent() {
        logger.info("GET /api/content");
        List<ContentResponseDTO> content = contentService.getAllContent();
        return ResponseEntity.ok(content);
    }
    
    /**
     * Get all movies
     * GET /api/content/movies
     */
    @GetMapping("/movies")
    public ResponseEntity<List<ContentResponseDTO>> getAllMovies() {
        logger.info("GET /api/content/movies");
        List<ContentResponseDTO> movies = contentService.getAllMovies();
        return ResponseEntity.ok(movies);
    }
    
    /**
     * Get all TV series
     * GET /api/content/series
     */
    @GetMapping("/series")
    public ResponseEntity<List<ContentResponseDTO>> getAllTVSeries() {
        logger.info("GET /api/content/series");
        List<ContentResponseDTO> series = contentService.getAllTVSeries();
        return ResponseEntity.ok(series);
    }
    
    /**
     * Search content by title
     * GET /api/content/search?title={title}
     */
    @GetMapping("/search")
    public ResponseEntity<List<ContentResponseDTO>> searchContent(@RequestParam String title) {
        logger.info("GET /api/content/search?title={}", title);
        List<ContentResponseDTO> content = contentService.searchByTitle(title);
        return ResponseEntity.ok(content);
    }
    
    /**
     * Get content by genre
     * GET /api/content/genre/{genre}
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<ContentResponseDTO>> getByGenre(@PathVariable String genre) {
        logger.info("GET /api/content/genre/{}", genre);
        List<ContentResponseDTO> content = contentService.getByGenre(genre);
        return ResponseEntity.ok(content);
    }
    
    /**
     * Get top-rated content
     * GET /api/content/top-rated?minRating={rating}
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<ContentResponseDTO>> getTopRated(
            @RequestParam(defaultValue = "7.0") Double minRating) {
        logger.info("GET /api/content/top-rated?minRating={}", minRating);
        List<ContentResponseDTO> content = contentService.getTopRated(minRating);
        return ResponseEntity.ok(content);
    }
    
    /**
     * Get most-viewed content
     * GET /api/content/most-viewed
     */
    @GetMapping("/most-viewed")
    public ResponseEntity<List<ContentResponseDTO>> getMostViewed() {
        logger.info("GET /api/content/most-viewed");
        List<ContentResponseDTO> content = contentService.getMostViewed();
        return ResponseEntity.ok(content);
    }
    
    /**
     * Update content
     * PUT /api/content/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponseDTO> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ContentUpdateDTO updateDTO) {
        logger.info("PUT /api/content/{}", id);
        ContentResponseDTO content = contentService.updateContent(id, updateDTO);
        return ResponseEntity.ok(content);
    }
    
    /**
     * Increment view count
     * POST /api/content/{id}/view
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        logger.info("POST /api/content/{}/view", id);
        contentService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Delete content
     * DELETE /api/content/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        logger.info("DELETE /api/content/{}", id);
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
