package com.example.easyfit;

public class Workout {
    private String name;
    private int duration;
    private int intensity;

    public Workout(String name, int duration, int intensity) {
        this.name = name;
        this.duration = duration;
        this.intensity = intensity;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}