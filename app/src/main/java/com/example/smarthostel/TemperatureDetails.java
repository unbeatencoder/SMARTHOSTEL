package com.example.smarthostel;

public class TemperatureDetails {
    private String Time;
    private String Value;

    public TemperatureDetails(String time, String value) {
        Time = time;
        Value = value;
    }
    public TemperatureDetails(){

    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
