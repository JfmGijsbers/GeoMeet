package com.group02tue.geomeet;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.group02tue.geomeet.backend.Location2D;

public class LocationPicker extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLng pickedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.zoomTo(7.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.7,5.1))); //TODO: Waar moet ie heenzoomen


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                pickedLocation = latLng;
                mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()));
            }
        });


    }

    public void onPick(View view) {
        if(pickedLocation == null){
            return;
        }
        Intent data = new Intent();
        Location2D returnLoc = new Location2D((float) pickedLocation.longitude, (float) pickedLocation.latitude, "");
        //set the data to pass back
        data.setData(Uri.parse(returnLoc.toString()));
        setResult(RESULT_OK, data);
        //close the activity
        finish();

    }


}

