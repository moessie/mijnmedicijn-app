package com.example.mustafa.mijnmedicijn.Room.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "remindersData")
public class RemindersModel {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private final int _id;

    @NonNull
    @ColumnInfo(name =  "userId")
    private final String userID;

    @NonNull
    @ColumnInfo(name = "medicineName")
    private final String medicineName;

    @NonNull
    @ColumnInfo(name = "doseUnit")
    private final String doseUnit;

    @NonNull
    @ColumnInfo(name = "doseQuantity")
    private final String doseQuantity;

    @NonNull
    @ColumnInfo(name = "reminderTime")
    private final String reminderTime;

    @NonNull
    @ColumnInfo(name = "reminderRepeatInfo")
    private final String reminderRepeatInfo;

    public RemindersModel(int _id, @NonNull String userID, @NonNull String medicineName, @NonNull String doseUnit, @NonNull String doseQuantity, @NonNull String reminderTime, @NonNull String reminderRepeatInfo) {
        this._id = _id;
        this.userID = userID;
        this.medicineName = medicineName;
        this.doseUnit = doseUnit;
        this.doseQuantity = doseQuantity;
        this.reminderTime = reminderTime;
        this.reminderRepeatInfo = reminderRepeatInfo;
    }

    public int get_id() {
        return _id;
    }

    @NonNull
    public String getUserID() {
        return userID;
    }

    @NonNull
    public String getMedicineName() {
        return medicineName;
    }

    @NonNull
    public String getDoseUnit() {
        return doseUnit;
    }

    @NonNull
    public String getDoseQuantity() {
        return doseQuantity;
    }

    @NonNull
    public String getReminderTime() {
        return reminderTime;
    }

    @NonNull
    public String getReminderRepeatInfo() {
        return reminderRepeatInfo;
    }
}
