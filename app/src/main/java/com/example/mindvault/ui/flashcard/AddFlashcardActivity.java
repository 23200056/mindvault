package com.example.mindvault.ui.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Flashcard;

import java.util.concurrent.Executors;

public class AddFlashcardActivity extends AppCompatActivity {
    private EditText titleInput, descriptionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        // Link components
        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);

        findViewById(R.id.createButton).setOnClickListener(v -> saveFlashcard());

        // Set the backButton to redirect to the Main Page
        findViewById(R.id.backButton).setOnClickListener(v -> {
            startActivity(new Intent(this, FlashcardPage.class));
        });
    }

    private void saveFlashcard() {
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        long timestamp = System.currentTimeMillis();

        Flashcard flashcard = new Flashcard();
        flashcard.setTitle(title);
        flashcard.setDescription(description);
        flashcard.setTimestamp(timestamp);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(this.getApplication()).flashcardDao().insert(flashcard);
            runOnUiThread(this::finish);
        });
    }
}
