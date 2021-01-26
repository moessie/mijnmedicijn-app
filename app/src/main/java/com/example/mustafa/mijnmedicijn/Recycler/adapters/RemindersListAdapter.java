package com.example.mustafa.mijnmedicijn.Recycler.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;

import java.util.List;

public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.MyViewHolder>{
    private final List<RemindersModel>remindersList;
    private final NavController parentNavController;

    public RemindersListAdapter(List<RemindersModel> remindersList, NavController parentNavController){
        this.remindersList = remindersList;
        this.parentNavController = parentNavController;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RemindersModel reminder = remindersList.get(position);
        holder.nameTV.setText(reminder.getMedicineName());
        holder.dosageTV.setText(String.format("%s %s", reminder.getDoseQuantity(), reminder.getDoseUnit()));
        String time = reminder.getReminderTime();
        if(time.contains("AM")){
            time = time.replace("AM","");
            holder.ampmTV.setText("AM");
        }
        else {
            time = time.replace("PM","");
            holder.ampmTV.setText("PM");
        }
        holder.timeTV.setText(time);
        int frequency;
        if(reminder.getReminderRepeatInfo().startsWith("Reminder")){
            holder.frequencyTV.setText("Weekdays");
            frequency = 2;
        }
        else if(reminder.getReminderRepeatInfo().startsWith("Repeat")){
            holder.frequencyTV.setText("After X Days");
            frequency = 1;
        }
        else {holder.frequencyTV.setText("Everyday"); frequency = 0;}

        holder.reminderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putInt("reminderID",reminder.get_id());
                extras.putInt("frequency",frequency);
                parentNavController.navigate(R.id.action_navigation_reminders_to_reminderDetailsFragment,extras);
            }
        });
    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView reminderItem;
        TextView nameTV,dosageTV,timeTV,ampmTV,frequencyTV;

        MyViewHolder(View view) {
            super(view);
            reminderItem = view.findViewById(R.id.reminderItem);
            nameTV = view.findViewById(R.id.nameTV);
            ampmTV = view.findViewById(R.id.ampmTV);
            frequencyTV = view.findViewById(R.id.frequencyTV);
            dosageTV = view.findViewById(R.id.dosageTV);
            timeTV = view.findViewById(R.id.timeTV);
        }

    } // View Holder Class End

}
