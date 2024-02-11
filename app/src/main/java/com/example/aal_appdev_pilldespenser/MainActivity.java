package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button logoutButton; // Use Button instead of ImageButton

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            // Add any other permissions you need
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check and request necessary permissions
        checkAndRequestPermissions();

        // Initialize UI components and set up listeners
        setupButtons();

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Check the login state initially
        checkLoginState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-check the login state every time MainActivity resumes
        checkLoginState();
    }

    private void checkLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            logoutButton.setVisibility(View.GONE);
        }
    }

    private void setupButtons() {
        ImageButton button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                String userType = sharedPreferences.getString("userType", "");

                if (isLoggedIn && "doctor".equals(userType)) {
                    // User is a doctor and logged in, skip directly to docgenqr activity
                    Intent intent = new Intent(MainActivity.this, docgenqr.class);
                    startActivity(intent);
                } else if ("patient".equals(userType)) {
                    // If a patient is trying to access, show a toast message
                    Toast.makeText(MainActivity.this, "Only for Medical Personnel", Toast.LENGTH_LONG).show();
                } else {
                    // Otherwise, go to the LoginDOC activity for login
                    Intent intent = new Intent(MainActivity.this, LoginDOC.class);
                    startActivity(intent);
                }
            }
        });

        ImageButton button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanQR.class);
                startActivity(intent);
            }
        });

        ImageButton loginPatButton = findViewById(R.id.loginPatButton);
        loginPatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                String userType = sharedPreferences.getString("userType", "");

                if (isLoggedIn && "doctor".equals(userType)) {
                    // If the user is a doctor, redirect to doctor's activity
                    Intent intent = new Intent(MainActivity.this, docgenqr.class);
                    startActivity(intent);
                } else if (isLoggedIn && "patient".equals(userType)) {
                    // If the user is a patient, redirect to patient's overview activity
                    Intent intent = new Intent(MainActivity.this, PatientOverview.class);
                    startActivity(intent);
                } else {
                    // If no user is logged in, redirect to patient login activity
                    Intent intent = new Intent(MainActivity.this, LoginPAT.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void checkAndRequestPermissions() {
        boolean permissionsNeeded = false;
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded = true;
                break;
            }
        }

        if (permissionsNeeded) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            // Handle the permissions request response here
            // You can check each permission if it was granted or denied
        }
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        logoutButton.setVisibility(View.GONE);

        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
