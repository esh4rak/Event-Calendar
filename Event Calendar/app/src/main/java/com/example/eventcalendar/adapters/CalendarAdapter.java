package com.example.eventcalendar.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventcalendar.R;
import com.example.eventcalendar.views.utils.CalendarUtils;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDate;
import java.util.ArrayList;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final ArrayList<LocalDate> days;
    private OnItemClickListener cListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        cListener = listener;
    }


    public CalendarAdapter(ArrayList<LocalDate> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new CalendarViewHolder(view, cListener);
    }


    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        final LocalDate date = days.get(position);
        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(CalendarUtils.selectedDate))
                holder.parentView.setCardBackgroundColor(Color.LTGRAY);
        }

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemClickListener {

        void onItemClick(int position, View v);
    }


    public static class CalendarViewHolder extends RecyclerView.ViewHolder {

        TextView dayOfMonth;
        MaterialCardView parentView;

        public CalendarViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            parentView = itemView.findViewById(R.id.itemCalendar);
            dayOfMonth = itemView.findViewById(R.id.itemCalendarText);

            parentView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pos, v);
                }
            });


        }
    }
}
