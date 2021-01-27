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

    @Query("SELECT * FROM medicationTrackerData WHERE userID IN (:userID)")
    List<MedicationTrackerModel> getMyMedicationHistory(String userID);

    @Query("SELECT * FROM medicationTrackerData WHERE userID IN (:userID) AND medicationInfo IN (:medicineName)  ")
    List<MedicationTrackerModel> loadSelected(String userID, String medicineName);

    @Query("SELECT * FROM medicationTrackerData WHERE medicationInfo IN (:medicineName) AND dateStr IN (:intakeDate)")
    List<MedicationTrackerModel> loadSelectedDate(String medicineName,String intakeDate);
}
