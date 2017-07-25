package com.example.rigby.denizapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;
import java.util.Date;


public class DB extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "smokingApp";

    //  table name
    private static final String TABLE_User = "userData";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
     static final String DATE = "date";
    private static final String LONG = "long";
    private static final String LAT = "lat";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_User + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATE + " TEXT,"
                + LONG + " DOUBLE," + LAT + " DOUBLE"  + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);

        // Create tables again
        onCreate(db);
    }

    // Adding new record into the table
    public void addRecord(String date, double Long , double lat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
       // values.put(KEY_ID, id);
        values.put(DATE, date);
        //values.put(LONG, Long);
      //  values.put(LAT, lat);

        String INSERT_TABLE = "INSERT INTO " + TABLE_User + "("
                + DATE + "," + LONG + "," + LAT + ")"  + "VALUES" + "( '08:00 am',48.013515,7.830392) , ( '12:00 am',48.015515,7.830392),( '03:00 pm',48.017515,7.834824)";
        db.execSQL(INSERT_TABLE);

        // Inserting Row
        db.insert(TABLE_User, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Records
   // public String getAllRecords() {

        public Cursor getData() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery(  "SELECT * FROM " + TABLE_User, null);
            return res;
    }









}
