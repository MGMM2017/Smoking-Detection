package com.example.rigby.denizapp;

import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.sample.basiclocationsample.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import eu.senseable.sparklib.Spark;

public class MainActivity extends AppCompatActivity {

    Button addData;
    TextView viewdate;
    TextView numberofcigs;
    TextView costofcigs;

    TabHost tabHost;


    //location variables
    double Long;
    double Lat;
    public LocationRequest mLocationRequest = new LocationRequest();
    FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;


    //geofencing variable
    PendingIntent mGeofencePendingIntent;
    GeofencingClient mGeofencingClient;
    private List<Geofence> mGeofenceList;

    public static final String TAG = "Activity";


    // DB
    SQLiteDatabase sqliteDatabase;
    TimeAndLocationDataSource timeandlocatoin = new TimeAndLocationDataSource(this);
    SQLLiteDBHelper tt=new SQLLiteDBHelper(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlayout);


        viewdate = (TextView) findViewById(R.id.editText);
        numberofcigs = (TextView) findViewById(R.id.editText2);
        costofcigs = (TextView) findViewById(R.id.editText7);


        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceList = new ArrayList<Geofence>();


        getLocation();
/*
        //tabhost
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);
*/

        //show the current time
        viewdate.setText(addDate());
        numberofcigs.setText(String.valueOf(cigssmoked()));
        costofcigs.setText(String.valueOf(cigssmoked()*0.35)+"$");

    }



    public void createGeofences(double latitude, double longitude) {
        String id = UUID.randomUUID().toString();
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, 200)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


     private PendingIntent getGeofencePendingIntent() {

        // Reuse the PendingIntent if we already have it.

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);

    }




    void getLocation()
{
    // google maps api get location
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resp == ConnectionResult.SUCCESS) {

        return;
    }
    mFusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Lat = location.getLatitude();
                        Long = location.getLongitude();
                        //total2 = String.valueOf(Lat);
                    } else
                       // LocationServices.FusedLocationApi.requestLocationUpdates(location, mLocationRequest, this);

                        Log.e("Error :","there is no location");
                }
            });
}}

    // this only gets the current time
    private String addDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");

        Cursor rs = tt.getLocation();
        rs.moveToLast();
        String date = rs.getString(1);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    // this only gets the current number of cigs smoked so far
    int  cigssmoked() {
        Cursor rs = tt.getLocation();
        return rs.getCount();
    }

    // calls the add record method to add a cigarette manually
    public void addRecord(View view) {

        //get the current location
        getLocation();
        // add the time and current location to the database
       tt.addRecord(getTime(),Long,Lat);
    }


    public String getTime()
    {
        java.util.Date date = new java.util.Date();
        String dateString = new java.sql.Timestamp(date.getTime()).toString();
        return  dateString;
    }

    // not sure why i need this , i did not use it
    protected void createLocationRequest() {
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //shows the MapActivity when the button is clicked
    public void showMap(View view) {
        Intent showMapintent = new Intent(MainActivity.this , MapsActivity.class);
        startActivity(showMapintent);
    }

    // shows the MapActivity when the button is clicked
    public void showWeeklyHeatMap(View view) {
        Intent showMapintent = new Intent(MainActivity.this , HeatMap.class);
        startActivity(showMapintent);
    }

    public void showgraphs(View view) {
        Intent showMapintent = new Intent(MainActivity.this , graphs.class);
        startActivity(showMapintent);
    }
}