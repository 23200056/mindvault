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
import com.example.mindvault.ui.sign_up.ForgotPasswordPage;
import com.example.mindvault.ui.forgotpass.SignUpPage;

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
            // already on login screen; add any visual state change if you like
        });

        // Tab “Sign Up”
        tabSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginPage.this, SignUpPage.class));
        });

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_slash);
            } else {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_slash);
            }
            // keep cursor at end
            passwordInput.setSelection(passwordInput.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        // “Forgot Password?”
        forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginPage.this, ForgotPasswordPage.class))
        );

        // “Log In” button
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String pwd   = passwordInput.getText().toString();

            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: replace with real auth
            if (fakeAuth(email, pwd)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                // navigate into your main activity…
            } else {
                Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Social buttons
        googleButton.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In tapped", Toast.LENGTH_SHORT).show()
        );
        facebookButton.setOnClickListener(v ->
                Toast.makeText(this, "Facebook Login tapped", Toast.LENGTH_SHORT).show()
        );
    }

    /** Stub method for demonstration */
    private boolean fakeAuth(String email, String pwd) {
        return email.equals("user@example.com") && pwd.equals("password123");
    }
}
