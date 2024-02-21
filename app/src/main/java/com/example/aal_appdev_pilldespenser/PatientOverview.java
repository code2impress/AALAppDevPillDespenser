package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class PatientOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_overview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePillsTakenData();
        setupBarChart();
    }

    private void updatePillsTakenData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User"); // Default to "User" if no username found

        TextView greetingTextView = findViewById(R.id.greetingTextView);
        greetingTextView.setText(getString(R.string.greeting_text, username)); // Set greeting text with username

        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        int userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) return;

        // Calculate the total pills taken for all available weeks
        int totalPillsTaken = calculateTotalPillsTaken(userId);

        TextView pillsTakenTextView = findViewById(R.id.pillsTakenTextView);
        pillsTakenTextView.setText(getString(R.string.pills_taken_this_week, totalPillsTaken)); // Set total pills taken text
    }

    private int calculateTotalPillsTaken(int userId) {
        // Assuming you have a method in UserDatabaseHelper to calculate total pills taken for all weeks
        // For demonstration, let's return a mock value. Implement this method based on your database schema.
        return new UserDatabaseHelper(this).getTotalPillsTakenByUser(userId);
    }

    private void setupBarChart() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username == null) {
            // Handle case where no username is found
            return;
        }

        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        int userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) {
            // Handle case where user ID is not found
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int currentWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        for (int i = 9; i >= 0; i--) {
            int weekOfYear = currentWeekOfYear - i;
            if (weekOfYear < 1) {
                // Adjust for previous year's weeks if necessary
                weekOfYear += 52; // This might need adjustment based on the calendar specifics
            }
            int pillsTaken = dbHelper.getPillsTakenForWeekOfYear(userId, weekOfYear);
            // The chart library uses float for the X-axis values
            entries.add(new BarEntry(9 - i, pillsTaken));
        }

        if (!entries.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entries, "Pills Taken");
            dataSet.setColor(Color.BLUE);
            BarData data = new BarData(dataSet);

            BarChart chart = findViewById(R.id.pillsBarChart);
            chart.setData(data);
            chart.getDescription().setEnabled(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setGranularity(1f); // Only show integer labels
            chart.getXAxis().setValueFormatter(new WeekAxisValueFormatter());
            chart.getAxisLeft().setGranularity(1f); // Optional: make left Y-axis only show integer values
            chart.getAxisRight().setEnabled(false);
            chart.animateY(1000);
            chart.invalidate(); // Refresh the chart
        } else {
            BarChart chart = findViewById(R.id.pillsBarChart);
            chart.clear();
            chart.setNoDataText("No pills data available");
        }
    }

    public class WeekAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            // Example implementation - adjust based on how you want to display the weeks
            return "Week " + ((int) value + 1);
        }
    }

    private int fetchPillsTakenForWeek(int weekOffset) {
        // Placeholder implementation
        return 0; // You need to implement this
    }

    // Any additional methods or inner classes should be included before this closing bracket
}
