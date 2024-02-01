package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

public class LoginPAT extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pat);

        // Initialize the UserDatabaseHelper
        userDatabaseHelper = new UserDatabaseHelper(this);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate credentials for user
                String userType = userDatabaseHelper.validateUser(username, password);
                if (userType != null) {
                    saveLoginState(userType); // Save login state
                    Intent intent;
                    if ("doctor".equals(userType)) {
                        // If the user is a doctor, redirect to doctor's activity
                        intent = new Intent(LoginPAT.this, docgenqr.class);
                    } else {
                        // If the user is a patient, redirect to patient's activity
                        intent = new Intent(LoginPAT.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPAT.this, "Access Denied", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void saveLoginState(String userType) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userType", userType);
        editor.apply();
    }
}
