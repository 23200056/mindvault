package com.example.mindvault.ui.flashcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Flashcard;

import java.util.List;
import java.util.concurrent.Executors;

public class FlashcardDetailActivity extends AppCompatActivity {
    private List<Flashcard> flashcards;
    private int currentPosition;
    private TextView titleText, descriptionText;
    private boolean showingDescription = false;
    private CardView cardContainer;

    private Button passButton, failButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_detail);

        // Get data from intent
        flashcards = getIntent().getParcelableArrayListExtra("flashcards");
        currentPosition = getIntent().getIntExtra("position", 0);


        // Initialize cardContainer and set camera distance for 3D effect
        cardContainer = findViewById(R.id.cardContainer);
        cardContainer.setCameraDistance(20000 * getResources().getDisplayMetrics().density);

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);

        passButton = findViewById(R.id.btnPass);
        failButton = findViewById(R.id.btnFail);

        passButton.setOnClickListener(v -> handleStateClick("Pass"));
        failButton.setOnClickListener(v -> handleStateClick("Fail"));


        updateFlashcardDisplay();

        // Card flip functionality
        findViewById(R.id.cardContainer).setOnClickListener(v -> toggleCardDisplay());

        // Navigation buttons
        findViewById(R.id.prevButton).setOnClickListener(v -> navigate(-1));
        findViewById(R.id.nextButton).setOnClickListener(v -> navigate(1));

        findViewById(R.id.backButton).setOnClickListener(v -> {
            startActivity(new Intent(this, FlashcardPage.class));
        });
    }

    private void handleStateClick(String newState) {
        Flashcard current = flashcards.get(currentPosition);
        String oldState = current.getState() != null ? current.getState() : "None";

        // 1. clicking opposite → switch
        // 2. clicking same   → back to “None”
        if (oldState.equals(newState)) {
            current.setState("None");
        } else {
            current.setState(newState);
        }

        // persist
        Executors.newSingleThreadExecutor().execute(() ->
                AppDatabase.getInstance(getApplication())
                        .flashcardDao()
                        .update(current)
        );

        // reflect in UI
        runOnUiThread(() -> {
            updateStateButtons(current.getState());
            Toast.makeText(this, "State: " + current.getState(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateStateButtons(String state) {
        switch (state) {
            case "Pass":
                passButton.setAlpha(1f);
                failButton.setAlpha(0.5f);
                passButton.setBackgroundColor(Color.GREEN);
                failButton.setTextColor(Color.WHITE);
                failButton.setBackgroundColor(Color.GRAY);
                break;
            case "Fail":
                passButton.setAlpha(0.5f);
                failButton.setAlpha(1f);
                failButton.setBackgroundColor(Color.RED);
                failButton.setTextColor(Color.WHITE);
                passButton.setBackgroundColor(Color.GRAY);
                break;
            default:  // None
                passButton.setAlpha(1f);
                failButton.setAlpha(1f);
                passButton.setBackgroundColor(Color.GRAY);
                failButton.setBackgroundColor(Color.GRAY);
                failButton.setTextColor(Color.WHITE);
                passButton.setTextColor(Color.WHITE);
                break;
        }
    }


    private void updateFlashcardDisplay() {
        Flashcard current = flashcards.get(currentPosition);
        titleText.setText(current.getTitle());
        descriptionText.setText(current.getDescription());
        updateStateButtons(current.getState());
        resetCardVisibilityFast();
    }

    private void resetCardVisibilityFast() {
        titleText.setVisibility(View.VISIBLE);
        descriptionText.setVisibility(View.GONE);
        showingDescription = false;
        // Reset transformations when navigating to a new card
        cardContainer.setRotationX(0f);
        cardContainer.setScaleY(1f);
    }

    private void toggleCardDisplay() {
        final float targetRotation = showingDescription ? 0f : 180f;
        final float targetScale = showingDescription ? 1f : -1f;

        cardContainer.animate()
                .rotationX(targetRotation)
                .scaleY(targetScale)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Toggle visibility
                        titleText.setVisibility(showingDescription ? View.VISIBLE : View.GONE);
                        descriptionText.setVisibility(showingDescription ? View.GONE : View.VISIBLE);
                        showingDescription = !showingDescription;
                    }
                })
                .withLayer()
                .start();
    }

    // Modified resetCardVisibility() method
    private void resetCardVisibility() {
        // Animate back to original state before changing visibility
        cardContainer.animate()
                .rotationX(0f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withLayer()
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Update visibility AFTER animation completes
                        titleText.setVisibility(View.VISIBLE);
                        descriptionText.setVisibility(View.GONE);
                        showingDescription = false;
                    }
                })
                .withLayer()
                .start();
    }

    private void navigate(int direction) {
        int newPosition = currentPosition + direction;

        if (newPosition >= 0 && newPosition < flashcards.size()) {
            currentPosition = newPosition;
            updateFlashcardDisplay();
        } else {
            Toast.makeText(this,
                    direction > 0 ? "Last card" : "First card",
                    Toast.LENGTH_SHORT).show();
        }
    }
}