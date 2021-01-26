package com.example.mustafa.mijnmedicijn.Room.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;

import java.util.List;

@Dao
public interface MedicationTrackerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedication(MedicationTrackerModel medicationInfo);

    @Delete
    void deleteReminder(MedicationTrackerModel medicationInfo);

    @Query("SELECT * FROM medicationTrackerData")
    List<MedicationTrackerModel> getMyMedicationHistory();

    @Query("SELECT * FROM medicationTrackerData WHERE mId IN (:medicationID)")
    MedicationTrackerModel loadSingle(int medicationID);

}
