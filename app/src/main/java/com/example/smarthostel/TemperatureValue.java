package com.example.smarthostel;

public class TemperatureValue {
    private String Current;

    public TemperatureValue(String current) {
        Current = current;
    }

    public TemperatureValue() {

    }

    public String getCurrent() {
        return Current;
    }

    public void setCurrent(String current) {
        Current = current;
    }
}
