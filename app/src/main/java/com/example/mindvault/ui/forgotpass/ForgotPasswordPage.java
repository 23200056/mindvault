package com.example.mindvault.ui.forgotpass;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;

import java.text.BreakIterator;
import java.util.concurrent.Executors;

public class ForgotPasswordPage extends AppCompatActivity {

    private EditText password1, password2;
    private ImageView toggle1, toggle2;
    private boolean isPassword1Visible = false, isPassword2Visible = false;
    private BreakIterator emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_page);

        // 1) Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.forgotpass_layout),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // 2) Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // 3) Enable the two password fields
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        enablePasswordField(password1);
        enablePasswordField(password2);

        // 4) Locate each eye-slash icon by walking the view hierarchy
        LinearLayout inputContainer = findViewById(R.id.inputContainer);
        LinearLayout row1 = (LinearLayout) inputContainer.getChildAt(0);
        LinearLayout row2 = (LinearLayout) inputContainer.getChildAt(1);
        toggle1 = (ImageView) row1.getChildAt(2);
        toggle2 = (ImageView) row2.getChildAt(2);

        // 5) Toggle logic for field 1
        toggle1.setOnClickListener(v -> {
            if (isPassword1Visible) {
                // hide
                password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggle1.setImageResource(R.drawable.ic_eye_slash);
            } else {
                // show
                password1.setTransformationMethod(null);
                toggle1.setImageResource(R.drawable.ic_eye_slash);
            }
            // keep cursor at end
            password1.setSelection(password1.getText().length());
            isPassword1Visible = !isPassword1Visible;
        });

        // 6) Toggle logic for field 2
        toggle2.setOnClickListener(v -> {
            if (isPassword2Visible) {
                password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggle2.setImageResource(R.drawable.ic_eye_slash);
            } else {
                password2.setTransformationMethod(null);
                toggle2.setImageResource(R.drawable.ic_eye_slash);
            }
            password2.setSelection(password2.getText().length());
            isPassword2Visible = !isPassword2Visible;
        });

        // 7) (Optional) Submit button stub
        Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(v -> {
            String p1 = password1.getText().toString();
            String p2 = password2.getText().toString();
            // TODO: validate and send to your backend
        });


        AppDatabase db = AppDatabase.getInstance(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            String newPass = "";
            db.userDao().updatePassword(emailInput.getText().toString(), newPass);
            runOnUiThread(() ->
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
            );
        });
    }

    /** Helper to turn a disabled XML field into a real password input */
    private void enablePasswordField(EditText et) {
        et.setEnabled(true);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.setCursorVisible(true);
        et.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
