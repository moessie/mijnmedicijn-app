package com.example.mustafa.mijnmedicijn.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RemindersDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertReminder(RemindersData reminder);

    @Delete
    void deleteReminder(RemindersData model);

    @Query("SELECT * FROM remindersData")
    List<RemindersData> getMyReminders();
}
