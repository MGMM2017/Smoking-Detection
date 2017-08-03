package com.example.rigby.denizapp;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SQLLiteDBHelper d =new SQLLiteDBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
/*
        mMap=googleMap;
        Cursor rs = d.getLocation();
        {
            while (rs.moveToNext()) {
                String date = rs.getString(1);
                LatLng location = new LatLng(rs.getDouble(4), rs.getDouble(3));
                mMap.addMarker(new MarkerOptions().position(location).title(date));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        location,12));
            }
        }
      }*/


        mMap=googleMap;
        java.util.Date date = new java.util.Date();
            String dateString = new java.sql.Timestamp(date.getTime()).toString();
            String today = dateString.substring(0, 10);

            Cursor rs = d.getLocation();
            while (rs.moveToNext()) {
                String d = rs.getString(1);
                String dd = d.substring(0, 10);
                if ( dd.equals(today)) {
                    LatLng location = new LatLng(rs.getDouble(4), rs.getDouble(3));
                    mMap.addMarker(new MarkerOptions().position(location).title(d));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            location,12));
                }

            }


    }




}

