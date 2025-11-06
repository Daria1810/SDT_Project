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

/**
 * Recommends content similar to items a user rated highly.
 * Best for users who actively leave ratings.
 */
public class RatingBasedStrategy implements RecommendationStrategy {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public RatingBasedStrategy(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Content> recommend(User user, int limit) {
        // Find content similar to user's 4-5 star ratings
        String sql = "SELECT c.* FROM content c " +
                    "WHERE c.genre IN (" +
                    "  SELECT c2.genre FROM content c2 " +
                    "  JOIN rating r ON c2.id = r.content_id " +
                    "  WHERE r.user_id = :userId AND r.rating >= 4 " +
                    "  GROUP BY c2.genre " +
                    "  ORDER BY AVG(r.rating) DESC " +
                    "  LIMIT 3" +
                    ") " +
                    "AND c.id NOT IN (" +
                    "  SELECT content_id FROM rating WHERE user_id = :userId" +
                    ") " +
                    "ORDER BY c.average_rating DESC " +
                    "LIMIT :limit";
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("limit", limit);
        
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
