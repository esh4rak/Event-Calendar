package com.example.eventcalendar.repositories;


import androidx.lifecycle.MutableLiveData;

import com.example.eventcalendar.models.EventItem;

import java.util.ArrayList;
import java.util.UUID;


public class EventRepository {

    private static EventRepository instance;
    private ArrayList<EventItem> eventItemArrayList = new ArrayList<>();

    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }


    // Pretend to get data from a webservice or online source
    public MutableLiveData<ArrayList<EventItem>> getEvents() {
        setEvents();
        MutableLiveData<ArrayList<EventItem>> data = new MutableLiveData<>();
        data.setValue(eventItemArrayList);
        return data;
    }


    private void setEvents() {

        String uniqueID = UUID.randomUUID().toString();

        eventItemArrayList.add(new EventItem(uniqueID, "Party", "22 September 2022", "10:45 AM", "11:45 AM", "Paris"));


        uniqueID = UUID.randomUUID().toString();


        eventItemArrayList.add(new EventItem(uniqueID, "Meeting with Jeeon", "25 September 2022", "10:45 AM", "11:45 AM", "Dhaka"));
    }


}











