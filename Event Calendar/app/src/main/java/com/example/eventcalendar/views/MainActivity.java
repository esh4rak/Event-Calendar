package com.example.eventcalendar.views;


import static com.example.eventcalendar.utils.CalendarUtils.daysInWeekArray;
import static com.example.eventcalendar.utils.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.eventcalendar.R;
import com.example.eventcalendar.databinding.ActivityMainBinding;
import com.example.eventcalendar.models.EventItem;
import com.example.eventcalendar.models.SignInUser;
import com.example.eventcalendar.utils.CalendarUtils;
import com.example.eventcalendar.viewmodels.MainActivityViewModel;
import com.example.eventcalendar.adapters.CalendarAdapter;
import com.example.eventcalendar.adapters.EventAdapter;
import com.example.eventcalendar.viewmodels.SignInViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements EventBottomSheet.BottomSheetListener {


    private ActivityMainBinding binding;

    private ArrayList<LocalDate> days;
    private ArrayList<EventItem> eventItemArrayList;

    private EventAdapter eventAdapter;

    private MainActivityViewModel mainActivityViewModel;

    private GoogleSignInClient googleSignInClient;


    private SignInViewModel signInViewModel;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        CalendarUtils.selectedDate = LocalDate.now();
        firebaseAuth = FirebaseAuth.getInstance();

        getUserInfo();
        initGoogleSignInClient();
        init();

    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);

    }

    private void getUserInfo() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.
                getInstance(this.getApplication())).get(SignInViewModel.class);
        signInViewModel.collectUserInfo();
        signInViewModel.collectUserInfoLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                setProfile(signInUser);
            }
        });

    }

    private void setProfile(SignInUser signInUser) {
        if(signInUser != null){
            Glide.with(getApplicationContext()).load(signInUser.getImageUrl())
                    .centerCrop().placeholder(R.drawable.ic_person_24).into(binding.profileImageId);
            binding.profileNameId.setText(signInUser.getName());
            binding.profileEmailId.setText(signInUser.getEmail());
        }

    }

    private void init() {

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mainActivityViewModel.init();


        mainActivityViewModel.getEvents().observe(this, eventItems -> {

            eventAdapter.updateData(eventItems);

        });

        mainActivityViewModel.getIsUpdating().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressBar();
            } else {
                hideProgressBar();
                binding.eventRecyclerView.smoothScrollToPosition(mainActivityViewModel.getEvents().getValue().size() - 1);
            }
        });


        binding.previousWeekButton.setOnClickListener(view -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
            setEventAdapter();
        });

        binding.nextWeekButton.setOnClickListener(view -> {

            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();
            setEventAdapter();

        });

        binding.floatingActionButton.setOnClickListener(v -> {

            EventBottomSheet bottomSheet = new EventBottomSheet(
                    null,
                    null,
                    null,
                    null,
                    CalendarUtils.formattedDate(CalendarUtils.selectedDate),
                    "add",
                    0);
            bottomSheet.show(getSupportFragmentManager(), "playBottomSheetAllergies");

        });


        binding.signOutButtonId.setOnClickListener(view -> {
            signOut();
        });


        setWeekView();
        setEventAdapter();


    }


    private void setWeekView() {

        binding.monthYearTextView.setText(monthYearFromDate(CalendarUtils.selectedDate));
        days = daysInWeekArray(CalendarUtils.selectedDate);

        binding.calendarRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);


        calendarAdapter.setOnItemClickListener((position, v) -> {

            CalendarUtils.selectedDate = days.get(position);
            setWeekView();
            setEventAdapter();

        });


    }

    private void setEventAdapter() {


        eventItemArrayList = new ArrayList<>();

        binding.eventRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventAdapter = new EventAdapter(eventItemArrayList);
        binding.eventRecyclerView.setLayoutManager(layoutManager);
        binding.eventRecyclerView.setAdapter(eventAdapter);


        eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                //ToDo
            }

            @Override
            public void onEditButtonClick(int position, View v) {

                PopupMenu popup = new PopupMenu(MainActivity.this, v, Gravity.END);
                popup.inflate(R.menu.card_threedot);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.cardThreeDot_delete) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Do you want to delete this item ?").setCancelable(false)
                                .setPositiveButton("Yes", (dialog, which) -> DeleteItem(position))
                                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                        AlertDialog alert = builder.create();
                        alert.setTitle("Delete");
                        alert.show();
                    } else if (item.getItemId() == R.id.cardThreeDot_edit) {

                        EventBottomSheet bottomSheet = new EventBottomSheet(
                                eventItemArrayList.get(position).getEventName(),
                                eventItemArrayList.get(position).getStartTime(),
                                eventItemArrayList.get(position).getEndTime(),
                                eventItemArrayList.get(position).getLocation(),
                                eventItemArrayList.get(position).getDate(),
                                "update",
                                position);
                        bottomSheet.show(getSupportFragmentManager(), "playBottomSheetAllergies");

                    }
                    return false;
                });

                popup.show();

            }
        });


    }


    private void DeleteItem(int position) {
        //Todo
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onSaveButtonClick(String EventName, String StartTime, String EndTime, String Location, String Date, String AddOrUpdate, int Position) {

        String uniqueID = UUID.randomUUID().toString();


        mainActivityViewModel.addValue(
                new EventItem(
                        uniqueID,
                        EventName,
                        Date,
                        StartTime,
                        EndTime,
                        Location
                )
        );


    }

    private void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut();
        Intent intent= new Intent(getApplicationContext(),SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }
}