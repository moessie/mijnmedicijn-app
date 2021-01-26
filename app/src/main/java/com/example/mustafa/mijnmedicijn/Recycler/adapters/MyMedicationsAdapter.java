package com.example.mustafa.mijnmedicijn.Recycler.adapters;

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

import java.util.List;

public class MyMedicationsAdapter extends RecyclerView.Adapter<MyMedicationsAdapter.MyViewHolder>{
    private List<String>medsList;
    private NavController parentNavController;

    public MyMedicationsAdapter(List<String> namesList, NavController navController) {
        this.medsList = namesList;
        this.parentNavController = navController;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_medicines_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(medsList.get(position));

        holder.item.setOnClickListener(v->{
            Bundle extras = new Bundle();
            extras.putString("medicineName",medsList.get(position));
            parentNavController.navigate(R.id.action_medication_to_intakeHistoryFragment,extras);
        });
    }

    @Override
    public int getItemCount() {
        return medsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView item;
        TextView title;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.medicineTitleTV);
            item = view.findViewById(R.id.medicineCard);
        }

    } // View Holder Class End

}
