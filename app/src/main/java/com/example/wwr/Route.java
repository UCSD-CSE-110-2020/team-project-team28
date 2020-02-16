package com.example.wwr;

public class Route {
    private String name;
    private String startLocation;
    private long totalSteps;
    private long totalMiles;
    private long totalSeconds;
    private String note;
    private boolean isFavorite;
    private int image;

    Route (String name, String startLocation, long totalSteps, long totalMiles, long totalSeconds,
           String note, boolean isFavorite, int image) {
        this.name = name;
        this.startLocation = startLocation;

        this.totalSteps = totalSteps;
        this.totalMiles = totalMiles;
        this.totalSeconds = totalSeconds;
        this.note = note;
        this.isFavorite = isFavorite;
        this.image = image;
    }

    public void updateSteps(long steps) {
        this.totalSteps = steps;
    }

    public void updateSeconds(long seconds) {
        this.totalSeconds = seconds;
    }

    public void updateMiles(long miles) {
        this.totalMiles = miles;
    }

    public String getName() {
        return this.name;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public long getSteps() {
        return this.totalSteps;
    }

    public long getTotalMiles() {
        return this.totalMiles;
    }

    public long getTotalSeconds() {
        return this.totalSeconds;
    }

    public int getImage() {
        return this.image;
    }

    public String getNote() {
        return note;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
