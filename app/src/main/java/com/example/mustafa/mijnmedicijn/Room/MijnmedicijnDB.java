package com.example.mustafa.mijnmedicijn.Room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mustafa.mijnmedicijn.Room.Dao.MedicationTrackerDao;
import com.example.mustafa.mijnmedicijn.Room.Dao.MedsSuggestionDao;
import com.example.mustafa.mijnmedicijn.Room.Dao.RemindersDataDao;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;
import com.example.mustafa.mijnmedicijn.Room.Models.MedsSuggestionsModel;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;


@Database(entities = { RemindersModel.class,MedicationTrackerModel.class, MedsSuggestionsModel.class}, version = 2)

public abstract class MijnmedicijnDB extends RoomDatabase {

    public abstract RemindersDataDao getRemindersDao();
    public abstract MedicationTrackerDao getMedicationTrackerDao();
    public abstract MedsSuggestionDao getMedsSuggestionDao();

    private static MijnmedicijnDB remindersDB;

    public static MijnmedicijnDB getInstance(Context context) {
        if (remindersDB == null) {
            remindersDB = buildDatabaseInstance(context);
        }
        return remindersDB;
    }

    private static MijnmedicijnDB buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                MijnmedicijnDB.class,
                "mijnmedicijn.db")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        remindersDB = null;
    }

}