package com.example.mustafa.mijnmedicijn.Room.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medsSuggestionsData")
public class MedsSuggestionsModel {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private final int _id;

    @NonNull
    @ColumnInfo(name = "suggestionName")
    private final String medicineName;

    public MedsSuggestionsModel(int _id, @NonNull String medicineName) {
        this._id = _id;
        this.medicineName = medicineName;
    }

    public int get_id() {
        return _id;
    }

    @NonNull
    public String getMedicineName() {
        return medicineName;
    }
}
