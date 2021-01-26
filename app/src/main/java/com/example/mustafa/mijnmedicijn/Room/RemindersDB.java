package com.example.mustafa.mijnmedicijn.Room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mustafa.mijnmedicijn.Room.Dao.MedicationTrackerDao;
import com.example.mustafa.mijnmedicijn.Room.Dao.RemindersDataDao;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;


@Database(entities = { RemindersModel.class,MedicationTrackerModel.class}, version = 1)
public abstract class RemindersDB extends RoomDatabase {

    public abstract RemindersDataDao getRemindersDao();
    public abstract MedicationTrackerDao getMedicationTrackerDao();

    private static RemindersDB remindersDB;

    public static RemindersDB getInstance(Context context) {
        if (remindersDB == null) {
            remindersDB = buildDatabaseInstance(context);
        }
        return remindersDB;
    }

    private static RemindersDB buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                RemindersDB.class,
                "reminders.db")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        remindersDB = null;
    }

}