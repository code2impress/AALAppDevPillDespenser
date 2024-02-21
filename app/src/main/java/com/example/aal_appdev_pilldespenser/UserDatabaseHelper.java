package com.example.aal_appdev_pilldespenser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Calendar; // Import Calendar


public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "user_type";

    // New constants for pill intake table
    private static final String TABLE_PILL_INTAKE = "pill_intake";
    private static final String KEY_DATE = "date";
    private static final String KEY_PILLS_TAKEN = "pills_taken";

    Calendar cal = Calendar.getInstance();
    int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

    private static final String KEY_WEEK_OF_YEAR = "week_of_year";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USERNAME + " TEXT," +
                KEY_PASSWORD + " TEXT," +
                KEY_USER_TYPE + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create pill intake table
        String CREATE_PILL_INTAKE_TABLE = "CREATE TABLE " + TABLE_PILL_INTAKE + "("
                + KEY_USER_ID + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_WEEK_OF_YEAR + " INTEGER," // New column for week of the year
                + KEY_PILLS_TAKEN + " INTEGER,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "))";
        db.execSQL(CREATE_PILL_INTAKE_TABLE);

        // Add initial users (permanent doctor and a sample patient)
        addPermanentDoctorUser(db);
        addSamplePatientUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL_INTAKE);
        // Recreate tables
        onCreate(db);

        if (oldVersion < 2) {
            // If upgrading from version 1 to 2, apply necessary changes for version 2
            // For example, adding a new column, creating a new table, etc.
            // Since the pill intake table is introduced in this version, create it if not existing
            String CREATE_PILL_INTAKE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PILL_INTAKE + "("
                    + KEY_USER_ID + " INTEGER,"
                    + KEY_DATE + " TEXT,"
                    + KEY_PILLS_TAKEN + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "))";
            db.execSQL(CREATE_PILL_INTAKE_TABLE);
        }

        if (oldVersion < 4) { // Assuming the current version is 4 after adding the week_of_year
            // Add week_of_year column if not exists
            String alterTable = "ALTER TABLE " + TABLE_PILL_INTAKE + " ADD COLUMN " + KEY_WEEK_OF_YEAR + " INTEGER DEFAULT 0";
            db.execSQL(alterTable);
        }
    }

    // Method to add a user to the database
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_USER_TYPE, user.getUserType());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Method to validate user credentials
    public String validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ID, KEY_USERNAME, KEY_PASSWORD, KEY_USER_TYPE},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?", new String[]{username, password}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String userType = cursor.getString(cursor.getColumnIndex(KEY_USER_TYPE));
            cursor.close();
            return userType;
        }
        return null;
    }

    // Method to get user ID by username
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ID},
                KEY_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));
            cursor.close();
            return userId;
        }
        return -1; // Indicates user not found or error
    }

    // Method to add pill intake data to the database
    public void addPillIntake(int userId, String date, int weekOfYear, int pillsTaken) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_DATE, date);
        values.put(KEY_WEEK_OF_YEAR, weekOfYear); // Save the week of the year
        values.put(KEY_PILLS_TAKEN, pillsTaken);
        db.insert(TABLE_PILL_INTAKE, null, values);
        db.close();
    }

    // Method to get the total number of pills taken by a user for a specific week of the year
    public int getPillsTakenForWeekOfYear(int userId, int weekOfYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalPills = 0;

        // Query to sum up pills taken by the user for the specified week of the year
        String query = "SELECT SUM(" + KEY_PILLS_TAKEN + ") AS total FROM " + TABLE_PILL_INTAKE +
                " WHERE " + KEY_USER_ID + " = ? AND " + KEY_WEEK_OF_YEAR + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(weekOfYear)});

        if (cursor.moveToFirst()) {
            totalPills = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return totalPills;
    }

    // Method to get the total number of pills taken by a user
    public int getTotalPillsTakenByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalPills = 0;

        // Query to sum up all pills taken by the user
        String query = "SELECT SUM(" + KEY_PILLS_TAKEN + ") AS total FROM " + TABLE_PILL_INTAKE + " WHERE " + KEY_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            totalPills = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return totalPills;
    }

    // Method to add a permanent doctor user if not already exists
    private void addPermanentDoctorUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, "docadmin");
        values.put(KEY_PASSWORD, "admin"); // Consider hashing the password in a real app
        values.put(KEY_USER_TYPE, "doctor");

        // Check if the user already exists
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ID},
                KEY_USERNAME + "=?", new String[]{"docadmin"}, null, null, null);

        // If the user does not exist, insert the new user
        if (!cursor.moveToFirst()) {
            db.insert(TABLE_USERS, null, values);
        }
        cursor.close();
    }

    // Method to add a sample patient user with pill intake data for the last 10 weeks
    private void addSamplePatientUser(SQLiteDatabase db) {
        ContentValues patientValues = new ContentValues();
        patientValues.put(KEY_USERNAME, "patient0");
        patientValues.put(KEY_PASSWORD, "user"); // Hash the password in a real app
        patientValues.put(KEY_USER_TYPE, "patient");
        long patientId = db.insert(TABLE_USERS, null, patientValues);

        if (patientId != -1) {
            // Assuming the current week of the year is the starting point
            int currentWeekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

            for (int i = 0; i < 10; i++) { // Last 10 weeks
                ContentValues intakeValues = new ContentValues();
                intakeValues.put(KEY_USER_ID, patientId);
                intakeValues.put(KEY_DATE, "2024-01-01"); // Use a dummy date, adjust accordingly
                intakeValues.put(KEY_WEEK_OF_YEAR, currentWeekOfYear - i);
                intakeValues.put(KEY_PILLS_TAKEN, 20 + (i * 10)); // Example to vary pills from 20-100
                db.insert(TABLE_PILL_INTAKE, null, intakeValues);
            }
        }
    }
}
