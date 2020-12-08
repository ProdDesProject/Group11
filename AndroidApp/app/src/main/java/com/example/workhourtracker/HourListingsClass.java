package com.example.workhourtracker;

public class HourListingsClass {

    private String startTime, endTime, description, id;

    //HourListingClass Includes Get and set methods to ListView, that have user hour posts for the day

    public HourListingsClass(String startTime, String endTime, String description, String id){
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setDescription(description);
        this.setId(id);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
