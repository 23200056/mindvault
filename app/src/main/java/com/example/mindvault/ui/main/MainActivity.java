package com.example.mindvault.ui.main;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.mindvault.R;
import com.example.mindvault.ui.home.HomeFragment;
import com.example.mindvault.ui.notes.NotesFragment;
import com.example.mindvault.ui.pomodoro.PomodoroFragment;
import com.example.mindvault.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    LinearLayout navHome;
    LinearLayout navNotes;
    LinearLayout navPlanner;
    LinearLayout navFlashcards;
    LinearLayout navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        navHome = findViewById(R.id.navHome);
        navNotes = findViewById(R.id.navNotes);
        navPlanner = findViewById(R.id.navPlanner);
        navFlashcards = findViewById(R.id.navFlashcards);
        navProfile = findViewById(R.id.navProfile);


        navHome.setOnClickListener(v -> loadFragment(new HomeFragment()));
        navNotes.setOnClickListener(v -> loadFragment(new NotesFragment()));
        navProfile.setOnClickListener(v -> loadFragment(new ProfileFragment()));
        navPlanner.setOnClickListener(v -> loadFragment(new PomodoroFragment()));

        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
