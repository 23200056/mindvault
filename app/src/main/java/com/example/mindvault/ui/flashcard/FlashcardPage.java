package com.example.mindvault.ui.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class FlashcardPage extends AppCompatActivity {
    private FlashcardAdapter adapter;
    private List<Flashcard> allFlashcards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Link the recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Set the recycler view to have an Linear Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the FlashCardAdapter and set it to the recycler view
        adapter = new FlashcardAdapter();
        recyclerView.setAdapter(adapter);

        // Arranges the cards in oldest first order
        FlashcardViewModel viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);
        viewModel.getAllFlashcards().observe(this, flashcards -> {
            allFlashcards = flashcards;
            adapter.setFlashcards(flashcards);
            adapter.notifyDataSetChanged();
        });

        // Set the addButton to redirect to the AddFlashCard Page
        findViewById(R.id.addButton).setOnClickListener(v -> {
            startActivity(new Intent(FlashcardPage.this, AddFlashcardActivity.class));
        });

        findViewById(R.id.shuffleButton).setOnClickListener(v -> {
            if (allFlashcards.isEmpty()) {
                Toast.makeText(this, "No flashcards to shuffle", Toast.LENGTH_SHORT).show();
                return;
            }
            // make a mutable copy & shuffle
            ArrayList<Flashcard> shuffled = new ArrayList<>(allFlashcards);
            Collections.shuffle(shuffled);

            // launch detail activity at position 0 with the shuffled list
            Intent intent = new Intent(FlashcardPage.this, FlashcardDetailActivity.class);
            intent.putParcelableArrayListExtra("flashcards", shuffled);
            intent.putExtra("position", 0);
            startActivity(intent);
        });


        // **SWIPE TO DELETE**
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override public boolean onMove(
                    @NonNull RecyclerView rv,
                    @NonNull RecyclerView.ViewHolder vh,
                    @NonNull RecyclerView.ViewHolder target
            ) { return false; }

            @Override public void onSwiped(
                    @NonNull RecyclerView.ViewHolder vh,
                    int direction
            ) {
                int pos = vh.getAdapterPosition();
                Flashcard toDelete = adapter.getFlashcardAt(pos);

                // delete on background thread
                Executors.newSingleThreadExecutor().execute(() ->
                        AppDatabase.getInstance(FlashcardPage.this.getApplication())
                                .flashcardDao()
                                .delete(toDelete)
                );

                runOnUiThread(() ->
                        Toast.makeText(FlashcardPage.this,
                                "Flashcard deleted",
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).attachToRecyclerView(recyclerView);
    }
}