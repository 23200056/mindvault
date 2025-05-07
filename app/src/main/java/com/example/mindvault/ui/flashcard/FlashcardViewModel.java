package com.example.mindvault.ui.flashcard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Flashcard;

import java.util.List;

public class FlashcardViewModel extends AndroidViewModel {
    private final LiveData<List<Flashcard>> allFlashcards;

    public FlashcardViewModel(@NonNull Application application) {
        super(application);
        allFlashcards = AppDatabase.getInstance(application).flashcardDao().getAllFlashcards();
    }

    public LiveData<List<Flashcard>> getAllFlashcards() {
        return allFlashcards;
    }
}