package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.widget.Toast;

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

        MeetinglistAdapter listAdapter = new MeetinglistAdapter(Dashboard.this,
                countryList, imageId);
        list = (ListView) findViewById(R.id.meetingListView);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Dashboard.this, "You clicked at " +
                        countryList[position], Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void chat(View view) {
        Toast.makeText(Dashboard.this, "Chat not implemented yet", Toast.LENGTH_SHORT)
                .show();
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
        // TODO: alertDialog
        logout();
    }

    /**
     * Below this comment are all methods that simply refer the app to a different activity
     */
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
    }

    public void toConnections(View view) {
        Intent connectionIntent = new Intent(this, MyConnections.class);
        startActivity(connectionIntent);
    }

    public void toGroups(View view) {
    }

}

