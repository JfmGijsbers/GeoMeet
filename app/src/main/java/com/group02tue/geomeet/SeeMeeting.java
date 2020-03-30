package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

public class SeeMeeting extends AppCompatActivity {
    TextView txtTitle, txtLocation, txtAdmin, txtDate, txtDescription;
    ListView connectionList;
    private UUID meetingId;
    private MeetingManager meetingManager;
    private MeetingManager.MeetingSyncManager meetingSyncManager;
    private AuthenticationManager authenticationManager;
    private final ArrayList<String> membersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_meeting);
        meetingManager = ((MainApplication)getApplication()).getMeetingManager();
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();
        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
         * First, we need to retrieve all the data from the intent
         */
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        Date date = (Date) intent.getSerializableExtra("date");
        String strLocation = intent.getStringExtra("location");
        String hostedBy = intent.getStringExtra("hostedBy");
        meetingId = UUID.fromString(intent.getStringExtra("meetingId"));
        try {
            Location2D location = Location2D.parse(strLocation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String member = "";
        int i = 0;
        while (member != null) {
            member = intent.getStringExtra("member" + i);
            i++;
            if (member != null && !member.isEmpty()) {
                membersList.add(member);
            }
        }
        connectionList = findViewById(R.id.seeMeeting_comingConnectionsListView);
        connectionList.setAdapter(new MembersListAdapter(this, membersList));

        /*
        Then, we need to find our views
         */
        txtTitle = findViewById(R.id.seeMeeting_meetingName);
        txtLocation = findViewById(R.id.seeMeeting_meetingLocation);
        txtAdmin = findViewById(R.id.seeMeeting_organizer);
        txtDate = findViewById(R.id.seeMeeting_date);
        txtDescription = findViewById(R.id.txtDescription);

        /*
        Then, we change the text according to the received data
         */
        txtTitle.setText(name);
        txtLocation.setText(strLocation);
        txtAdmin.setText("Meeting organizer: " + hostedBy);
        txtDate.setText(date.toString());
        txtDescription.setText(description);
    }
    /**
     *  Create the options menu:
     *  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.meeting_menu, menu);
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
            case R.id.delete:
                deleteMeeting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteMeeting() {
        try {
            Meeting meeting = meetingManager.getLocalMeeting(meetingId);
            if (meeting.getAdminUsername().equals(authenticationManager.getUsername())) {
                meetingManager.removeMeeting(meetingId);
                back();
            } else {
                meetingSyncManager.leaveMeeting(meetingId);
                back();
            }
        } catch(NoSuchElementException e) {
            back();
        }
    }

    public void toMap(View view) {
        Intent mapIntent = new Intent(this, LocationViewer.class);
        mapIntent.putExtra("fromSeeMeeting", -1);
        mapIntent.putExtra("location", getIntent().getStringExtra("location"));
        startActivity(mapIntent);
    }

    public void toChat(View view) {
        Intent chatIntent = new Intent(this, MessageListActivity.class);
        chatIntent.putExtra("meetingId", meetingId.toString());
        startActivity(chatIntent);
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
    private void back() {
        Intent backIntent = new Intent(this, MeetingsOverview.class);
        startActivity(backIntent);
    }
}
