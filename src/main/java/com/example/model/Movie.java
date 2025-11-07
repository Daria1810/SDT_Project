package com.example.model;

//movie content with a single total duration.

public class Movie extends Content {
    private int durationMinutes;
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    @Override
    public String getContentType() {
        return "MOVIE";
    }
    
    @Override
    public String getDurationInfo() {
        return durationMinutes + " minutes";
    }
}
