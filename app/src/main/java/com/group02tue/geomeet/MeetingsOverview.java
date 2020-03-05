package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MeetingsOverview extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_overview);

        MeetinglistAdapter listAdapter = new MeetinglistAdapter(MeetingsOverview.this,
                countryList, imageId);
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
    public void chat(View view) {
        Toast.makeText(MeetingsOverview.this, "Chat not implemented yet", Toast.LENGTH_SHORT)
                .show();
    }

    public void newMeeting(View view) {
        Intent newMeeting = new Intent(this, com.group02tue.geomeet.newMeeting.class);
        startActivity(newMeeting);
    }
}
