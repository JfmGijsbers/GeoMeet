package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSyncEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Dashboard extends AppCompatActivity implements MeetingSyncEventListener {
    private ListView meetingOverviewList;
    private List<Meeting> meetings;
    private MeetingManager.MeetingSyncManager meetingSyncManager;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        meetingSyncManager = ((MainApplication) getApplication()).getMeetingSyncManager();
        meetings = meetingSyncManager.getLocalMeetingMemberships();
        final MeetingListAdapter listAdapter = new MeetingListAdapter(Dashboard.this,
                meetings);
        meetingOverviewList = (ListView) findViewById(R.id.meetingListView);
        meetingOverviewList.setAdapter(listAdapter);
        meetingOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MeetingsOverview.this, "You clicked at " +
                //        countryList[position], Toast.LENGTH_SHORT).show();
                Meeting meeting = listAdapter.getItem(position);
                toMeeting(meeting);
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
     * Create the options menu:
     */
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

    /**
     * Below this comment are all methods that simply refer the app to a different activity
     */
    private void toProfile() {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);
    }

    private void logout() {
        ((MainApplication) getApplication()).reset();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private void toSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void toMeeting() {
        Intent meetingIntent = new Intent(this, SeeMeeting.class);
        startActivity(meetingIntent);
    }

    public void toAllMeetings(View view) {
        Intent meetingOverviewIntent = new Intent(this, MeetingsOverview.class);
        startActivity(meetingOverviewIntent);
    }

    public void toInvites(View view) {
        Intent invitesIntent = new Intent(this, MeetingInvites.class);
        startActivity(invitesIntent);
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


    @Override
    protected void onStart() {
        super.onStart();
        meetingSyncManager.addListener(this);
        meetingSyncManager.syncMeetingMemberships();
    }

    @Override
    protected void onStop() {
        super.onStop();
        meetingSyncManager.removeListener(this);
    }

    public void newMeeting(View view) {
        Intent newMeeting = new Intent(this, NewMeeting.class);
        startActivity(newMeeting);
    }


    @Override
    public void onMeetingUpdatedReceived(Meeting meeting) {
        synchronized (meetings) {
            boolean updated = false;
            for (int i = 0; i < meetings.size(); i++) {
                if (meetings.get(i).getId().equals(meeting.getId())) {
                    meetings.set(i, meeting);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                meetings.add(meeting);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MeetingListAdapter) meetingOverviewList.getAdapter()).notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onLeftMeeting(UUID id) {

    }

    @Override
    public void onFailure(UUID id, String reason) {

    }

    @Override
    public void onReceivedNewMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
    }

    private void toMeeting(Meeting meeting) {
        Intent meetingIntent = new Intent(this, SeeMeeting.class);
        putMeetingInIntent(meetingIntent, meeting);
        startActivity(meetingIntent);
    }

    /**
     * Puts all the required data for the SeeMeeting activity into an intent.
     *
     * @param intent  Intent to put data in
     * @param meeting Meeting to show in the SeeMeeting activity
     */
    public static void putMeetingInIntent(Intent intent, Meeting meeting) {
        String name = meeting.getName();
        String description = meeting.getDescription();
        Date date = meeting.getMoment();
        Location2D location = meeting.getLocation();
        String strLocation = location.toString();
        Set<String> members = meeting.getMembers();
        String hostedBy = meeting.getAdminUsername();
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("date", date);
        intent.putExtra("location", strLocation);
        intent.putExtra("hostedBy", hostedBy);
        intent.putExtra("meetingId", meeting.getId().toString());
        int i = 0;
        for (String member : members) {
            intent.putExtra("member" + i, member);
            i++;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

