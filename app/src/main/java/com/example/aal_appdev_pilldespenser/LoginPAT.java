package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;


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
        Button createUserButton = findViewById(R.id.createUserButton);

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate credentials for user
                String userType = userDatabaseHelper.validateUser(username, password);
                if (userType != null) {
                    saveLoginState(userType, username); // Now also passing the username
                    Intent intent;
                    if ("doctor".equals(userType)) {
                        intent = new Intent(LoginPAT.this, docgenqr.class);
                    } else {
                        // Redirect patients to the new PatientOverview activity instead of MainActivity
                        intent = new Intent(LoginPAT.this, PatientOverview.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPAT.this, "Access Denied", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Set up the create user button click listener
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Always create a user with patient type
                if (!username.isEmpty() && !password.isEmpty()) {
                    User newUser = new User(username, password, "patient");
                    userDatabaseHelper.addUser(newUser); // Add the user to the database
                    Toast.makeText(LoginPAT.this, "User created successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginPAT.this, "Username or password cannot be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void saveLoginState(String userType, String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userType", userType);
        editor.putString("username", username); // Save the logged-in username
        editor.apply();
    }

}
