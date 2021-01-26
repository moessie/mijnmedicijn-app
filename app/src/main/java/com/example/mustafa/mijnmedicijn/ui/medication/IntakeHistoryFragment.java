package com.example.mustafa.mijnmedicijn.ui.medication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.adapters.IntakeHistoryAdapter;
import com.example.mustafa.mijnmedicijn.Recycler.models.IntakeHistoryRVModel;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class IntakeHistoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "medicineName";

    private String medicineName;

    public IntakeHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            medicineName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_intake_history, container, false);
        if(medicineName!=null){
            final TextView title = view.findViewById(R.id.medNameTV);
            title.setText(medicineName);
            final List<MedicationTrackerModel> medicationRecord = reminderDB.getMedicationTrackerDao().loadSelected(medicineName);
            if(medicationRecord!=null && medicationRecord.size()>0){
                final List<String>dateTracker = new ArrayList<>();
                final RecyclerView medHistoryRV = view.findViewById(R.id.medHistoryRV);
                final List<IntakeHistoryRVModel>historyList = new ArrayList<>();
                final IntakeHistoryAdapter adapter = new IntakeHistoryAdapter(historyList);
                medHistoryRV.setLayoutManager(new LinearLayoutManager(requireActivity()));

                for(MedicationTrackerModel med : medicationRecord){
                    if(!dateTracker.contains(med.getDateStr())){
                        dateTracker.add(med.getDateStr());
                    }
                }
                /// NOW WE HAVE ALL THE DATES WHEN selected MEDICINE was taken, we just need to load these

                for(String date : dateTracker){
                    final List<MedicationTrackerModel> selectedRecord = reminderDB.getMedicationTrackerDao().loadSelectedDate(medicineName,date);
                    StringBuilder intakeHistory = new StringBuilder();
                    for(MedicationTrackerModel obj : selectedRecord){
                        intakeHistory.append(obj.getTimeStr()).append(" - ").append(obj.getIntakeQuantity()).append("\n");
                    }
                    historyList.add(new IntakeHistoryRVModel(date, intakeHistory.toString()));
                }

                medHistoryRV.setAdapter(adapter);
            }
        }

        return view;
    }
}