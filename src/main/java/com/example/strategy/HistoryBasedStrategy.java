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
 * Recommends content based on the user's watch history.
 * Looks at preferred genres and finds similar titles.
 */
public class HistoryBasedStrategy implements RecommendationStrategy {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public HistoryBasedStrategy(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Content> recommend(User user, int limit) {
        // Find user's most watched genres
        String sql = "SELECT c.* FROM content c " +
                    "WHERE c.genre IN (" +
                    "  SELECT c2.genre FROM content c2 " +
                    "  JOIN watch_history wh ON c2.id = wh.content_id " +
                    "  WHERE wh.user_id = :userId " +
                    "  GROUP BY c2.genre " +
                    "  ORDER BY COUNT(*) DESC " +
                    "  LIMIT 3" +
                    ") " +
                    "AND c.id NOT IN (" +
                    "  SELECT content_id FROM watch_history WHERE user_id = :userId" +
                    ") " +
                    "ORDER BY c.average_rating DESC, c.view_count DESC " +
                    "LIMIT :limit";
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("limit", limit);
        
        return jdbcTemplate.query(sql, params, new ContentRowMapper());
    }
    
    @Override
    public String getStrategyName() {
        return "History-Based";
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
