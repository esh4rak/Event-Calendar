package com.example.eventcalendar.views.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventcalendar.R;
import com.example.eventcalendar.models.EventItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<EventItem> eEventItems;
    private OnItemClickListener eListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        eListener = listener;
    }


    public EventAdapter(ArrayList<EventItem> eventItems) {
        eEventItems = eventItems;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view, eListener);
    }


    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        EventItem currentItem = eEventItems.get(position);

        holder.eventNameTV.setText(currentItem.getEventName());
        holder.startTimeTV.setText(currentItem.getStartTime());
        holder.endTimeTV.setText(currentItem.getEndTime());
        holder.locationTV.setText(currentItem.getLocation());
        holder.dateTV.setText(currentItem.getDate());

    }

    @Override
    public int getItemCount() {
        return eEventItems.size();
    }


    public void updateData(ArrayList<EventItem> items) {
        eEventItems.clear();
        eEventItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(int position, EventItem viewModel) {
        eEventItems.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        eEventItems.remove(position);
        notifyItemRemoved(position);
    }


    public interface OnItemClickListener {

        void onItemClick(int position, View v);

        void onEditButtonClick(int position, View v);
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {


        MaterialCardView parentView;
        TextView eventNameTV;
        TextView startTimeTV;
        TextView endTimeTV;
        TextView locationTV;
        TextView dateTV;
        Button editButton;

        public EventViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            parentView = itemView.findViewById(R.id.itemEvent_CardView);
            eventNameTV = itemView.findViewById(R.id.itemEvent_eventName);
            dateTV = itemView.findViewById(R.id.itemEvent_date);
            startTimeTV = itemView.findViewById(R.id.itemEvent_startTimeTextView);
            endTimeTV = itemView.findViewById(R.id.itemEvent_endTimeTextView);
            locationTV = itemView.findViewById(R.id.itemEvent_locationTextView);
            editButton = itemView.findViewById(R.id.itemEvent_editButton);


            parentView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pos, v);
                }
            });


            editButton.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onEditButtonClick(pos, v);
                }
            });


        }
    }
}
