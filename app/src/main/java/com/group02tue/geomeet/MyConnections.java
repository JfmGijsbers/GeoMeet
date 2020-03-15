package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MyConnections extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_connections);

        /*ConnectionListAdapter listAdapter = new ConnectionListAdapter(MyConnections.this,
                countryList, imageId);
        connectionList = (ListView) findViewById(R.id.connectionListView);
        connectionList.setAdapter(listAdapter);

        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MyConnections.this, "You clicked at " +
                        countryList[position], Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
