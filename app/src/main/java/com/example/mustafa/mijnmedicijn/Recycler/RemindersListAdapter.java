package com.example.mustafa.mijnmedicijn.Recycler;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;

import java.util.List;

public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.MyViewHolder>{
    private List<RemindersModel>remindersList;

    public RemindersListAdapter(List<RemindersModel> remindersList){
        this.remindersList = remindersList;
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
        if(reminder.getReminderRepeatInfo().startsWith("Reminder")){
            holder.frequencyTV.setText("Weekdays");
        }
        else if(reminder.getReminderRepeatInfo().startsWith("Repeat")){
            holder.frequencyTV.setText("After X Days");
        }
        else {holder.frequencyTV.setText("Everyday");}

        holder.reminderItem.setOnClickListener(v->{
            //TODO : Goto reminder details Frag
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
