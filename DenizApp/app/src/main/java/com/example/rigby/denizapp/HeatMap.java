package com.example.rigby.denizapp;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import java.util.ArrayList;


public class HeatMap  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    MainActivity object= new MainActivity();
    DB db= new DB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void addHeatMap() {

        ArrayList<LatLng> list = new ArrayList<LatLng>();

        // Get the data: latitude/longitude positions of police stations.
        list.add( new LatLng(48.013515, 7.830392));
        list.add( new LatLng(48.015515, 7.830392));
        list.add( new LatLng(48.017515, 7.834824));
        list.add( new LatLng(48, 7.6));

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().data(list).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        // Get the data: latitude/longitude positions of police stations.
        list.add( new LatLng(48.013515, 7.830392));
        list.add( new LatLng(48.015515, 7.830392));
        list.add( new LatLng(48.017515, 7.834824));
        list.add( new LatLng(48, 7.6));

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().data(list).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));



    }


}
