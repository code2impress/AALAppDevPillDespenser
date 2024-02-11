package com.example.aal_appdev_pilldespenser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "user_type";

    // New constants for pill intake table
    private static final String TABLE_PILL_INTAKE = "pill_intake";
    private static final String KEY_DATE = "date";
    private static final String KEY_PILLS_TAKEN = "pills_taken";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USERNAME + " TEXT," +
                KEY_PASSWORD + " TEXT," +
                KEY_USER_TYPE + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PILL_INTAKE_TABLE = "CREATE TABLE " + TABLE_PILL_INTAKE + "("
                + KEY_USER_ID + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_PILLS_TAKEN + " INTEGER,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "))";
        db.execSQL(CREATE_PILL_INTAKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL_INTAKE);
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
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_USER_TYPE, user.getUserType());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

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

    public void addPillIntake(int userId, String date, int pillsTaken) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_DATE, date);
        values.put(KEY_PILLS_TAKEN, pillsTaken);
        db.insert(TABLE_PILL_INTAKE, null, values);
        db.close();
    }

    public int getPillsTakenInWeek(int userId, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + KEY_PILLS_TAKEN + ") as total FROM " + TABLE_PILL_INTAKE
                + " WHERE " + KEY_USER_ID + "=? AND " + KEY_DATE + " BETWEEN ? AND ?", new String[]{String.valueOf(userId), startDate, endDate});
        int totalPills = 0;
        if (cursor.moveToFirst()) {
            totalPills = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return totalPills;
    }
}
