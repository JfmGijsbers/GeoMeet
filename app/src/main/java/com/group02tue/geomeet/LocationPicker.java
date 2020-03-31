package com.group02tue.geomeet;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationPicker extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLng pickedLocation;

    private String pickedAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);


//        //check if GPS permission was granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(
                    getBaseContext(),
                    "Grant GPS permission to GeoMeet to use this functionality",
                    Toast.LENGTH_LONG).show();


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            Intent locationIntent = new Intent(this, Dashboard.class);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(locationIntent);
        }

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

        //give an option to show the user's current location
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e){}

        mMap.moveCamera(CameraUpdateFactory.zoomTo(7.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.7,5.1))); //TODO: Waar moet ie heenzoomen


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                pickedLocation = latLng;
                getPickedAddress();
                mMap.addMarker(new MarkerOptions().position(latLng).title(pickedAddress));
            }
        });


    }

    public void onPick(View view) {
        if(pickedLocation == null){
            return;
        }
        Intent data = new Intent();
        Location2D returnLoc = new Location2D((float) pickedLocation.longitude, (float) pickedLocation.latitude, pickedAddress);
        //set the data to pass back
        data.setData(Uri.parse(returnLoc.toString()));
        setResult(RESULT_OK, data);
        //close the activity
        finish();

    }
    private void getPickedAddress(){
        Geocoder geocoder = new Geocoder(this.getBaseContext());
        List<Address> foundAddresses = new ArrayList<>();
        try {
            foundAddresses = geocoder.getFromLocation(
                    pickedLocation.latitude, pickedLocation.longitude, 10);
        }catch(IOException e){pickedAddress = "";}

        pickedAddress = foundAddresses.get(0).getAddressLine(0)
                + ", "
                + foundAddresses.get(0).getAdminArea();

    }



}

