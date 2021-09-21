package com.example.eventcalendar.models;


import java.io.Serializable;

public class EventItem implements Serializable {

    private String id;
    private String eventName;
    private String date;
    private String startTime;
    private String endTime;
    private String location;


    public EventItem(String id, String eventName, String date, String startTime, String endTime, String location) {
        this.id = id;
        this.eventName = eventName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
