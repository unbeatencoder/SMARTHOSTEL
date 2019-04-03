package com.example.smarthostel;

public class Preference {
    private String currentpreference;

    public Preference(String currentpreference) {
        this.currentpreference = currentpreference;
    }

    public Preference() {
    }

    public String getCurrentpreference() {
        return currentpreference;
    }

    public void setCurrentpreference(String currentpreference) {
        this.currentpreference = currentpreference;
    }
}

