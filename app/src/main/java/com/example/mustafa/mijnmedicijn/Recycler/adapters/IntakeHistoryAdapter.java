package com.example.mustafa.mijnmedicijn.Recycler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.models.IntakeHistoryRVModel;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;
import java.util.List;

public class IntakeHistoryAdapter extends RecyclerView.Adapter<IntakeHistoryAdapter.MyViewHolder>{
    private List<IntakeHistoryRVModel>historyList;

    public IntakeHistoryAdapter(List<IntakeHistoryRVModel> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_track_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dateTV.setText(historyList.get(position).getDate());
        holder.intakeHistoryTV.setText(historyList.get(position).getIntakeHistory());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV,intakeHistoryTV;

        MyViewHolder(View view) {
            super(view);
            dateTV = view.findViewById(R.id.dateTV);
            intakeHistoryTV = view.findViewById(R.id.intakeHistoryTV);
        }

    } // View Holder Class End

}
