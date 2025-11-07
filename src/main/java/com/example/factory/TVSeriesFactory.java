package com.example.factory;

import com.example.model.Content;
import com.example.model.TVSeries;
import java.time.LocalDateTime;

//builds TVSeries objects from provided fields

public class TVSeriesFactory implements ContentFactory {
    private String title;
    private String description;
    private String genre;
    private int releaseYear;
    private int seasons;
    private int totalEpisodes;
    private int averageEpisodeDuration;
    
    public TVSeriesFactory(String title, String description, String genre, int releaseYear, 
                          int seasons, int totalEpisodes, int averageEpisodeDuration) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.seasons = seasons;
        this.totalEpisodes = totalEpisodes;
        this.averageEpisodeDuration = averageEpisodeDuration;
    }
    
    @Override
    public Content createContent() {
        TVSeries series = new TVSeries();
        series.setTitle(title);
        series.setDescription(description);
        series.setGenre(genre);
        series.setReleaseYear(releaseYear);
        series.setSeasons(seasons);
        series.setTotalEpisodes(totalEpisodes);
        series.setAverageEpisodeDuration(averageEpisodeDuration);
        series.setAverageRating(0.0);
        series.setViewCount(0);
        series.setCreatedAt(LocalDateTime.now());
        return series;
    }
}
