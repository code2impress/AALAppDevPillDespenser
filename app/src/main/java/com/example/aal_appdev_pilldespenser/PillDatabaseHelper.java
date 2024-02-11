package com.example.aal_appdev_pilldespenser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
