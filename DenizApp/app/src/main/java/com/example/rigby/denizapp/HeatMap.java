package com.example.rigby.denizapp;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import java.util.ArrayList;


public class HeatMap  extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heat_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void addHeatMap() {

        ArrayList<LatLng> list = new ArrayList<LatLng>();


        SQLLiteDBHelper d =new SQLLiteDBHelper(this);
        Cursor rs = d.getLocation();
        {
            while (rs.moveToNext()) {
                LatLng location = new LatLng(rs.getDouble(4), rs.getDouble(3));
                list.add( new LatLng(rs.getDouble(4), rs.getDouble(3)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        location,12));
            }
        }
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().data(list).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    private void addweeklyHeatMap() {

        ArrayList<LatLng> list = new ArrayList<LatLng>();

        java.util.Date date = new java.util.Date();
        String dateString = new java.sql.Timestamp(date.getTime()).toString();
        String today = dateString.substring(0, 6);
        SQLLiteDBHelper d =new SQLLiteDBHelper(this);
        Cursor rs = d.getLocation();
         if ((rs != null) && (rs.getCount() > 0)){
            while (rs.moveToNext()) {

                String dd = rs.getString(1).substring(0, 6);
                if ( dd.equals(today)) {
                LatLng location = new LatLng(rs.getDouble(4), rs.getDouble(3));
                list.add( new LatLng(rs.getDouble(4), rs.getDouble(3)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        location,13));}
            }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().data(list).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));}
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addweeklyHeatMap();
    }


}
