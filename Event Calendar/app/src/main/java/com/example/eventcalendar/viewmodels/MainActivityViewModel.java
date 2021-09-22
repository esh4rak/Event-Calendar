package com.example.eventcalendar.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.eventcalendar.models.EventItem;
import com.example.eventcalendar.repositories.EventRepository;
import java.util.ArrayList;


public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<ArrayList<EventItem>> eEvents;
    private EventRepository eventRepository;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();




    public void init() {
        if (eEvents != null) {
            return;
        }
        eventRepository = EventRepository.getInstance();
        eEvents = eventRepository.getEvents();
    }


    public void addValue(final EventItem eventItem) {
        mIsUpdating.setValue(true);


        ArrayList<EventItem> currentEvents = eEvents.getValue();
        currentEvents.add(eventItem);
        eEvents.postValue(currentEvents);
        mIsUpdating.postValue(false);

    }


    public LiveData<ArrayList<EventItem>> getEvents() {
        return eEvents;
    }


    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }


}
