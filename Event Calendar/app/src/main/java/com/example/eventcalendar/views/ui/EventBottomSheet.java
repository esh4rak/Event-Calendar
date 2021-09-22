package com.example.eventcalendar.views.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.eventcalendar.R;
import com.example.eventcalendar.databinding.BottomSheetEventBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class EventBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetListener bottomSheetListener;
    private BottomSheetEventBinding binding;

    private String eventName;
    private String startTime;
    private String endTime;
    private String location;
    private String date;
    private String addOrUpdate;
    private int position;

    public EventBottomSheet(String eventName, String startTime, String endTime, String location, String date, String addOrUpdate, int position) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.date = date;
        this.addOrUpdate = addOrUpdate;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = BottomSheetEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();

        return view;
    }


    @SuppressLint("SetTextI18n")
    private void init() {

        //event Name
        binding.eventNameLayout.getEditText().setText(eventName);

        //start time
        binding.startTimeLayout.getEditText().setText(startTime);

        //end time
        binding.endTimeLayout.getEditText().setText(endTime);

        //location
        binding.locationLayout.getEditText().setText(location);

        //Date
        binding.dateTextView.setText(date);


        if (addOrUpdate.equals("add")) {
            binding.saveButton.setText("Save");
        } else if (addOrUpdate.equals("update")) {
            binding.saveButton.setText("Update");
        }


        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventName = binding.eventNameLayout.getEditText().getText().toString().trim();
                startTime = binding.startTimeEditText.getText().toString().trim();
                endTime = binding.endTimeEditText.getText().toString().trim();
                location = binding.locationEditText.getText().toString().trim();
                date = binding.dateTextView.getText().toString().trim();


                if (!eventName.isEmpty()) {
                    bottomSheetListener.onSaveButtonClick(eventName, startTime, endTime, location, date, addOrUpdate, position);
                    dismiss();
                }else {
                    Toast.makeText(getContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }

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
        void onSaveButtonClick(String EventName, String StartTime, String EndTime, String Location, String Date, String AddOrUpdate, int Position);

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

