package com.example.strategy;

import com.example.model.Content;
import com.example.model.User;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//recommends content similar to items a user rated highly
//best for users who actively leave ratings
public class RatingBasedStrategy implements RecommendationStrategy {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public RatingBasedStrategy(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Content> recommend(User user, int limit) {
        //find content similar to user's 4-5 star ratings
        //Use derived-table JOIN for top genres and LEFT JOIN to exclude already-rated content
        String sql =
            "SELECT c.* FROM content c " +
            "JOIN ( " +
            "  SELECT UPPER(TRIM(c2.genre)) AS g " +
            "  FROM content c2 " +
            "  JOIN rating r ON c2.id = r.content_id " +
            "  WHERE r.user_id = :userId AND r.rating >= 4 " +
            "  GROUP BY UPPER(TRIM(c2.genre)) " +
            "  ORDER BY AVG(r.rating) DESC " +
            "  LIMIT 3 " +
            ") topr ON UPPER(TRIM(c.genre)) = topr.g " +
            "LEFT JOIN rating r2 ON r2.user_id = :userId AND r2.content_id = c.id " +
            "WHERE r2.content_id IS NULL " +
            "ORDER BY c.average_rating DESC " +
            "LIMIT " + Math.max(1, limit);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        
        return jdbcTemplate.query(sql, params, new ContentRowMapper());
    }
    
    @Override
    public String getStrategyName() {
        return "Rating-Based";
    }
    
    private static class ContentRowMapper implements RowMapper<Content> {
        @Override
        public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
            Content content = new com.example.model.Movie();
            content.setId(rs.getLong("id"));
            content.setTitle(rs.getString("title"));
            content.setDescription(rs.getString("description"));
            content.setGenre(rs.getString("genre"));
            content.setReleaseYear(rs.getInt("release_year"));
            content.setAverageRating(rs.getDouble("average_rating"));
            content.setViewCount(rs.getInt("view_count"));
            return content;
        }
    }
}
