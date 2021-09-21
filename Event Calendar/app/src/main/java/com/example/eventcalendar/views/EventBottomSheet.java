package com.example.eventcalendar.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventcalendar.R;
import com.example.eventcalendar.databinding.BottomSheetEventBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class EventBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetListener bottomSheetListener;
    private BottomSheetEventBinding binding;

    private String eventName;
    private String startTime;
    private String endTime;
    private String location;
    private String addOrUpdate;
    private int position;

    public EventBottomSheet(String eventName, String startTime, String endTime, String location, String addOrUpdate, int position) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.addOrUpdate = addOrUpdate;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = BottomSheetEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();

        return view;
    }


    private void init() {

        //event Name
        binding.eventNameLayout.getEditText().setText(eventName);

        //start time
        binding.startTimeLayout.getEditText().setText(startTime);

        //end time
        binding.endTimeLayout.getEditText().setText(endTime);


        //location
        binding.locationLayout.getEditText().setText(location);


        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventName = binding.eventNameLayout.getEditText().getText().toString().trim();
                startTime = binding.startTimeEditText.getText().toString().trim();
                endTime = binding.endTimeEditText.getText().toString().trim();
                location = binding.locationEditText.getText().toString().trim();


                if (!eventName.isEmpty()) {
                    bottomSheetListener.onSaveButtonClick(eventName, startTime, endTime, location, addOrUpdate, position);
                }
                dismiss();
            }
        });


        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    public interface BottomSheetListener {
        void onSaveButtonClick(String EventName, String StartTime, String EndTime, String Location, String AddOrUpdate, int Position);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            bottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must Implement BottomSheetListener");
        }
    }


}

