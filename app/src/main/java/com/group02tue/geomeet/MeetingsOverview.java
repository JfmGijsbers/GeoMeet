package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSyncEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MeetingsOverview extends AppCompatActivity implements MeetingSyncEventListener {
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

    ListView meetingOverviewList;

    private MeetingManager.MeetingSyncManager meetingSyncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_overview);
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();

        List<Meeting> meetings = meetingSyncManager.getMeetingMemberships();
        MeetingListAdapter listAdapter = new MeetingListAdapter(MeetingsOverview.this,
                meetings);
        meetingOverviewList = (ListView) findViewById(R.id.fullMeetingListView);
        meetingOverviewList.setAdapter(listAdapter);

        meetingOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MeetingsOverview.this, "You clicked at " +
                        countryList[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingSyncManager.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        meetingSyncManager.removeListener(this);
    }


    public void chat(View view) {
        Toast.makeText(MeetingsOverview.this, "Chat not implemented yet", Toast.LENGTH_SHORT)
                .show();
    }

    public void newMeeting(View view) {
        Intent newMeeting = new Intent(this, NewMeeting.class);
        startActivity(newMeeting);
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

    @Override
    public void onReceivedMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
    }
}
