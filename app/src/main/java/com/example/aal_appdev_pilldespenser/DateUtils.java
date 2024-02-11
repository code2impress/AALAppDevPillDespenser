package com.example.aal_appdev_pilldespenser;
import java.text.SimpleDateFormat;
import java.util.Locale;


import java.util.Calendar;

public class DateUtils {

    public static String getStartOfWeek() {
        Calendar calendar = Calendar.getInstance();
        // Set the calendar to the first day of the week (Sunday in the US, Monday in France, etc.)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    public static String getEndOfWeek() {
        Calendar calendar = Calendar.getInstance();
        // Move the calendar to the last day of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DAY_OF_MONTH, 6); // Add 6 days to get to the end of the week
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }
}
