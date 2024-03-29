package com.example.aal_appdev_pilldespenser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

// Helper class for managing the SQLite database used for storing patient data
public class PatientDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "PatientDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_PATIENTS = "patients";

    // Patient Table Columns
    // Column names for the patient table
    private static final String KEY_PATIENT_ID = "id";
    private static final String KEY_PATIENT_NAME = "name";
    private static final String KEY_MONDAY_VALUE = "mondayValue";
    private static final String KEY_TUESDAY_VALUE = "tuesdayValue";
    private static final String KEY_WEDNESDAY_VALUE = "wednesdayValue";
    private static final String KEY_THURSDAY_VALUE = "thursdayValue";
    private static final String KEY_FRIDAY_VALUE = "fridayValue";
    private static final String KEY_SATURDAY_VALUE = "saturdayValue";
    private static final String KEY_SUNDAY_VALUE = "sundayValue";

    // Constructor
    public PatientDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS +
                "(" +
                KEY_PATIENT_ID + " INTEGER PRIMARY KEY," +
                KEY_PATIENT_NAME + " TEXT," +
                KEY_MONDAY_VALUE + " INTEGER," +
                KEY_TUESDAY_VALUE + " INTEGER," +
                KEY_WEDNESDAY_VALUE + " INTEGER," +
                KEY_THURSDAY_VALUE + " INTEGER," +
                KEY_FRIDAY_VALUE + " INTEGER," +
                KEY_SATURDAY_VALUE + " INTEGER," +
                KEY_SUNDAY_VALUE + " INTEGER" +
                ")";
        db.execSQL(CREATE_PATIENTS_TABLE);
    }

    // Upgrade the database schema if needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
    }

    // Add a new patient to the database
    public void addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PATIENT_NAME, patient.getName());
        values.put(KEY_MONDAY_VALUE, patient.getMondayValue());
        values.put(KEY_TUESDAY_VALUE, patient.getTuesdayValue());
        values.put(KEY_WEDNESDAY_VALUE, patient.getWednesdayValue());
        values.put(KEY_THURSDAY_VALUE, patient.getThursdayValue());
        values.put(KEY_FRIDAY_VALUE, patient.getFridayValue());
        values.put(KEY_SATURDAY_VALUE, patient.getSaturdayValue());
        values.put(KEY_SUNDAY_VALUE, patient.getSundayValue());

        db.insert(TABLE_PATIENTS, null, values);
        db.close();
    }

    // Retrieve a patient from the database by their name
    public Patient getPatient(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_PATIENT_ID, KEY_PATIENT_NAME, KEY_MONDAY_VALUE, KEY_TUESDAY_VALUE, KEY_WEDNESDAY_VALUE, KEY_THURSDAY_VALUE, KEY_FRIDAY_VALUE, KEY_SATURDAY_VALUE, KEY_SUNDAY_VALUE},
                KEY_PATIENT_NAME + "=?", new String[]{name}, null, null, null, null);
        // If a matching patient is found, create a Patient object and return it
        if (cursor != null && cursor.moveToFirst()) {
            Patient patient = new Patient(
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PATIENT_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MONDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TUESDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WEDNESDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_THURSDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FRIDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SATURDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SUNDAY_VALUE))
            );
            cursor.close();
            return patient;
        }
        return null; // If no patient is found, return null
    }

    // Update the value of a specific day for a given patient
    public void updatePatientDayValue(String patientName, String day, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String column = getKeyForDay(day);
        if (column != null) {
            values.put(column, value);
            db.update(TABLE_PATIENTS, values, KEY_PATIENT_NAME + " = ?", new String[]{patientName});
        }
        db.close();
    }

    // Helper method to get the column name for a given day
    private String getKeyForDay(String day) {
        switch (day) {
            case "Monday": return KEY_MONDAY_VALUE;
            case "Tuesday": return KEY_TUESDAY_VALUE;
            case "Wednesday": return KEY_WEDNESDAY_VALUE;
            case "Thursday": return KEY_THURSDAY_VALUE;
            case "Friday": return KEY_FRIDAY_VALUE;
            case "Saturday": return KEY_SATURDAY_VALUE;
            case "Sunday": return KEY_SUNDAY_VALUE;
            default: return null;
        }
    }

    // Retrieve a list of all patient names from the database
    public List<String> getAllPatientNames() {
        List<String> patientNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_PATIENT_NAME}, null, null, null, null, KEY_PATIENT_NAME + " ASC");

        // Iterate through the cursor and add each patient name to the list
        if (cursor.moveToFirst()) {
            do {
                patientNames.add(cursor.getString(cursor.getColumnIndex(KEY_PATIENT_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return patientNames;
    }
}
