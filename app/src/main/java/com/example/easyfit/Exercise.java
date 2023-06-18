package com.example.easyfit;

import java.util.ArrayList;

public class Exercise {
    private String name;

    private int sets;

    private boolean isLogged;

    private ArrayList<Set> setList = new ArrayList<>();

    public Exercise(String name, int sets) {
        this.name = name;
        this.sets = sets;
        for (int i = 0; i < sets; i++) {
            this.setList.add(new Set(0, 0));
        }
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

    public ArrayList<Set> getSetList() {
        return setList;
    }

    public void setSetList(ArrayList<Set> setList) {
        this.setList = setList;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

}