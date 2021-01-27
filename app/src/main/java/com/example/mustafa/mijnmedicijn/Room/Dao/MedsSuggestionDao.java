package com.example.mustafa.mijnmedicijn.Room.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;
import com.example.mustafa.mijnmedicijn.Room.Models.MedsSuggestionsModel;

import java.util.List;

@Dao
public interface MedsSuggestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addSuggestion(MedsSuggestionsModel suggestion);

    @Delete
    void deleteSuggestion(MedsSuggestionsModel suggestionInfo);

    @Query("SELECT * FROM medsSuggestionsData")
    List<MedsSuggestionsModel> getSuggestionsList();

}
