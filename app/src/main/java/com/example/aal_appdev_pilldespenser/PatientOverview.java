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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        setupBarChart(); // Draw the bar chart
    }

    private void updatePillsTakenData() {
        // Existing implementation...
    }

    private void setupBarChart() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username == null) return;

        UserDatabaseHelper userDbHelper = new UserDatabaseHelper(this);
        int userId = userDbHelper.getUserIdByUsername(username);
        if (userId == -1) return;

        PillDatabaseHelper pillDbHelper = new PillDatabaseHelper(this);
        ArrayList<BarEntry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 9; i >= 0; i--) {
            // Calculate the start and end of each week
            calendar.add(Calendar.WEEK_OF_YEAR, -i);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String startOfWeek = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            String endOfWeek = sdf.format(calendar.getTime());

            int totalPills = pillDbHelper.getTotalPillsTakenByUserForDateRange(userId, startOfWeek, endOfWeek);
            entries.add(new BarEntry(10 - i, totalPills));

            // Reset calendar to current date for next iteration
            calendar.setTime(new Date());
            calendar.add(Calendar.WEEK_OF_YEAR, i);
        }

        if (!entries.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entries, "Pills Taken");
            dataSet.setColor(Color.BLUE);
            BarData data = new BarData(dataSet);

            BarChart chart = findViewById(R.id.pillsBarChart);
            chart.setData(data);
            chart.getDescription().setEnabled(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getAxisRight().setEnabled(false);
            chart.animateY(1000);
            chart.invalidate();
        } else {
            BarChart chart = findViewById(R.id.pillsBarChart);
            chart.clear();
            chart.setNoDataText("No pills data available");
        }
    }


    private int fetchPillsTakenForWeek(int weekOffset) {
        // Placeholder implementation
        return 0; // You need to implement this
    }

    // Any additional methods or inner classes should be included before this closing bracket
}
