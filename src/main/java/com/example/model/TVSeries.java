package com.example.model;

//TV series content with seasons and episodes.
 
public class TVSeries extends Content {
    private int seasons;
    private int totalEpisodes;
    private int averageEpisodeDuration;
    
    public int getSeasons() { return seasons; }
    public void setSeasons(int seasons) { this.seasons = seasons; }
    public int getTotalEpisodes() { return totalEpisodes; }
    public void setTotalEpisodes(int totalEpisodes) { this.totalEpisodes = totalEpisodes; }
    public int getAverageEpisodeDuration() { return averageEpisodeDuration; }
    public void setAverageEpisodeDuration(int averageEpisodeDuration) { this.averageEpisodeDuration = averageEpisodeDuration; }

    @Override
    public String getContentType() {
        return "TV_SERIES";
    }
    
    @Override
    public String getDurationInfo() {
        return seasons + " seasons, " + totalEpisodes + " episodes";
    }
}
