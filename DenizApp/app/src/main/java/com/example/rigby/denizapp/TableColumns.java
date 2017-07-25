package com.example.rigby.denizapp;


import java.util.Calendar;
import java.util.Date;

public class TableColumns {

    //private variables
    int _id;
    String _date;
    int _long;
    int _lat;

    // Empty constructor
    public TableColumns(){

    }
    // constructor
    //public TableColumns(int id, int date, int Long ,int lat){
       // this._id = id;
       // this._date = date;
      //  this._long = Long;
       // this._lat = lat;
   // }

    // constructor
    public TableColumns(String date, int Long ,int lat){
        this._date = date;
        this._long = Long;
        this._lat = lat;

    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting date
    public String getDate()
    {
       // return this._date;

        Calendar c = Calendar.getInstance();
        String date = c.getTime().toString();
        return date;
    }

    // setting date
    public void setDate(String date){
        this._date = date;
    }

    // getting long
    public int getLong(){
        return this._long;
    }

    // setting long
    public void setLong(int Long){
        this._long = Long;
    }

    // getting lat
    public int getLat(){
        return this._lat;
    }

    // setting lat
    public void setLat(int lat){
        this._lat = lat;
    }
}
