package com.example.smarthostel;
public class UserDetails {
    String emailID;
    String studentID;
    String floor;

    public UserDetails(String emailID, String studentID, String floor) {
        this.emailID = emailID;
        this.studentID = studentID;
        this.floor = floor;
    }
    public UserDetails(){

    }
    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
