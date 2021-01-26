package com.example.mustafa.mijnmedicijn.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;

import java.util.List;

@Dao
public interface RemindersDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(RemindersModel reminder);

    @Delete
    void deleteReminder(RemindersModel model);

    @Query("SELECT * FROM remindersData")
    List<RemindersModel> getMyReminders();

    @Query("SELECT * FROM remindersData WHERE _id IN (:reminderID)")
    RemindersModel loadSingle(int reminderID);

}
