package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.social.ConnectionsEventListener;
import com.group02tue.geomeet.backend.social.ConnectionsManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class MyConnections extends AppCompatActivity implements ConnectionsEventListener {
    private ListView connectionList;
    private final List<ExternalUserProfile> connections = new ArrayList<>();
    private ConnectionsManager connectionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connections);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connectionsManager = ((MainApplication) getApplication()).getConnectionsManager();

        connectionList = findViewById(R.id.listview_connections);
        ProfilesListAdapter listAdapter = new ProfilesListAdapter(this, connections);
        connectionList.setAdapter(listAdapter);
        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                synchronized (connections) {
                    if (position < connections.size()) {
                        ExternalUserProfile profileToShow = connections.get(position);
                        Intent profileIntent = new Intent(view.getContext(), Profile.class);
                        profileIntent.putExtra("profileName", profileToShow.getFullName());
                        profileIntent.putExtra("username", profileToShow.getUsername());
                        profileIntent.putExtra("description", profileToShow.getDescription());
                        startActivity(profileIntent);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectionsManager.addListener(this);
        connectionsManager.requestConnections();
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectionsManager.removeListener(this);
    }

    /**
     * Create the options menu:
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Below this comment are all methods that simply refer the app to a different activity
     */
    private void back() {
        Intent backIntent = new Intent(this, Dashboard.class);
        startActivity(backIntent);
    }

    private void toProfile() {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);
    }

    private void logout() {
        ((MainApplication) getApplication()).reset();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private void toSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public void onReceivedConnections(ArrayList<ExternalUserProfile> receivedConnections) {
        synchronized (connections) {
            connections.clear();
            for (ExternalUserProfile receivedConnection : receivedConnections) {
                connections.add(receivedConnection);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProfilesListAdapter) connectionList.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}
