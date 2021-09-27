package com.example.eventcalendar.repositories;


import androidx.lifecycle.MutableLiveData;

import com.example.eventcalendar.models.EventItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventRepository {

    private static EventRepository instance;


    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }


    //add event
    public MutableLiveData<String> insertEventFirebase(final EventItem eventItem) {

        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        final MutableLiveData<String> insertResultLiveData = new MutableLiveData<>();

        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("event_id", eventItem.id);
        contactMap.put("event_name", eventItem.eventName);
        contactMap.put("event_start_time", eventItem.startTime);
        contactMap.put("event_end_time", eventItem.endTime);
        contactMap.put("event_date", eventItem.date);
        contactMap.put("event_location", eventItem.location);


        firebaseFirestore.collection("EventList").document(currentUser).collection("Event")
                .document(eventItem.id).set(contactMap)
                .addOnSuccessListener(aVoid -> insertResultLiveData.setValue("Upload Successfully"))
                .addOnFailureListener(e -> insertResultLiveData.setValue(e.toString()));


        return insertResultLiveData;
    }


    //get events
    public MutableLiveData<ArrayList<EventItem>> getDataFromFireStore(String sDate) {
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        final ArrayList<EventItem> eventList = new ArrayList<>();
        final MutableLiveData<ArrayList<EventItem>> getFireStoreMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("EventList").document(currentUser).collection("Event").get().addOnCompleteListener(task -> {
            eventList.clear();
            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                String id = documentSnapshot.getString("event_id");
                String eventName = documentSnapshot.getString("event_name");
                String eventStartTime = documentSnapshot.getString("event_start_time");
                String eventEndTime = documentSnapshot.getString("event_end_time");
                String date = documentSnapshot.getString("event_date");
                String location = documentSnapshot.getString("event_location");

                assert date != null;
                if (date.equals(sDate)) {
                    EventItem eventItem = new EventItem(id, eventName, date, eventStartTime, eventEndTime, location);
                    eventList.add(eventItem);
                }


            }
            getFireStoreMutableLiveData.setValue(eventList);

        }).addOnFailureListener(e -> {

        });


        return getFireStoreMutableLiveData;
    }


    //update event
    public void updateInfoFirebase(EventItem eventItem) {
        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("EventList").document(currentUser)
                .collection("Event").document(eventItem.id)
                .update("event_name", eventItem.eventName,
                        "event_start_time", eventItem.startTime,
                        "event_end_time", eventItem.endTime,
                        "event_date", eventItem.date,
                        "event_location", eventItem.location).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> {

        });
    }

    //delete event
    public void deleteDataFirebase(final String id) {
        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("EventList").document(currentUser).collection("Event")
                .document(id).delete().addOnSuccessListener(aVoid -> {

        });

    }


}











