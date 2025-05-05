package com.example.mindvault.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM Note")
    List<Note> getAllNotes();

    @Query("SELECT * FROM Note WHERE id = :id")
    Note getNoteById(int id);

    @Query("SELECT * FROM Note " +
            "WHERE title   LIKE '%' || :q || '%' " +
            "   OR content LIKE '%' || :q || '%'")
    List<Note> searchNotes(String q);
}
