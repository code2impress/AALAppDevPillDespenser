package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.os.Bundle;

public class PatientOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_overview);

        // Fetch the shared preferences to get the username
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User"); // Default to "User" if no username found

        // Update greeting TextView with username
        TextView greetingTextView = findViewById(R.id.greetingTextView);
        greetingTextView.setText(getString(R.string.greeting_text, username)); // Assuming you have a string resource with a placeholder

        // Use UserDatabaseHelper to access pill intake data
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);

        // Assuming you have methods to calculate the start and end date of the current week
        String startDate = "2023-01-01"; // Placeholder, calculate dynamically
        String endDate = "2023-01-07"; // Placeholder, calculate dynamically

        // Get user ID for the username to fetch pills data
        int userId = dbHelper.getUserIdByUsername(username);
        int pillsTakenThisWeek = dbHelper.getPillsTakenInWeek(userId, startDate, endDate);

        // Update TextView with pills taken this week
        TextView pillsTakenTextView = findViewById(R.id.pillsTakenTextView); // Ensure this ID exists in your XML
        pillsTakenTextView.setText(getString(R.string.pills_taken_this_week, pillsTakenThisWeek)); // Assuming you have a string resource with a placeholder
    }
}
