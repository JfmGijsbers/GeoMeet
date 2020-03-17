package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.Location2D;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class SeeMeeting extends AppCompatActivity {
    TextView txtTitle, txtLocation, txtAdmin, txtDate, txtDescription;
    ListView connectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_meeting);

        /*
         * First, we need to retrieve all the data from the intent
         */
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        Date date = (Date) intent.getSerializableExtra("date");
        String strLocation = intent.getStringExtra("location");
        String hostedBy = intent.getStringExtra("hostedBy");
        try {
            Location2D location = Location2D.parse(strLocation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String member = "";
        int i = 0;
        ArrayList<String> members = new ArrayList<>();
        while (member != null) {
            member = intent.getStringExtra("member" + i);
            i++;
            members.add(member);
        }
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
    public void toMap(View view){
        Intent mapIntent = new Intent(this, LocationViewer.class);
        startActivity(mapIntent);
    }
}
