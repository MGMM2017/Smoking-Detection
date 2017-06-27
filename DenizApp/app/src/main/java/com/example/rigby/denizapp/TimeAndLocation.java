package com.example.rigby.denizapp;

/**
 * Created by rigby on 27.06.17.
 */

public class TimeAndLocation {

    private long id;
    private String time;
    private String nicotin;
    private String location;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNicotin() {
        return nicotin;
    }

    public void setNicotin(String date) {
        this.nicotin = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
