package com.example.rigby.denizapp;

/**
 * Created by rigby on 27.06.17.
 */

public class TimeAndLocation {

    private long id;
    private String time;
    private String nicotin;
    private double Long;
    private double Lat;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNicotin() {
        return nicotin;
    }

    public void setNicotin(String nicotin) {
        this.nicotin = nicotin;
    }

    public double getLong() {
        return Long;
    }

    public double getLat() {
        return Lat;
    }

    public void setLocationLong(double Long) {
        this.Long = Long;
    }

    public void setLocationLat(double Lat) {
        this.Lat = Lat;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}