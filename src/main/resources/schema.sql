-- StreamFlix Database Schema

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    subscription_tier VARCHAR(50) NOT NULL,
    is_new BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Content table (Movies and TV Series)
CREATE TABLE IF NOT EXISTS content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    genre VARCHAR(100),
    content_type VARCHAR(50) NOT NULL,
    release_year INT,
    average_rating DOUBLE DEFAULT 0.0,
    view_count INT DEFAULT 0,
    -- Movie-specific fields
    duration_minutes INT,
    -- TV Series-specific fields
    seasons INT,
    total_episodes INT,
    avg_episode_duration INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ratings table
CREATE TABLE IF NOT EXISTS rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_content (user_id, content_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (content_id) REFERENCES content(id)
);

-- Watch History table
CREATE TABLE IF NOT EXISTS watch_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content_id BIGINT NOT NULL,
    progress_seconds INT DEFAULT 0,
    last_watched TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed BOOLEAN DEFAULT FALSE,
    UNIQUE KEY unique_user_content_history (user_id, content_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (content_id) REFERENCES content(id)
);

-- Insert sample data

-- Sample users
INSERT INTO users (email, password_hash, subscription_tier, is_new) VALUES
('john@example.com', 'hashed_password123', 'PREMIUM', FALSE),
('jane@example.com', 'hashed_password456', 'STANDARD', TRUE),
('bob@example.com', 'hashed_password789', 'BASIC', FALSE);

-- Sample movies
INSERT INTO content (title, description, genre, content_type, release_year, average_rating, view_count, duration_minutes) VALUES
('The Matrix', 'A hacker discovers reality is a simulation', 'Sci-Fi', 'MOVIE', 1999, 4.5, 15000, 136),
('Inception', 'Dream within a dream heist thriller', 'Sci-Fi', 'MOVIE', 2010, 4.7, 20000, 148),
('The Shawshank Redemption', 'Hope and friendship in prison', 'Drama', 'MOVIE', 1994, 4.9, 30000, 142),
('The Dark Knight', 'Batman faces the Joker', 'Action', 'MOVIE', 2008, 4.8, 25000, 152),
('Pulp Fiction', 'Interconnected crime stories', 'Crime', 'MOVIE', 1994, 4.6, 18000, 154);

-- Sample TV series
INSERT INTO content (title, description, genre, content_type, release_year, average_rating, view_count, seasons, total_episodes, avg_episode_duration) VALUES
('Breaking Bad', 'Chemistry teacher turns to cooking meth', 'Drama', 'TV_SERIES', 2008, 4.9, 50000, 5, 62, 47),
('Stranger Things', 'Kids fight supernatural forces in 80s', 'Sci-Fi', 'TV_SERIES', 2016, 4.6, 40000, 4, 34, 51),
('The Office', 'Mockumentary about office life', 'Comedy', 'TV_SERIES', 2005, 4.7, 35000, 9, 201, 22),
('Game of Thrones', 'Epic fantasy power struggle', 'Fantasy', 'TV_SERIES', 2011, 4.5, 60000, 8, 73, 57);

-- Sample watch history for user 1 (john@example.com)
INSERT INTO watch_history (user_id, content_id, progress_seconds, completed) VALUES
(1, 1, 8160, TRUE),  -- Completed The Matrix
(1, 2, 5000, FALSE), -- Watching Inception
(1, 6, 2820, TRUE);  -- Completed Breaking Bad episode

-- Sample ratings from user 1
INSERT INTO rating (user_id, content_id, rating) VALUES
(1, 1, 5),  -- Rated The Matrix 5 stars
(1, 6, 5);  -- Rated Breaking Bad 5 stars

-- Sample watch history for user 3 (bob@example.com)
INSERT INTO watch_history (user_id, content_id, progress_seconds, completed) VALUES
(3, 3, 8520, TRUE),  -- Completed Shawshank Redemption
(3, 4, 9120, TRUE),  -- Completed The Dark Knight
(3, 7, 3060, FALSE); -- Watching Stranger Things

-- Sample ratings from user 3
INSERT INTO rating (user_id, content_id, rating) VALUES
(3, 3, 5),  -- Rated Shawshank 5 stars
(3, 4, 4);  -- Rated Dark Knight 4 stars
