package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_invites);
        meetingInvitesList = findViewById(R.id.invites_meeting_list);
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();

        meetingInvites = meetingSyncManager.requestMeetingInvitations();
        final MeetingInvitesListAdapter listAdapter = new MeetingInvitesListAdapter(
                MeetingInvites.this, meetingInvites);
        meetingInvitesList.setAdapter(listAdapter);
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

    @Override
    public void onReceivedNewMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
        synchronized (meetingInvites) {
            for (ImmutableMeeting meetingInvite : meetings) {
                meetingInvites.add(meetingInvite);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MeetingInvitesListAdapter)meetingInvitesList.getAdapter()).notifyDataSetChanged();
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
