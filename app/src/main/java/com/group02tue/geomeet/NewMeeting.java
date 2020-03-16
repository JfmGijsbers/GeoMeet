package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingAsAdminManager;
import com.group02tue.geomeet.backend.social.MeetingEventListener;
import com.group02tue.geomeet.backend.social.MeetingManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class NewMeeting extends AppCompatActivity implements  MeetingEventListener {
    private EditText etName, etLocation, etDescription;
    private EditText etDay, etMonth, etYear, etHour, etMinute;
    private Button btnCreate;
    private ListView connectionList;
    private MeetingManager meetingManager;
    private AuthenticationManager authenticationManager;

    private UserListItem[] testList = {
            new UserListItem("user", new ExternalUserProfile("a", "b", "email", "test")) };

    private UUID createdMeetingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        meetingManager = ((MainApplication)getApplication()).getMeetingManager();
        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();

        String date = "03" + "03" + "2020" + "13" + "30";
        etName = findViewById(R.id.et_name);
        etLocation = findViewById(R.id.et_location);
        etDescription = findViewById(R.id.et_description);
        connectionList = findViewById(R.id.connectionListView);
        btnCreate = findViewById(R.id.btn_create);

        etDay = findViewById(R.id.et_day);
        etMonth = findViewById(R.id.et_month);
        etYear = findViewById(R.id.et_year);
        etHour = findViewById(R.id.et_hour);
        etMinute = findViewById(R.id.et_minute);

        // Initialize list adapter
        ConnectionListAdapter listAdapter = new ConnectionListAdapter(NewMeeting.this,
                new ArrayList<UserListItem>());
        connectionList.setAdapter(listAdapter);
        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UserListItem userClicked = (UserListItem)parent.getItemAtPosition(position);
                // TODO
            }
        });
        listAdapter.add(testList[0]);
        // TODO: retrieve all current user's connections and add them to the listview
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingManager.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        meetingManager.removeListener(this);
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
                    authenticationManager, meetingManager.getMeeting(id));
            for (int i = 0; i < connectionList.getAdapter().getCount(); i++) {
                String username = ((UserListItem)connectionList.getAdapter().getItem(i)).getUsername();
                adminManager.inviteUserToMeeting(username);
                // TODO: loop over list items and add users
            }

            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
            // TODO: switch dashboard to MyMeetings
        }
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
    public void onMeetingUpdatedReceived(Meeting meeting) {
    }

    @Override
    public void onLeftMeeting(UUID id) {
    }

    @Override
    public void onReceivedMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
    }

    @Override
    public void onRemovedMeeting(UUID id) {
    }
}
