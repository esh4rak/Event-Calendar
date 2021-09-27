package com.example.eventcalendar.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.eventcalendar.models.EventItem;
import com.example.eventcalendar.repositories.EventRepository;
import java.util.ArrayList;


public class EventViewModel extends ViewModel {


    private EventRepository eventRepository;

    public LiveData<String> insertResultLiveData;
    public LiveData<ArrayList<EventItem>> getEventLiveData;


    public void init() {
        if (getEventLiveData != null) {
            return;
        }
        eventRepository = EventRepository.getInstance();
    }


    public void addValue(final EventItem eventItem) {

        insertResultLiveData = eventRepository.insertEventFirebase(eventItem);
    }


    public void show(String date) {
        getEventLiveData = eventRepository.getDataFromFireStore(date);
    }



}
