package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SeeMeeting extends AppCompatActivity {
    String countryList[] = {"Roel Koopman", "Lucas Vereggen", "Julian Vink",
            "Kevin Dirksen", "Rik Litjens", "Jeroen Gijsbers"};
    Integer[] imageId = {
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground
    };

    ListView connectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_meeting);

        ConnectionlistAdapter listAdapter = new ConnectionlistAdapter(SeeMeeting.this,
                countryList, imageId);
        connectionList = (ListView) findViewById(R.id.seeMeeting_comingConnectionsListView);
        connectionList.setAdapter(listAdapter);

        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(SeeMeeting.this, "You clicked at " +
                        countryList[position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
