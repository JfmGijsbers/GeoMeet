package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.group02tue.geomeet.backend.social.InternalUserProfile;
import com.group02tue.geomeet.backend.social.ProfileEventListener;

public class Profile extends AppCompatActivity implements ProfileEventListener {
    private TextView txtProfileName;
    private TextView txtUsername;
    private TextView txtDescription;

    private AuthenticationManager authenticationManager;
    private InternalUserProfile profile;
    private InternalUserProfile.ProfileManager profileManager;

    private String profileUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtProfileName = findViewById(R.id.profileName);
        txtUsername = findViewById(R.id.username);
        txtDescription = findViewById(R.id.txtDescription);

        authenticationManager = ((MainApplication) getApplication()).getAuthenticationManager();
        profile = ((MainApplication) getApplication()).getInternalUserProfile();
        profileManager = ((MainApplication) getApplication()).getProfileManager();

        Intent intent = getIntent();
        if (intent.getStringExtra("profileName") == null) {
            txtProfileName.setText(profile.getFirstName() + " " + profile.getLastName());
            txtUsername.setText(authenticationManager.getUsername());
            profileUsername = authenticationManager.getUsername();
            txtDescription.setText(profile.getDescription());
        } else {
            txtProfileName.setText(intent.getStringExtra("profileName"));
            profileUsername = intent.getStringExtra("username");
            txtUsername.setText(intent.getStringExtra("username"));
            txtDescription.setText(intent.getStringExtra("description"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileManager.addListener(this);
        profileManager.getProfile(profileUsername);
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileManager.removeListener(this);
    }

    /**
     * Create the options menu:
     */
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
            case android.R.id.home:
                back();
                return true;
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
    private void back() {
        Intent backIntent = new Intent(this, Dashboard.class);
        startActivity(backIntent);
    }

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

    @Override
    public void onProfileUpdated() {
    }

    @Override
    public void onFailedToUpdateProfile(String reason) {
    }

    @Override
    public void onProfileFound(String requestedUser, final ExternalUserProfile profile) {
        if (requestedUser.equals(profileUsername)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtProfileName.setText(profile.getFullName());
                    txtDescription.setText(profile.getDescription());
                }
            });
        }
    }

    @Override
    public void onProfileNotFound(String requestedUser) {
    }
}
