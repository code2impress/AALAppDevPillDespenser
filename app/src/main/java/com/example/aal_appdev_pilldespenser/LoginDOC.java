package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class LoginDOC extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doc);

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
                // Get username and password from EditTexts
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate credentials using the UserDatabaseHelper
                if (userDatabaseHelper.validateUser(username, password)) {
                    // If credentials are valid, proceed to docgenqr activity
                    Intent intent = new Intent(LoginDOC.this, docgenqr.class);
                    startActivity(intent);
                    finish(); // Optionally close the LoginDOC activity
                } else {
                    // Handle invalid credentials case
                    Toast.makeText(LoginDOC.this, "Access Denied", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
