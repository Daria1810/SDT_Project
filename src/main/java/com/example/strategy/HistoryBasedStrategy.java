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


 //recommends content based on the user's watch history
 //looks at preferred genres and finds similar titles

public class HistoryBasedStrategy implements RecommendationStrategy {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public HistoryBasedStrategy(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Content> recommend(User user, int limit) {
    // Find user's most watched genres using a derived table and exclude already watched content via LEFT JOIN.
    // This avoids potential quirks with IN (...) + LIMIT in older H2 versions.
    String sql =
        "SELECT c.* FROM content c " +
        "JOIN ( " +
        "  SELECT UPPER(TRIM(c2.genre)) AS g " +
        "  FROM content c2 " +
        "  JOIN watch_history wh ON c2.id = wh.content_id " +
        "  WHERE wh.user_id = :userId " +
        "  GROUP BY UPPER(TRIM(c2.genre)) " +
        "  ORDER BY COUNT(*) DESC " +
        "  LIMIT 3 " +
        ") topg ON UPPER(TRIM(c.genre)) = topg.g " +
        "LEFT JOIN watch_history wh2 ON wh2.user_id = :userId AND wh2.content_id = c.id " +
        "WHERE wh2.content_id IS NULL " +
        "ORDER BY c.average_rating DESC, c.view_count DESC " +
        "LIMIT " + Math.max(1, limit);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        
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
