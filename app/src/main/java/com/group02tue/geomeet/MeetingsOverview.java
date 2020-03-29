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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MeetingsOverview extends AppCompatActivity implements MeetingSyncEventListener {
    private ListView meetingOverviewList;
    private List<Meeting> meetings;
    private MeetingManager.MeetingSyncManager meetingSyncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_overview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();
        meetings = meetingSyncManager.getMeetingMemberships();
        final MeetingListAdapter listAdapter = new MeetingListAdapter(MeetingsOverview.this,
                meetings);
        meetingOverviewList = (ListView) findViewById(R.id.fullMeetingListView);
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
            case android.R.id.home:
                back();
                return true;
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
    private void back() {
        Intent backIntent = new Intent(this, Dashboard.class);
        startActivity(backIntent);
    }
    private void toProfile() {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);
    }
    private void logout() {
        ((MainApplication)getApplication()).reset();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
    private void toSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    protected void onStart() {
        Log.println(Log.DEBUG, "Debug3", "1");
        super.onStart();
        meetingSyncManager.addListener(this);
        Log.println(Log.DEBUG, "Debug3", "2");
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
        Log.println(Log.DEBUG, "Debug3", "3");
        synchronized (meetings) {
            boolean updated = false;
            for (int i = 0; i < meetings.size(); i++) {
                if (meetings.get(i).getId().equals(meeting.getId())) {
                    meetings.set(i, meeting);
                    updated = true;
                    break;
                }
            }
            Log.println(Log.DEBUG, "Debug3", "4");
            if (!updated) {
                meetings.add(meeting);
            }
            Log.println(Log.DEBUG, "Debug3", "5");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MeetingListAdapter)meetingOverviewList.getAdapter()).notifyDataSetChanged();
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
     * @param intent Intent to put data in
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
        for (String member: members) {
            intent.putExtra("member" + i, member);
            i++;
        }
    }
}
