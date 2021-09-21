package com.example.eventcalendar.views;

import static com.example.eventcalendar.views.utils.CalendarUtils.daysInWeekArray;
import static com.example.eventcalendar.views.utils.CalendarUtils.monthYearFromDate;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.example.eventcalendar.adapters.CalendarAdapter;
import com.example.eventcalendar.databinding.ActivityMainBinding;
import com.example.eventcalendar.views.utils.CalendarUtils;
import java.time.LocalDate;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private ArrayList<LocalDate> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        CalendarUtils.selectedDate = LocalDate.now();
        init();
        setWeekView();
    }


    private void init() {


        binding.previousWeekButton.setOnClickListener(view -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
        });


        binding.nextWeekButton.setOnClickListener(view -> {

            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();

        });

    }


    private void setWeekView() {

        binding.monthYearTextView.setText(monthYearFromDate(CalendarUtils.selectedDate));
        days = daysInWeekArray(CalendarUtils.selectedDate);

        binding.calendarRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        CalendarAdapter adapter = new CalendarAdapter(days);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener((position, v) -> {

            CalendarUtils.selectedDate = days.get(position);
            setWeekView();

        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


}