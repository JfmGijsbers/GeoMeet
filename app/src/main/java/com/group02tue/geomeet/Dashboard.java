package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.social.Meeting;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    String countryList[] = {"Sprint retrospection meeting", "Sprint planning", "Lucas' monologue",
            "Sprint 2", "Borrel meeting", "Final sprint"};
    Integer[] imageId = {
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground
    };

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MeetingListAdapter listAdapter = new MeetingListAdapter(Dashboard.this,
                new ArrayList<Meeting>());
        list = (ListView) findViewById(R.id.meetingListView);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = countryList[position];
                toMeeting();
            }
        });

    }


    public void chat(View view) {
        Toast.makeText(Dashboard.this, "Chat not implemented yet", Toast.LENGTH_SHORT)
                .show();
        Intent locationIntent = new Intent(this, LocationViewer.class);
        startActivity(locationIntent);
    }
    /**
     *  Create the options menu:
     *  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    /**
     * Reacting to menu items getting clicked:
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                toProfile();
                return true;
            case R.id.settings:
                toSettings();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Below this comment are all methods that simply refer the app to a different activity
     */
    private void toMeeting() {
        Intent meetingIntent = new Intent(this, SeeMeeting.class);
        startActivity(meetingIntent);
    }
    private void toProfile() {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);
    }
    private void toSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
    private void logout() {
        ((MainApplication)getApplication()).reset();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    public void toAllMeetings(View view) {
        Intent meetingOverviewIntent = new Intent(this, MeetingsOverview.class);
        startActivity(meetingOverviewIntent);
    }

    public void toSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void toProfile(View view) {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);

        /*Intent locationIntent = new Intent(this, LocationViewer.class);
        locationIntent.putExtra("fromSeeMeeting", -1);
        locationIntent.putExtra("location", "61.0@28.0@Test");
        startActivity(locationIntent);*/
    }

    public void toConnections(View view) {
        Intent connectionIntent = new Intent(this, MyConnections.class);
        startActivity(connectionIntent);
    }

    public void toMyLocations(View view) {
        Intent locationIntent = new Intent(this, LocationViewer.class);
        locationIntent.putExtra("fromSeeMeeting", 0);
        startActivity(locationIntent);
    }

    public void toGroups(View view) {
    }
}

