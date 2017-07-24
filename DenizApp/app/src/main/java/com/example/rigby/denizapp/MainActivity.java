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
    TextView t;

    double Long=1;
    double Lat=1;

    PendingIntent mGeofencePendingIntent;
    GeofencingClient mGeofencingClient;
    private List<Geofence> mGeofenceList;

//    Geofence g =  new Geofence.Builder().setCircularRegion(48.013515, 7.830392,100).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |Geofence.GEOFENCE_TRANSITION_EXIT).build();

    // DB
    SQLiteDatabase sqliteDatabase;
   // DB object = new DB(this);
    TimeAndLocationDataSource timeandlocatoin = new TimeAndLocationDataSource(this);
SQLLiteDBHelper tt=new SQLLiteDBHelper(this);
    // google maps api get location
    FusedLocationProviderClient mFusedLocationClient;
    String total2;

    TabHost tabHost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.newlayout);


        //addData = (Button) findViewById(R.id.button2);
       // t = (TextView) findViewById(R.id.t);
        DB db = new DB(this);

        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceList = new ArrayList<Geofence>();


        sqliteDatabase = db.getWritableDatabase();
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



        createGeofences(Lat, Long);

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
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                    }
                });


        //getGeofencingRequest();
        //getGeofencePendingIntent();


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

    @NonNull
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
                        //total2 = String.valueOf(Lat);
                    } else

                        Log.e("Error :","there is no location");
                }
            });
}


    // calls the add record method from the DB class
    public void addRecord(View view) {

        getLocation();
       // String what= addDate();

       // tt.addRecord(what,Long,Lat);
       // timeandlocatoin.createTime(what,Long,Lat);

   // t.setText(String.valueOf(Long));

     //object.addRecord(addDate(),Long,Lat);



    }



    private void printData(String title , String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public String getTime()
    {

        java.util.Date date = new java.util.Date();
        String dateString = new java.sql.Timestamp(date.getTime()).toString();
        return  dateString;
    }



    private DataPoint[] getData() {
        SQLiteDatabase db;
        String[] columns ={"1","Time"};
        Cursor cursor = sqliteDatabase.query("TABLE_User",columns,null,null,null,null,null);
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for(int i=0; i<cursor.getCount(); i++)
        {
           cursor.moveToNext();
            dp[i]= new DataPoint(1,cursor.getInt(1));
        }
        return dp;
    }

    // not sure why i need this , i did not use it
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    // shows the MapActivity when the button is clicked
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

/**
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return numberOfLines;
    }
**/

}