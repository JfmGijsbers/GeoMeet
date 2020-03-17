package com.group02tue.geomeet;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationViewer extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLng location;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(
                    getBaseContext(),
                    "Grant GPS permission to GeoMeet to use this functionality", Toast.LENGTH_LONG).show();

            Intent locationIntent = new Intent(this, Dashboard.class);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(locationIntent);
        }


        this.location = new LatLng(51.7, 5.3);

//        // for which location is the activity started (#######OWN LOCATION)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location locFound) {
                        // Got last known location. In some rare situations this can be null.
                        if (locFound != null) {
                            location = new LatLng(locFound.getLatitude(), locFound.getLongitude());
                        }
                    }
                });

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

        //give an option to show the user's location
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e){}

        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);
        location = new LatLng(51.7, 5.3);
        //first move and zoom a bit
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 7.0));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        //then do the rest when map is loaded
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                // Add a marker and move+zoom the camera
                mMap.addMarker(new MarkerOptions().position(location).title("Meeting location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, (float) 11.0), 3000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        System.out.println("geen idee wat dit is");
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });


    }
}
