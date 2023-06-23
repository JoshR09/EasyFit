package com.example.easyfit.structures;

public class Set {

    private int reps;

    private int weight;

    private boolean isComplete;

    public Set(int reps, int weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}