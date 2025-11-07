package com.example.factory;

import com.example.model.Content;
import com.example.model.Movie;
import java.time.LocalDateTime;


//builds Movie objects from provided fields.

public class MovieFactory implements ContentFactory {
    private String title;
    private String description;
    private String genre;
    private int releaseYear;
    private int durationMinutes;
    
    public MovieFactory(String title, String description, String genre, int releaseYear, int durationMinutes) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.durationMinutes = durationMinutes;
    }
    
    @Override
    public Content createContent() {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setGenre(genre);
        movie.setReleaseYear(releaseYear);
        movie.setDurationMinutes(durationMinutes);
        movie.setAverageRating(0.0);
        movie.setViewCount(0);
        movie.setCreatedAt(LocalDateTime.now());
        return movie;
    }
}
