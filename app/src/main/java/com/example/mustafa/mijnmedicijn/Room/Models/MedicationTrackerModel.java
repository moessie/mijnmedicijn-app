package com.example.mustafa.mijnmedicijn.Room.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicationTrackerData")
public class MedicationTrackerModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mId")
    private final int medicationID;

    @NonNull
    @ColumnInfo(name = "intakeDate")
    private final String intakeDate;

    @NonNull
    @ColumnInfo(name = "medicationInfo")
    private final String medicationName;

    @NonNull
    @ColumnInfo(name = "intakeQuantity")
    private final String intakeQuantity;

    public MedicationTrackerModel(int medicationID, @NonNull String intakeDate, @NonNull String medicationName, @NonNull String intakeQuantity) {
        this.medicationID = medicationID;
        this.intakeDate = intakeDate;
        this.medicationName = medicationName;
        this.intakeQuantity = intakeQuantity;
    }

    public int getMedicationID() {
        return medicationID;
    }

    @NonNull
    public String getIntakeDate() {
        return intakeDate;
    }

    @NonNull
    public String getMedicationName() {
        return medicationName;
    }

    @NonNull
    public String getIntakeQuantity() {
        return intakeQuantity;
    }
}
