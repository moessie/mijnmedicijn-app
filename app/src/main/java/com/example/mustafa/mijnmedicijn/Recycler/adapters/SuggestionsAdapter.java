package com.example.mustafa.mijnmedicijn.Recycler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mustafa.mijnmedicijn.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.MyViewHolder>{
    private List<String>suggestionsList;
    private TextInputEditText searchText;

    public SuggestionsAdapter(List<String> suggestionsList, TextInputEditText searchText){
        this.suggestionsList = suggestionsList;
        this.searchText = searchText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_suggestion_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.suggestionName.setText(suggestionsList.get(position));
        holder.suggestionItem.setOnClickListener(v->{
            searchText.setText(holder.suggestionName.getText());
            searchText.setSelection(Objects.requireNonNull(searchText.getText()).length());
            suggestionsList.clear();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return suggestionsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView suggestionItem;
        TextView suggestionName;

        MyViewHolder(View view) {
            super(view);
            suggestionItem = view.findViewById(R.id.suggestionItem);
            suggestionName = view.findViewById(R.id.suggestionNameTV);
        }

    } // View Holder Class End

}
