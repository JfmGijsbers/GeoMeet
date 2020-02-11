package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    /**
     *  Create the options menu:
     *  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return true;
    }
    /**
     * Reacting to menu items getting clicked:
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                toProfile();
                return true;
            case R.id.settings:
                toSettings();
                return true;
            case R.id.edit:
                toEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Below this comment are all methods that simply refer the app to a different activity
     */
    private void toProfile() {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);
    }
    private void toSettings() {
        // TODO: add either activity or fragment
        Toast.makeText(this, "Settings not implemented yet", Toast.LENGTH_SHORT).show();
    }
    private void toEdit() {
        // TODO: allow for editing of the profile
        Toast.makeText(this, "Editing of profile not implemented yet", Toast.LENGTH_SHORT).show();
    }
}
