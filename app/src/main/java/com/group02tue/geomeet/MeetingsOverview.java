package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    ListView meetingOverviewList;

    private MeetingManager.MeetingSyncManager meetingSyncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_overview);
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();

        List<Meeting> meetings = meetingSyncManager.getMeetingMemberships();
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
