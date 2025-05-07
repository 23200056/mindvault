package com.example.mindvault.data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Note.class, Flashcard.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();

    public abstract NoteDao noteDao();

    public abstract FlashcardDao flashcardDao();

    public static synchronized AppDatabase getInstance(Application context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "mindvault_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
