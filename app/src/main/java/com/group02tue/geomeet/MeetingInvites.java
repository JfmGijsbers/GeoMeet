package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSyncEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MeetingInvites extends AppCompatActivity implements MeetingSyncEventListener {
    private ListView meetingInvitesList;
    private List<ImmutableMeeting> meetingInvites;
    private MeetingManager.MeetingSyncManager meetingSyncManager;

    private Handler syncMeetingInvitesTimer = new Handler();      // Timer, implemented as handler
    private final static int SYNC_MEETING_INVITES_TIMER_INTERVAL = 5000;  // Run timer code every ... ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_invites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        meetingInvitesList = findViewById(R.id.invites_meeting_list);
        meetingSyncManager = ((MainApplication) getApplication()).getMeetingSyncManager();

        meetingInvites = meetingSyncManager.requestMeetingInvitations();
        final MeetingInvitesListAdapter listAdapter = new MeetingInvitesListAdapter(
                MeetingInvites.this, meetingInvites);
        meetingInvitesList.setAdapter(listAdapter);
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
        ((MainApplication) getApplication()).reset();
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
        super.onStart();
        meetingSyncManager.addListener(this);
        syncMeetingInvitesTimer.postDelayed(runnableCode, SYNC_MEETING_INVITES_TIMER_INTERVAL);   // Start timer
    }

    @Override
    protected void onStop() {
        super.onStop();
        syncMeetingInvitesTimer.removeCallbacksAndMessages(null); // 'Stop' the timer
        meetingSyncManager.removeListener(this);
    }

    /**
     * Timer elapsed callback
     */
    final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            meetingSyncManager.requestMeetingInvitations();
            // Repeat this the same runnable code block again
            syncMeetingInvitesTimer.postDelayed(runnableCode, SYNC_MEETING_INVITES_TIMER_INTERVAL);
        }
    };

    @Override
    public void onReceivedNewMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
        synchronized (meetingInvites) {
            for (ImmutableMeeting meetingInvite : meetings) {
                meetingInvites.add(meetingInvite);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MeetingInvitesListAdapter) meetingInvitesList.getAdapter()).notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onMeetingUpdatedReceived(Meeting meeting) {
    }

    @Override
    public void onLeftMeeting(UUID id) {
    }

    @Override
    public void onFailure(UUID id, String reason) {
    }


}
