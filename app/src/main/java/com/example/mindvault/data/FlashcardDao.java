package com.example.mindvault.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// FlashcardDao.java
@Dao
public interface FlashcardDao {
    @Insert
    void insert(Flashcard flashcard);

    @Query("SELECT * FROM flashcard ORDER BY timestamp ASC")
    LiveData<List<Flashcard>> getAllFlashcards();

    @Delete
    void delete(Flashcard flashcard);

    @Update
    void update(Flashcard f);
}
