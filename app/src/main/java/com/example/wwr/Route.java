package com.example.wwr;

public class Route {

    private String name;
    private String startLocation;
    private int totalSteps;
    private double totalMiles;
    private int totalMinutes;
    private int image;

    Route (String name, String startLocation, int totalSteps, double totalMiles, int totalMinutes, int image) {
        this.name = name;
        this.startLocation = startLocation;
        this.totalSteps = totalSteps;
        this.totalMiles = totalMiles;
        this.totalMinutes = totalMinutes;
        this.image = image;
    }

    public void updateRouteName(String name) {
        this.name = name;
    }

    public void updateStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public void updateSteps(int steps) {
        this.totalSteps = steps;
    }

    public void updateMinutes(int minutes) {
        this.totalMinutes = minutes;
    }

    public void updateMiles(double miles) {
        this.totalMiles = miles;
    }

    public String getName() {
        return this.name;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public int getSteps() {
        return this.totalSteps;
    }

    public double getMiles() {
        return this.totalMiles;
    }

    public int getTotalMinutes() {
        return this.totalMinutes;
    }

    public int getImage() {
        return this.image;
    }
}
