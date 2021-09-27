package com.example.eventcalendar.repositories;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.eventcalendar.models.EventItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class EventRepository {

    private static EventRepository instance;
    private ArrayList<EventItem> eventItemArrayList = new ArrayList<>();


    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();



    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }


    // Pretend to get data from a webservice or online source
    public MutableLiveData<ArrayList<EventItem>> getEvents() {
        //setEvents();
        MutableLiveData<ArrayList<EventItem>> data = new MutableLiveData<>();
        data.setValue(eventItemArrayList);
        return data;
    }


    /*private void setEvents() {

        String uniqueID = UUID.randomUUID().toString();

        eventItemArrayList.add(new EventItem(uniqueID, "Party", "22 September 2022", "10:45 AM", "11:45 AM", "Paris"));

        uniqueID = UUID.randomUUID().toString();

        eventItemArrayList.add(new EventItem(uniqueID, "Meeting with Jeeon", "25 September 2022", "10:45 AM", "11:45 AM", "Dhaka"));
    }*/


    public MutableLiveData<String> insertEventFirebase(final EventItem eventItem){

        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        final MutableLiveData<String> insertResultLiveData= new MutableLiveData<>();

        Map<String,String> contactMap= new HashMap<>();
        contactMap.put("event_id",eventItem.id);
        contactMap.put("event_name",eventItem.eventName);
        contactMap.put("event_start_time",eventItem.startTime);
        contactMap.put("event_end_time",eventItem.endTime);
        contactMap.put("event_date",eventItem.date);
        contactMap.put("event_location",eventItem.location);

        //now put this data in firebase....
        firebaseFirestore.collection("EventList").document(currentUser).collection("Event")
                .document(eventItem.id).set(contactMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        insertResultLiveData.setValue("Upload Successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        insertResultLiveData.setValue(e.toString());
                    }
                });


        return  insertResultLiveData;
    }


    public MutableLiveData<ArrayList<EventItem>> getDataFromFireStore(){
        String currentUser= firebaseAuth.getCurrentUser().getUid();
        final ArrayList<EventItem> eventList= new ArrayList<>();
        final MutableLiveData<ArrayList<EventItem>> getFireStoreMutableLiveData= new MutableLiveData<>();
        firebaseFirestore.collection("EventList").document(currentUser).collection("Event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                eventList.clear();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    String id= documentSnapshot.getString("event_id");
                    String eventName= documentSnapshot.getString("event_name");
                    String eventStartTime= documentSnapshot.getString("event_start_time");
                    String eventEndTime= documentSnapshot.getString("event_end_time");
                    String date= documentSnapshot.getString("event_date");
                    String location= documentSnapshot.getString("event_location");
                    EventItem eventItem= new EventItem(id,eventName,date,eventStartTime,eventEndTime,location);
                    eventList.add(eventItem);
                }
                getFireStoreMutableLiveData.setValue(eventList);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        return getFireStoreMutableLiveData;
    }



}











