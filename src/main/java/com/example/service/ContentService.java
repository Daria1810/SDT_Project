package com.example.service;

import com.example.factory.ContentFactory;
import com.example.factory.MovieFactory;
import com.example.factory.TVSeriesFactory;
import com.example.model.Content;
import com.example.singleton.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//handles creating and looking up content (movies and series)
//uses small factories to build the right content type
@Service
public class ContentService {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    private CacheManager cacheManager = CacheManager.getInstance();
    
    //create a new movie record
    public Content createMovie(String title, String description, String genre, int releaseYear, int durationMinutes) {
        ContentFactory factory = new MovieFactory(title, description, genre, releaseYear, durationMinutes);
        Content content = factory.createContent();
        
    // Save to database
        Long id = saveContent(content);
        content.setId(id);
        
    // Cache the new content
        cacheManager.put("content:" + id, content);
        
        System.out.println("[ContentService] Created movie: " + title + " using Factory Pattern");
        return content;
    }
    
    public Content createTVSeries(String title, String description, String genre, int releaseYear, 
                                 int seasons, int totalEpisodes, int averageEpisodeDuration) {
        ContentFactory factory = new TVSeriesFactory(title, description, genre, releaseYear, 
                                                     seasons, totalEpisodes, averageEpisodeDuration);
        Content content = factory.createContent();
        
    // Save to database
        Long id = saveContent(content);
        content.setId(id);
        
    // Cache the new content
        cacheManager.put("content:" + id, content);
        
        System.out.println("[ContentService] Created TV series: " + title + " using Factory Pattern");
        return content;
    }
    
    private Long saveContent(Content content) {
        String sql = "INSERT INTO content (title, description, genre, content_type, release_year, " +
                    "average_rating, view_count, duration_minutes, seasons, total_episodes, avg_episode_duration) " +
                    "VALUES (:title, :description, :genre, :contentType, :releaseYear, " +
                    ":averageRating, :viewCount, :durationMinutes, :seasons, :totalEpisodes, :avgEpisodeDuration)";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("title", content.getTitle())
            .addValue("description", content.getDescription())
            .addValue("genre", content.getGenre())
            .addValue("contentType", content.getContentType())
            .addValue("releaseYear", content.getReleaseYear())
            .addValue("averageRating", content.getAverageRating())
            .addValue("viewCount", content.getViewCount())
            .addValue("durationMinutes", content instanceof com.example.model.Movie ? 
                     ((com.example.model.Movie)content).getDurationMinutes() : null)
            .addValue("seasons", content instanceof com.example.model.TVSeries ? 
                     ((com.example.model.TVSeries)content).getSeasons() : null)
            .addValue("totalEpisodes", content instanceof com.example.model.TVSeries ? 
                     ((com.example.model.TVSeries)content).getTotalEpisodes() : null)
            .addValue("avgEpisodeDuration", content instanceof com.example.model.TVSeries ? 
                     ((com.example.model.TVSeries)content).getAverageEpisodeDuration() : null);
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }
    
    public Content getContentById(Long id) {
    // Fast path: check cache first
        Object cached = cacheManager.get("content:" + id);
        if (cached != null) {
            return (Content) cached;
        }
        
    // Fetch from database
        String sql = "SELECT * FROM content WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        
        List<Content> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            String type = rs.getString("content_type");
            Content content;
            
            if ("MOVIE".equals(type)) {
                com.example.model.Movie movie = new com.example.model.Movie();
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                content = movie;
            } else {
                com.example.model.TVSeries series = new com.example.model.TVSeries();
                series.setSeasons(rs.getInt("seasons"));
                series.setTotalEpisodes(rs.getInt("total_episodes"));
                series.setAverageEpisodeDuration(rs.getInt("avg_episode_duration"));
                content = series;
            }
            
            content.setId(rs.getLong("id"));
            content.setTitle(rs.getString("title"));
            content.setDescription(rs.getString("description"));
            content.setGenre(rs.getString("genre"));
            content.setReleaseYear(rs.getInt("release_year"));
            content.setAverageRating(rs.getDouble("average_rating"));
            content.setViewCount(rs.getInt("view_count"));
            return content;
        });
        
        if (!results.isEmpty()) {
            Content content = results.get(0);
            cacheManager.put("content:" + id, content);
            return content;
        }
        return null;
    }
}
