package com.example.mindvault.ui.pomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mindvault.R;

public class PomodoroSettingsFragment extends Fragment {
    private EditText focusLengthEditText;
    private EditText pomodorosUntilLongBreakEditText;
    private EditText shortBreakLengthEditText;
    private EditText longBreakLengthEditText;
    private Button saveButton;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "PomodoroPrefs";
    private static final String KEY_FOCUS_LENGTH = "focusLength";
    private static final String KEY_POMODOROS_UNTIL_LONG_BREAK = "pomodorosUntilLongBreak";
    private static final String KEY_SHORT_BREAK_LENGTH = "shortBreakLength";
    private static final String KEY_LONG_BREAK_LENGTH = "longBreakLength";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro_settings, container, false);

        focusLengthEditText = view.findViewById(R.id.focusLengthEditText);
        pomodorosUntilLongBreakEditText = view.findViewById(R.id.pomodorosUntilLongBreakEditText);
        shortBreakLengthEditText = view.findViewById(R.id.shortBreakLengthEditText);
        longBreakLengthEditText = view.findViewById(R.id.longBreakLengthEditText);
        saveButton = view.findViewById(R.id.saveButton);

        // Load current settings from SharedPreferences
        loadSettings();

        saveButton.setOnClickListener(v -> saveSettings());

        return view;
    }

    private void loadSettings() {
        focusLengthEditText.setText(String.valueOf(sharedPreferences.getInt(KEY_FOCUS_LENGTH, 25)));
        pomodorosUntilLongBreakEditText.setText(String.valueOf(sharedPreferences.getInt(KEY_POMODOROS_UNTIL_LONG_BREAK, 4)));
        shortBreakLengthEditText.setText(String.valueOf(sharedPreferences.getInt(KEY_SHORT_BREAK_LENGTH, 5)));
        longBreakLengthEditText.setText(String.valueOf(sharedPreferences.getInt(KEY_LONG_BREAK_LENGTH, 15)));
    }

    private void saveSettings() {
        try {
            // Get values from EditText fields
            int focusLength = Integer.parseInt(focusLengthEditText.getText().toString());
            int pomodorosUntilLongBreak = Integer.parseInt(pomodorosUntilLongBreakEditText.getText().toString());
            int shortBreakLength = Integer.parseInt(shortBreakLengthEditText.getText().toString());
            int longBreakLength = Integer.parseInt(longBreakLengthEditText.getText().toString());

            // Validate inputs
            if (focusLength <= 0 || pomodorosUntilLongBreak <= 0 || shortBreakLength <= 0 || longBreakLength <= 0) {
                Toast.makeText(getContext(), "Please enter positive numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_FOCUS_LENGTH, focusLength);
            editor.putInt(KEY_POMODOROS_UNTIL_LONG_BREAK, pomodorosUntilLongBreak);
            editor.putInt(KEY_SHORT_BREAK_LENGTH, shortBreakLength);
            editor.putInt(KEY_LONG_BREAK_LENGTH, longBreakLength);
            editor.apply();

            // Show success message and go back
            Toast.makeText(getContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}