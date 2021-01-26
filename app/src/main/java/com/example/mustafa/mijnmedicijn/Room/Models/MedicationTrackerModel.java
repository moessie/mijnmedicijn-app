package com.example.mustafa.mijnmedicijn.Room.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicationTrackerData")
public class MedicationTrackerModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "medicationID")
    Integer medicationID;

    @NonNull
    @ColumnInfo(name = "timeStamp")
    private Long timeStamp;

    @NonNull
    @ColumnInfo(name = "dateStr")
    private String dateStr;

    @NonNull
    @ColumnInfo(name = "timeStr")
    private String timeStr;

    @NonNull
    @ColumnInfo(name = "medicationInfo")
    private String medicationName;

    @NonNull
    @ColumnInfo(name = "intakeQuantity")
    private String intakeQuantity;

    public MedicationTrackerModel(@NonNull Integer medicationID, @NonNull Long timeStamp, @NonNull String dateStr, @NonNull String timeStr, @NonNull String medicationName, @NonNull String intakeQuantity) {
        this.medicationID = medicationID;
        this.timeStamp = timeStamp;
        this.dateStr = dateStr;
        this.timeStr = timeStr;
        this.medicationName = medicationName;
        this.intakeQuantity = intakeQuantity;
    }

    @NonNull
    public Integer getMedicationID() {
        return medicationID;
    }

    @NonNull
    public Long getTimeStamp() {
        return timeStamp;
    }

    @NonNull
    public String getDateStr() {
        return dateStr;
    }

    @NonNull
    public String getTimeStr() {
        return timeStr;
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
