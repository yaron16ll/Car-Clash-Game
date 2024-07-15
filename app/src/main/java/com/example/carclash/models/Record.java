package com.example.carclash.models;

public class Record {
    private String name;
    private int score;
    private double latitude;
    private double longitude;

    public Record() {
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public Record setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Record setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
