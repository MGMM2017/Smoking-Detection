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
    public static final String COLUMN_LOCATION = "location";



    private static final String DATABASE_NAME = "timeandlocation.db";
    private static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    private static final String DATABASE_CREATE =  "CREATE TABLE IF NOT EXISTS "
            + TABLE_TIMELOCATION + "( " + COLUMN_ID + " integer PRIMARY KEY, "
            + COLUMN_TIME + " TEXT, "+  COLUMN_NICOTIN +" TEXT, " + COLUMN_LOCATION+ " TEXT )";

    public SQLLiteDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(
                "create table timeandlocation " +
                        "(id integer primary key, time text,nicotin text,location text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLLiteDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMELOCATION);
        onCreate(db);
    }

    /*public ArrayList<String> getAllTimeAndLocation() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from timeandlocation", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_TIME)));
            res.moveToNext();
        }
        return array_list;
    }*/

}