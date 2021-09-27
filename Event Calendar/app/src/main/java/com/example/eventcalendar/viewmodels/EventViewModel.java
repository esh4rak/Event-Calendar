package com.example.eventcalendar.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventcalendar.models.EventItem;
import com.example.eventcalendar.repositories.EventRepository;

import java.util.ArrayList;
import java.util.List;


public class EventViewModel extends ViewModel {


    private EventRepository eventRepository;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    public LiveData<String> insertResultLiveData;
    public LiveData<ArrayList<EventItem>> getEventLiveData;


    public void init() {
        if (getEventLiveData != null) {
            return;
        }
        eventRepository = EventRepository.getInstance();
    }


    public void addValue(final EventItem eventItem) {
        mIsUpdating.setValue(true);

        insertResultLiveData = eventRepository.insertEventFirebase(eventItem);

        mIsUpdating.postValue(false);

    }


    public void show() {
        getEventLiveData = eventRepository.getDataFromFireStore();
    }


    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }


}
