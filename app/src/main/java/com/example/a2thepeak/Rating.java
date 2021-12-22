package com.example.a2thepeak;

public class Rating {
    String username;
    String description;
    double rating;
    int id;

    public Rating() {};

    public Rating(String username, String description, double rating, int id) {
        this.username = username;
        this.description = description;
        this.rating = rating;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
