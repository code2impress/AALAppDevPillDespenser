package com.example.aal_appdev_pilldespenser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PatientDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "PatientDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_PATIENTS = "patients";

    // Patient Table Columns
    private static final String KEY_PATIENT_ID = "id";
    private static final String KEY_PATIENT_NAME = "name";
    private static final String KEY_MONDAY_VALUE = "mondayValue";
    private static final String KEY_TUESDAY_VALUE = "tuesdayValue";
    private static final String KEY_WEDNESDAY_VALUE = "wednesdayValue";
    private static final String KEY_THURSDAY_VALUE = "thursdayValue";
    private static final String KEY_FRIDAY_VALUE = "fridayValue";
    private static final String KEY_SATURDAY_VALUE = "saturdayValue";
    private static final String KEY_SUNDAY_VALUE = "sundayValue";

    public PatientDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
    }

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

    public Patient getPatient(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[] { KEY_PATIENT_NAME, KEY_MONDAY_VALUE, KEY_TUESDAY_VALUE, KEY_WEDNESDAY_VALUE, KEY_THURSDAY_VALUE, KEY_FRIDAY_VALUE, KEY_SATURDAY_VALUE, KEY_SUNDAY_VALUE },
                KEY_PATIENT_NAME + "=?", new String[] { name }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Patient patient = new Patient(
                    cursor.getString(cursor.getColumnIndex(KEY_PATIENT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(KEY_MONDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_TUESDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_WEDNESDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_THURSDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_FRIDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_SATURDAY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_SUNDAY_VALUE))
            );
            cursor.close();
            return patient;
        }
        return null;
    }

    // Additional methods for updating, deleting, and fetching patients can be added here
}
