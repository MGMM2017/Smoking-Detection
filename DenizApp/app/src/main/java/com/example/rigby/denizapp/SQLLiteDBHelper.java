package com.example.rigby.denizapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rigby on 27.06.17.
 */

public class SQLLiteDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_TIMELOCATION = "timeandlocation";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_NICOTIN = "nicotin";
    public static final String COLUMN_LOCATION_LONG = "long";
    public static final String COLUMN_LOCATION_LAT = "lat";



    private static final String DATABASE_NAME = "timeandlocation.db";
    private static final int DATABASE_VERSION = 10;

    // Database creation sql statement
    private static final String DATABASE_CREATE =  "CREATE TABLE IF NOT EXISTS "
            + TABLE_TIMELOCATION + "( " + COLUMN_ID + " integer PRIMARY KEY, "
            + COLUMN_TIME + " TEXT," +  COLUMN_NICOTIN +" TEXT," + COLUMN_LOCATION_LONG + " DOUBLE," + COLUMN_LOCATION_LAT + " DOUBLE" + ")";


    public SQLLiteDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLLiteDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMELOCATION);
        onCreate(db);
    }


}