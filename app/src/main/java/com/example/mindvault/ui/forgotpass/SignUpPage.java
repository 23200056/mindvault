package com.example.mindvault.ui.forgotpass;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mindvault.R;
import com.example.mindvault.ui.login.LoginPage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpPage extends AppCompatActivity {

    private ImageButton backButton, passwordToggle;
    private EditText usernameInput, emailInput, dobInput, passwordInput;
    private TextView loginText;
    private Button registerButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // Bind views
        backButton      = findViewById(R.id.backButton);
        usernameInput   = findViewById(R.id.usernameInput);
        emailInput      = findViewById(R.id.emailInput);
        dobInput        = findViewById(R.id.dobInput);
        passwordInput   = findViewById(R.id.passwordInput);
        passwordToggle  = findViewById(R.id.passwordToggle);
        loginText       = findViewById(R.id.loginText);
        registerButton  = findViewById(R.id.registerButton);

        // Back arrow → close this Activity
        backButton.setOnClickListener(v -> finish());

        // “Already have an account? Login” → go to LoginPage
        loginText.setOnClickListener(v -> {
            startActivity(new Intent(SignUpPage.this, LoginPage.class));
            finish();
        });

        // Date picker for DOB
        dobInput.setInputType(InputType.TYPE_NULL);
        dobInput.setOnClickListener(v -> showDatePicker());

        // Toggle password visibility
        passwordToggle.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
                passwordToggle.setImageResource(R.drawable.ic_eye_slash);
            } else {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
                passwordToggle.setImageResource(R.drawable.ic_eye_slash);
            }
            // Keep cursor at end
            passwordInput.setSelection(passwordInput.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        // Register button
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String email    = emailInput.getText().toString().trim();
            String dob      = dobInput.getText().toString().trim();
            String pwd      = passwordInput.getText().toString();

            if (username.isEmpty() || email.isEmpty() || dob.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: replace with real registration logic
            if (fakeRegister(username, email, dob, pwd)) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                // e.g., go to main screen:
                // startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Shows a date picker and fills dobInput with MM/dd/yyyy */
    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR),
                m = cal.get(Calendar.MONTH),
                d = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    cal.set(year, month, dayOfMonth);
                    String formatted = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                            .format(cal.getTime());
                    dobInput.setText(formatted);
                },
                y, m, d
        );
        dpd.show();
    }

    /** Stub registration logic – replace with your API or DB call */
    private boolean fakeRegister(String username, String email, String dob, String pwd) {
        return true;
    }
}
