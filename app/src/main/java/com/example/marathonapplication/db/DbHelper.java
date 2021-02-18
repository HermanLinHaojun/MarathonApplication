package com.example.marathonapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DbHelper.class.getSimpleName();
    /** Name of the database file */
    private static final String DATABASE_NAME = "marathon.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 4;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_USER_TABLE =  "CREATE TABLE " + TableContract.UserInfoEntry.TABLE_NAME + " ("
                + TableContract.UserInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TableContract.UserInfoEntry.COLUMN_USER_ID + " TEXT NOT NULL, "
                + TableContract.UserInfoEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + TableContract.UserInfoEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + TableContract.UserInfoEntry.COLUMN_USER_ADDRESS + " TEXT NOT NULL, "
                + TableContract.UserInfoEntry.COLUMN_PASSWORD + " TEXT NOT NULL);";
        String SQL_CREATE_RACE_INFO_TABLE =  "CREATE TABLE " + TableContract.RaceInfoEntry.TABLE_NAME + " ("
                + TableContract.RaceInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TableContract.RaceInfoEntry.COLUMN_RACE_NAME + " TEXT NOT NULL, "
                + TableContract.RaceInfoEntry.COLUMN_LINE_POINTS + " TEXT NOT NULL, "
                + TableContract.RaceInfoEntry.COLUMN_CREATOR + " TEXT NOT NULL, "
                + TableContract.RaceInfoEntry.COLUMN_START_DATE + " TEXT NOT NULL, "
                + TableContract.RaceInfoEntry.COLUMN_END_DATE + " TEXT NOT NULL );";


        String SQL_CREATE_RACE_DETAIL_TABLE =  "CREATE TABLE " + TableContract.RaceDetailEntry.TABLE_NAME + " ("
                + TableContract.RaceDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TableContract.RaceDetailEntry.COLUMN_RACE_ID + " TEXT NOT NULL, "
                + TableContract.RaceDetailEntry.COLUMN_CREATOR + " TEXT NOT NULL, "
                + TableContract.RaceDetailEntry.COLUMN_PARTICIPANT_ID + " TEXT NOT NULL, "
                + TableContract.RaceDetailEntry.COLUMN_STEP_NUM + " INTEGER NOT NULL DEFAULT 0, "
                + TableContract.RaceDetailEntry.COLUMN_RACE_COST_TIME + " INTEGER NOT NULL DEFAULT 0, "
                + TableContract.RaceDetailEntry.COLUMN_RACE_STATUS + " INTEGER NOT NULL DEFAULT 0);";
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RACE_INFO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RACE_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
