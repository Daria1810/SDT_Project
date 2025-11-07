package com.example.strategy;

import com.example.model.Content;
import com.example.model.User;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

//recommends trending content (high views, high ratings),
//great for new users with little or no history

public class TrendingStrategy implements RecommendationStrategy {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public TrendingStrategy(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Content> recommend(User user, int limit) {
        // Some H2 versions behave inconsistently with named params in LIMIT; inline the value safely.
        String sql = "SELECT * FROM content ORDER BY view_count DESC, average_rating DESC LIMIT " + Math.max(1, limit);
        return jdbcTemplate.query(sql, new HashMap<String, Object>(), new ContentRowMapper());
    }
    
    @Override
    public String getStrategyName() {
        return "Trending";
    }
    
    private static class ContentRowMapper implements RowMapper<Content> {
        @Override
        public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Simplified - would use Factory pattern to create proper Movie/TVSeries objects
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
