package com.example.easyfit;

public class Exercise {
    private String name;
    private int sets;


    public Exercise(String name, int sets) {
        this.name = name;
        this.sets = sets;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

}