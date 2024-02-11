package com.example.aal_appdev_pilldespenser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PillDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserPillDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PILL_INTAKE = "pill_intake";
    private static final String KEY_DATE = "date";
    private static final String KEY_PILLS_TAKEN = "pills_taken";
    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";

    public PillDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PILL_INTAKE_TABLE = "CREATE TABLE " + TABLE_PILL_INTAKE + "("
                + KEY_USER_ID + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_PILLS_TAKEN + " INTEGER,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "))";
        db.execSQL(CREATE_PILL_INTAKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database version upgrades here
    }

    public int getTotalPillsTakenByUserForDateRange(int userId, String startDate, String endDate) {
        int totalPills = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + KEY_PILLS_TAKEN + ") AS total FROM " + TABLE_PILL_INTAKE
                + " WHERE " + KEY_USER_ID + " = ? AND " + KEY_DATE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), startDate, endDate});

        if (cursor.moveToFirst()) {
            int totalColumnIndex = cursor.getColumnIndex("total");
            if (totalColumnIndex != -1) {
                totalPills = cursor.getInt(totalColumnIndex);
            } else {
                // Handle the error or log it
                Log.e("PillDatabaseHelper", "Column 'total' was not found in the result set.");
            }
        }
        cursor.close();
        return totalPills;
    }


}
