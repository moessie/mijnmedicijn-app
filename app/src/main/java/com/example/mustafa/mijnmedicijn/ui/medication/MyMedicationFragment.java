package com.example.mustafa.mijnmedicijn.ui.medication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.adapters.MyMedicationsAdapter;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class MyMedicationFragment extends Fragment {

    private SharedPreferences prefs;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_medication, container, false);
        LinearLayout noMedicationMsg = view.findViewById(R.id.noMedicationMsg);
        prefs = requireActivity().getSharedPreferences("AuthPrefs",MODE_PRIVATE);

        final List<MedicationTrackerModel> medicationList = reminderDB.getMedicationTrackerDao().getMyMedicationHistory(getUserID());
        if(medicationList.isEmpty()){
            noMedicationMsg.setVisibility(View.VISIBLE); // No reminders found
        }
        else {
            final List<String> medsList = new ArrayList<>();
            for(MedicationTrackerModel med : medicationList){
                if(!medsList.contains(med.getMedicationName())){
                    medsList.add(med.getMedicationName());
                }
            }
            noMedicationMsg.setVisibility(View.GONE);
            setMedicationsRV(view,medsList);
        }
        return view;
    }

    private String getUserID(){
        if(prefs.contains("UserId")){
            return prefs.getString("UserId",null);
        }
        return null;
    }

    private void setMedicationsRV(View view, List<String> medsList) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) { RecyclerView medicationsRV = view.findViewById(R.id.medicationsRV);
            medicationsRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
            final MyMedicationsAdapter adapter;
            adapter = new MyMedicationsAdapter(medsList,navHostFragment.getNavController());
            medicationsRV.setAdapter(adapter);
        }
    }
}