package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ConnectionsManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.group02tue.geomeet.backend.social.InternalUserProfile;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingAsAdminManager;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSemiAdminEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.group02tue.geomeet.backend.social.ConnectionsEventListener;
import com.group02tue.geomeet.backend.social.ProfileEventListener;


public class NewMeeting extends AppCompatActivity implements MeetingSemiAdminEventListener,
        ConnectionsEventListener {
    private EditText etName, etLocation, etDescription;
    private EditText etDay, etMonth, etYear, etHour, etMinute;
    private EditText manualUser;
    private Button btnCreate;
    private ListView connectionList;
    private MeetingManager meetingManager;
    private MeetingManager.MeetingSyncManager meetingSyncManager;
    private AuthenticationManager authenticationManager;
    private ConnectionsManager connectionsManager;

    //private ExternalUserProfile[] testList = {
     //       new ExternalUserProfile("user","a", "b", "email", "test") };

    private UUID createdMeetingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        meetingManager = ((MainApplication)getApplication()).getMeetingManager();
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();
        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();
        connectionsManager = ((MainApplication)getApplication()).getConnectionsManager();

        etName = findViewById(R.id.et_meeting_name);
        etLocation = findViewById(R.id.et_meeting_location);
        etDescription = findViewById(R.id.et_meeting_description);
        connectionList = findViewById(R.id.listview_connections);
        btnCreate = findViewById(R.id.btn_create_meeting);

        etDay = findViewById(R.id.et_day);
        etMonth = findViewById(R.id.et_month);
        etYear = findViewById(R.id.et_year);
        etHour = findViewById(R.id.et_hour);
        etMinute = findViewById(R.id.et_minute);

        manualUser = findViewById(R.id.et_meeting_manual_user);

        // Initialize list adapter
        ConnectionListAdapter listAdapter = new ConnectionListAdapter(NewMeeting.this,
                new ArrayList<String>());
        connectionList.setAdapter(listAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingManager.addListener(this);
        connectionsManager.addListener(this);
        connectionsManager.requestConnections();
    }

    @Override
    protected void onStop() {
        super.onStop();
        meetingManager.removeListener(this);
        connectionsManager.removeListener(this);
    }

    public void addManualUser(View view) {
        String username = String.valueOf(manualUser.getText());
        ((ConnectionListAdapter)connectionList.getAdapter()).add(username);
        ((ConnectionListAdapter) connectionList.getAdapter()).notifyDataSetChanged();
    }

    public void createMeeting(View view) {
        String name = String.valueOf(etName.getText());
        String strLocation = String.valueOf(etLocation.getText());
        String description = String.valueOf(etDescription.getText());

        int day = Integer.parseInt(String.valueOf(etDay.getText()));
        int month = Integer.parseInt(String.valueOf(etMonth.getText()));
        int year = Integer.parseInt(String.valueOf(etYear.getText()));
        int hour = Integer.parseInt(String.valueOf(etHour.getText()));
        int minute = Integer.parseInt(String.valueOf(etMinute.getText()));
        Date meetingMoment = new Date(year, month, day, hour, minute);

        try {
            Location2D location = Location2D.parse(strLocation);
            Meeting meeting = new Meeting(name, description, meetingMoment, location,
                    authenticationManager.getUsername());
            createdMeetingId = meeting.getId();
            meetingManager.addMeeting(meeting);
            btnCreate.setEnabled(false);
        } catch (ParseException e) {
            etLocation.setError("Invalid location");
        }
    }

    public void toAllMeetings(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();   // TODO: code duplication
    }

    @Override
    public void onCreatedMeeting(UUID id) {
        if (id.equals(createdMeetingId)) {
            // Add all invited users to the meeting
            MeetingAsAdminManager adminManager = new MeetingAsAdminManager(meetingManager,
                    authenticationManager, meetingManager.getMeeting(id, meetingSyncManager));
            for (int i = 0; i < connectionList.getAdapter().getCount(); i++) {
                if (((ConnectionListAdapter)connectionList.getAdapter()).isChecked(i)) {
                    String username = (String)connectionList.getAdapter().getItem(i);
                    adminManager.inviteUserToMeeting(username);
                }
            }

            Intent intent = new Intent(this, MeetingsOverview.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRemovedMeeting(UUID id) {

    }

    @Override
    public void onFailure(UUID id, String reason) {
        if (id.equals(createdMeetingId)) {
            createdMeetingId = null;
            Toast.makeText(this, "Failed to create meeting: " + reason,
                    Toast.LENGTH_LONG).show();
            btnCreate.setEnabled(true);
        }
    }

    @Override
    public void onReceivedConnections(final ArrayList<ExternalUserProfile> connections) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ExternalUserProfile profile : connections) {
                    ((ConnectionListAdapter)connectionList.getAdapter()).add(
                            profile.getUsername());
                }
                ((ConnectionListAdapter)connectionList.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}
