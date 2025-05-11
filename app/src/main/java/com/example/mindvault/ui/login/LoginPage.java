package com.example.mindvault.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.User;
import com.example.mindvault.ui.forgotpass.ForgotPasswordPage;
import com.example.mindvault.ui.main.MainActivity;
import com.example.mindvault.ui.sign_up.SignUpPage;

import java.util.concurrent.Executors;

public class LoginPage extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private ImageButton togglePasswordVisibility;
    private CheckBox rememberMe;
    private TextView forgotPassword;
    private Button tabLogin, tabSignUp, loginButton, googleButton, facebookButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Bind views
        emailInput               = findViewById(R.id.email_input);
        passwordInput            = findViewById(R.id.password_input);
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        rememberMe               = findViewById(R.id.remember_me);
        forgotPassword           = findViewById(R.id.forgot_password);

        tabLogin      = findViewById(R.id.btn_login);
        tabSignUp     = findViewById(R.id.btn_signup);
        loginButton   = findViewById(R.id.login_button);
        googleButton  = findViewById(R.id.google_button);
        facebookButton= findViewById(R.id.facebook_button);

        // Tab “Log In” (default)
        tabLogin.setOnClickListener(v -> {
            // already here
        });

        // Tab “Sign Up”
        tabSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginPage.this, SignUpPage.class))
        );

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_slash);
            } else {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_slash);
            }
            passwordInput.setSelection(passwordInput.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        // “Forgot Password?”
        forgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "In Progress Feature", Toast.LENGTH_SHORT).show()
        );

        // “Log In” button ⇒ authenticate via Room
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String pwd   = passwordInput.getText().toString();
            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Run lookup on a background thread
            AppDatabase db = AppDatabase.getInstance(this);
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = db.userDao().getUser(email, pwd);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        // Social buttons (stubs)
        googleButton.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In tapped", Toast.LENGTH_SHORT).show()
        );
        facebookButton.setOnClickListener(v ->
                Toast.makeText(this, "Facebook Login tapped", Toast.LENGTH_SHORT).show()
        );
    }
}

// name: signup1
// email: signuptest@email.com
// dob: 05/11/2025
// password: pass