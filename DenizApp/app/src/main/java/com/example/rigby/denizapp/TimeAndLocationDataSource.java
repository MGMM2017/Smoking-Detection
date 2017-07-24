package com.example.rigby.denizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigby on 27.06.17.
 */

public class TimeAndLocationDataSource {


    // Database fields
    private SQLiteDatabase database;
    private SQLLiteDBHelper dbHelper;
    private String[] allColumns = { SQLLiteDBHelper.COLUMN_ID,
            SQLLiteDBHelper.COLUMN_TIME, SQLLiteDBHelper.COLUMN_NICOTIN, SQLLiteDBHelper.COLUMN_LOCATION_LONG, SQLLiteDBHelper.COLUMN_LOCATION_LAT };

    public TimeAndLocationDataSource(Context context) {
        dbHelper = new SQLLiteDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public TimeAndLocation createTime(String time, String nicotin, double Long , double Lat) {
        ContentValues values = new ContentValues();
        values.put(SQLLiteDBHelper.COLUMN_TIME, time);
        values.put(SQLLiteDBHelper.COLUMN_NICOTIN, nicotin);
        values.put(SQLLiteDBHelper.COLUMN_LOCATION_LONG, Long);
        values.put(SQLLiteDBHelper.COLUMN_LOCATION_LAT, Lat);

        long insertId = database.insert(SQLLiteDBHelper.TABLE_TIMELOCATION, null,
                values);

        Cursor cursor = database.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_TIMELOCATION,null);
        cursor.moveToFirst();
        TimeAndLocation newTimeAndLoc = cursorToTime(cursor);
        cursor.close();
        return newTimeAndLoc;
    }

    public void deleteTime(TimeAndLocation time) {
        long id = time.getId();
        database.delete(SQLLiteDBHelper.TABLE_TIMELOCATION, SQLLiteDBHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<TimeAndLocation> getAllTimeAndLoc() {
        List<TimeAndLocation> timeAndLocs = new ArrayList<TimeAndLocation>();

        Cursor cursor =  database.rawQuery( "select * from timeandlocation", null );


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeAndLocation timeAndLoc = cursorToTime(cursor);
            timeAndLocs.add(timeAndLoc);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return timeAndLocs;
    }

    private TimeAndLocation cursorToTime(Cursor cursor) {
        TimeAndLocation timeAndLoc = new TimeAndLocation();
        timeAndLoc.setId(cursor.getLong(0));
        timeAndLoc.setTime(cursor.getString(1));
        timeAndLoc.setNicotin(cursor.getString(2));
        timeAndLoc.setLocationLong(cursor.getDouble(3));
        timeAndLoc.setLocationLat(cursor.getDouble(4));
        return timeAndLoc;
    }

}