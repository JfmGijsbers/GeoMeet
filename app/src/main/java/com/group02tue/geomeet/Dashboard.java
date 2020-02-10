package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

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
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MeetinglistAdapter listAdapter = new MeetinglistAdapter(Dashboard.this,
                countryList, imageId);
        list = (ListView) findViewById(R.id.meetingListView);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Dashboard.this, "You clicked at " +
                        countryList[position], Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void chat(View view) {
        Toast.makeText(Dashboard.this, "Chat not implemented yet", Toast.LENGTH_SHORT)
                .show();
    }
}
