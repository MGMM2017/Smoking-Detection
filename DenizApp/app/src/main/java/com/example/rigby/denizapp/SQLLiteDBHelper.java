package com.example.rigby.denizapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLLiteDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_TIMELOCATION = "timeandlocation";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_NICOTIN = "nicotin";
    public static final String COLUMN_LOCATION_LONG = "long";
    public static final String COLUMN_LOCATION_LAT = "lat";



    private static final String DATABASE_NAME = "timeandlocation.db";
    private static final int DATABASE_VERSION = 8;

    // Database creation sql statement
    private static final String DATABASE_CREATE =  "CREATE TABLE IF NOT EXISTS "
            + TABLE_TIMELOCATION + "( " + COLUMN_ID + " integer PRIMARY KEY, "
            + COLUMN_TIME + " TEXT, " +  COLUMN_NICOTIN +" TEXT, " + COLUMN_LOCATION_LONG + " DOUBLE, " + COLUMN_LOCATION_LAT + " DOUBLE " + ")";


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

    public void addRecord(String date, double Long , double lat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, date);
        values.put(COLUMN_NICOTIN,1);

        values.put(COLUMN_LOCATION_LONG, Long);
        values.put(COLUMN_LOCATION_LAT, lat);

       // String INSERT_TABLE = "INSERT INTO timeandlocation (time,long,lat) VALUES ( '08:00 am',48.013515,7.830392) , ( '12:00 am',48.015515,7.830392),( '03:00 pm',48.017515,7.834824)";
       // db.execSQL(INSERT_TABLE);

        // Inserting Row
        db.insert(TABLE_TIMELOCATION, null, values);
        db.close(); // Closing database connection

    }

    // returns all records for further parsing mainly for location data
    public Cursor getLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(  "SELECT * FROM timeandlocation" , null);
        return res;
    }

}


