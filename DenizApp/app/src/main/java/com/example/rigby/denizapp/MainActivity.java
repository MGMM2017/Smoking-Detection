package com.example.rigby.denizapp;

import android.Manifest;
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
import java.text.DecimalFormat;
import java.text.ParseException;
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
import java.util.Locale;
import java.util.UUID;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

    TextView viewdate;
    TextView numberofcigs;
    TextView costofcigs;
    TextView todaycigs;

    public double cost = 0;
    DecimalFormat numberFormat = new DecimalFormat("#.00");

    //location variables
    double Long=0;
    double Lat=0;
    public LocationRequest mLocationRequest = new LocationRequest();
    FusedLocationProviderClient mFusedLocationClient;
   // GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();


    //geofencing variable
    double visitedlong=0;
    double visitedlat=0;
    PendingIntent mGeofencePendingIntent;
    GeofencingClient mGeofencingClient;
    private List<Geofence> mGeofenceList;

    public static final String TAG = "Activity";


    // DB
    SQLLiteDBHelper tt = new SQLLiteDBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlayout);


        getmostvisitedlatlong();
        viewdate = (TextView) findViewById(R.id.editText);
        numberofcigs = (TextView) findViewById(R.id.editText2);
        costofcigs = (TextView) findViewById(R.id.editText7);
        todaycigs = (TextView) findViewById(R.id.editText4);



        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceList = new ArrayList<Geofence>();
        String id = UUID.randomUUID().toString();
        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(id)
                .setCircularRegion(visitedlat,visitedlong,100)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT).build());
        addGeofence();


        getLocation();

        viewdate.setText(addDate());
        numberofcigs.setText(String.valueOf(cigssmoked()));
        cost = cigssmoked() * 0.35;
        costofcigs.setText(numberFormat.format(cost) + "$");
        todaycigs.setText(String.valueOf(cigssmokedtoday()));


    }



    //geofencing

    //create the geofence area
    public List<Geofence> createGeofences(double latitude, double longitude) {
        String id = UUID.randomUUID().toString();
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, 50)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);
        return mGeofenceList;
    }

    //receives the geofence area to be monitered and what action to trigger
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
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;

    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.e("Great news :","Geofence added");
                    }})
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...

                        Log.e("Error :","Geofence Failed ");
                    }});
    }



    //location
    void getLocation()
    {
        // google maps api get location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                        } else

                            Log.e(" :","");
                    }
                });
    }


    // this gets the last time somked a cig and sets the first text box to it, if DB is empty gets the current time
    private String addDate() {
        DateFormat sdf = new SimpleDateFormat("HH:mm a");
        Cursor rs = tt.getLocation();
        if((rs != null) && (rs.getCount() > 0)) {
            rs.moveToLast();
            String string = rs.getString(1);
            return string.substring(10,16);}
        else {
            String currentDateandTime = sdf.format(new Date());
            return currentDateandTime;}
    }

    // this only gets the current number of cigs smoked so far
    int  cigssmoked() {
        Cursor rs = tt.getLocation();
        if ((rs != null) && (rs.getCount() > 0))
            return rs.getCount();
        else
        return 0;
    }
    int  cigssmokedtoday() {
        java.util.Date date = new java.util.Date();
        String dateString = new java.sql.Timestamp(date.getTime()).toString();
        String today = dateString.substring(0, 10);
        int a=0;
        Cursor rs = tt.getLocation();
        if ((rs != null) && (rs.getCount() > 0)){
        while (rs.moveToNext()) {
            String d = rs.getString(1);
            String dd = d.substring(0, 10);
            if ( dd.equals(today)) {
                a++;}}
            return a;}
        else return 0;}

    // calls the add record method to add a cigarette manually
    public void addRecord(View view) {

        //get the current location
        getLocation();
        // add the time and current location to the database
       tt.addRecord(getTime(),Long,Lat);
        viewdate.setText(addDate());
        numberofcigs.setText(String.valueOf(cigssmoked()));
        cost= cigssmoked() * 0.35;
        costofcigs.setText(numberFormat.format(cost)+"$");
        todaycigs.setText(String.valueOf(cigssmokedtoday()));

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


    // find the post visited location to create a geofence over it
    public void getmostvisitedlatlong()
    {
        Cursor rs = tt.getLocation();
        int length= rs.getCount();
        String[] LongLat= new String [length];
        int s=0;


        if ((rs != null) && (rs.getCount() > 0)) {
            while (rs.moveToNext()) {
                LongLat[s] = rs.getDouble(4) + "," + rs.getDouble(3);
                Log.d("value of array", LongLat[s]);
                s++;
            }
            int count = 1, tempCount;
            String popular = LongLat[0];
            String temp;
            for (int i = 0; i < (LongLat.length - 1); i++) {
                temp = LongLat[i];
                tempCount = 0;
                for (int j = 1; j < LongLat.length; j++) {
                    if (temp.equals(LongLat[j]))
                        tempCount++;
                }
                if (tempCount > count) {
                    popular = temp;
                    count = tempCount;
                }
            }
            String[] popularlocation= {""};
            popularlocation=popular.split(",");
            visitedlat= Double.valueOf((popularlocation[0]));
            visitedlong=Double.valueOf(popularlocation[1]);
        }
        else
            return;
    }
}