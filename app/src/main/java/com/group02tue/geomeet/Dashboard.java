package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class Dashboard extends AppCompatActivity {

    ListView simpleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //simpleList = (ListView) findViewById(R.id.meetingListView);
        //MeetinglistAdapter adapter = new MeetinglistAdapter(getApplicationContext());
        //simpleList.setAdapter(adapter);

    }
}
