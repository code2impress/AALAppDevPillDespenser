package com.example.aal_appdev_pilldespenser;

public class Patient {
    private String name;
    private int mondayValue;
    private int tuesdayValue;
    private int wednesdayValue;
    private int thursdayValue;
    private int fridayValue;
    private int saturdayValue;
    private int sundayValue;

    // Constructor
    public Patient(String name, int mondayValue, int tuesdayValue, int wednesdayValue, int thursdayValue, int fridayValue, int saturdayValue, int sundayValue) {
        this.name = name;
        this.mondayValue = mondayValue;
        this.tuesdayValue = tuesdayValue;
        this.wednesdayValue = wednesdayValue;
        this.thursdayValue = thursdayValue;
        this.fridayValue = fridayValue;
        this.saturdayValue = saturdayValue;
        this.sundayValue = sundayValue;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getMondayValue() {
        return mondayValue;
    }

    public int getTuesdayValue() {
        return tuesdayValue;
    }

    public int getWednesdayValue() {
        return wednesdayValue;
    }

    public int getThursdayValue() {
        return thursdayValue;
    }

    public int getFridayValue() {
        return fridayValue;
    }

    public int getSaturdayValue() {
        return saturdayValue;
    }

    public int getSundayValue() {
        return sundayValue;
    }

    // Setters (if needed)
    // ...
}
