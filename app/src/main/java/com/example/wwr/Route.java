package com.example.wwr;

public class Route {
    private String userName;
    private String userEmail;
    private String name;
    private String startLocation;
    private long totalSteps;
    private double totalMiles;
    private long totalSeconds;
    private String flatOrHilly;
    private String loopOrOut;
    private String streetOrTrail;
    private String surface;
    private String difficulty;
    private String note;
    private boolean isFavorite;
    private int image;

    Route (String userName, String userEmail, String name, String startLocation, long totalSteps, double totalMiles, long totalSeconds,
           String flatOrHilly, String loopOrOut, String streetOrTrail, String surface,
           String difficulty, String note, boolean isFavorite, int image) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.name = name;
        this.startLocation = startLocation;
        this.totalSteps = totalSteps;
        this.totalMiles = totalMiles;
        this.totalSeconds = totalSeconds;
        this.flatOrHilly = flatOrHilly;
        this.loopOrOut = loopOrOut;
        this.streetOrTrail = streetOrTrail;
        this.surface = surface;
        this.difficulty = difficulty;
        this.surface = surface;
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

    public void updateMiles(double miles) {
        this.totalMiles = miles;
    }

    public String getUserName() { return this.userName; }

    public String getUserEmail() { return this.userEmail; }

    public String getName() {
        return this.name;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public long getSteps() {
        return this.totalSteps;
    }

    public double getTotalMiles() {
        return this.totalMiles;
    }

    public long getTotalSeconds() {
        return this.totalSeconds;
    }

    public int getImage() {
        return this.image;
    }

    public String getFlatOrHilly() { return this.flatOrHilly; }

    public String getLoopOrOut() { return this.loopOrOut; }

    public String getStreetOrTrail() { return this.streetOrTrail; }

    public String getSurface() { return this.surface; }

    public String getDifficulty() { return this.difficulty; }

    public String getNote() {
        return note;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
