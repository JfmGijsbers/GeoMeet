package com.group02tue.geomeet;

import androidx.core.app.ActivityCompat;
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
import android.util.Log;
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
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSyncEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LocationViewer extends FragmentActivity implements OnMapReadyCallback, MeetingSyncEventListener {

    private GoogleMap mMap;
    private final List<Location2D> allLocations = new ArrayList<>();
    private Boolean fromSeeMeeting;
    private FusedLocationProviderClient fusedLocationClient;
    private MeetingManager.MeetingSyncManager meetingSyncManager;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();

        //check if GPS permission was granted
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

        if (getIntent().getExtras().getInt("fromSeeMeeting") == -1){
            //the SeeMeeting activity was used to get here, so a specific meeting was selected
            fromSeeMeeting = true;
            //remove the chosen location from the list, because this location gets a dedicated marker
            //allLocations.remove(location);
            try {
                Location2D meetingLocation = Location2D.parse(getIntent().getStringExtra("location"));
                allLocations.add(meetingLocation);
            } catch (ParseException e) {
                Toast.makeText(this, "Failed to load meeting location.", Toast.LENGTH_LONG).show();
            }

        } else {
            //the MyLocations activity was used to get here, so NO specific meeting was selected
            fromSeeMeeting = false;
        }

        //The user's own location, probably superfluous here but handy elsewhere
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location locFound) {
                        // Got last known location. In some rare situations this can be null.
                        if (locFound != null) {
                            //location = new LatLng(locFound.getLatitude(), locFound.getLongitude());
                        }
                    }
                });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingSyncManager.addListener(this);
        if (!fromSeeMeeting) {
            synchronized (allLocations) {
                List<Meeting> meetings = meetingSyncManager.getMeetingMemberships();
                for (Meeting meeting : meetings) {
                    allLocations.add(meeting.getLocation());
                }
                refreshMap();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        meetingSyncManager.removeListener(this);
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        synchronized (allLocations) {
            mMap = googleMap;

            //give an option to show the user's current location
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e){}


            //first move to the chosen location and zoom such that The Netherlands is fully visible
            mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 7.0));
            if (allLocations.size() > 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(allLocations.get(0).getLatLng()));

                //add markers for all meetings and make opacity dependant on whether a specific meeting was selected
                if (!fromSeeMeeting) {
                    for (Location2D loc: allLocations){
                        mMap.addMarker(new MarkerOptions().position(loc.getLatLng())
                                .title(loc.detailedLocation));
                    }
                } else {
                    //if a specific location is selected, zoom further and add special marker
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        public void onMapLoaded() {
                            // Add a marker and move+zoom the camera
                            mMap.addMarker(new MarkerOptions().position(
                                    allLocations.get(0).getLatLng()).title(allLocations.get(0).detailedLocation));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    allLocations.get(0).getLatLng(),
                                    (float) 11.0), 3000, new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
                                }

                                @Override
                                public void onCancel() {
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    private void refreshMap() {
        if (mMap != null) {
            mMap.clear();
            onMapReady(mMap);
        }
    }

    @Override
    public void onMeetingUpdatedReceived(Meeting meeting) {
        synchronized (allLocations) {
            for (int i = allLocations.size() -1; i > -1; i--) {
                if (allLocations.get(i).equals(meeting.getLocation())) {
                    allLocations.remove(i);
                }
            }
            allLocations.add(meeting.getLocation());
            refreshMap();
        }
    }

    @Override
    public void onLeftMeeting(UUID id) {
        // N/A
    }

    @Override
    public void onFailure(UUID id, String reason) {
    }

    @Override
    public void onReceivedMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
        // N/A
    }
}
