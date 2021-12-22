package com.example.a2thepeak;

public class Hike {
    private int _id;
    private String title;
    private String region;
    private int difficulty;
    private double rating;
    private int ratingCount;
    private String length;
    private String time;
    private int elevationGain;
    private String routeType;
    private String description;

    public Hike() {}

    public Hike(int _id, String title, String region, int difficulty, double rating, int ratingCount, String length, String time, int elevationGain, String routeType, String description) {
        this._id = _id;
        this.title = title;
        this.region = region;
        this.difficulty = difficulty;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.length = length;
        this.time = time;
        this.elevationGain = elevationGain;
        this.routeType = routeType;
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(int elevationGain) {
        this.elevationGain = elevationGain;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
