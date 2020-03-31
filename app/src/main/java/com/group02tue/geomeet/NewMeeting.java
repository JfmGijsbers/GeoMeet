package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.profiles.QueryUsersAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ConnectionsManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.group02tue.geomeet.backend.social.InternalUserProfile;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingAsAdminManager;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSemiAdminEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.group02tue.geomeet.backend.social.ConnectionsEventListener;
import com.group02tue.geomeet.backend.social.ProfileEventListener;


public class NewMeeting extends AppCompatActivity implements MeetingSemiAdminEventListener,
        ConnectionsEventListener, DatePickerDialog.OnDateSetListener {
    private EditText etName, etLocation, etDescription;
    private EditText manualUser;
    private TextView txtDate;
    private Button btnCreate;
    private ListView connectionList;
    private ArrayList<String> connections = new ArrayList<>();
    private MeetingManager meetingManager;
    private MeetingManager.MeetingSyncManager meetingSyncManager;
    private AuthenticationManager authenticationManager;
    private ConnectionsManager connectionsManager;

    private Date date;
    private int mMinute, mHour, mDay, mMonth, mYear;


    private UUID createdMeetingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        meetingManager = ((MainApplication)getApplication()).getMeetingManager();
        meetingSyncManager = ((MainApplication)getApplication()).getMeetingSyncManager();
        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();
        connectionsManager = ((MainApplication)getApplication()).getConnectionsManager();

        etName = findViewById(R.id.et_meeting_name);
        etLocation = findViewById(R.id.et_meeting_location);
        etDescription = findViewById(R.id.et_meeting_description);
        connectionList = findViewById(R.id.listview_connections);
        btnCreate = findViewById(R.id.btn_create_meeting);

        txtDate = findViewById(R.id.txt_date);

        manualUser = findViewById(R.id.et_meeting_manual_user);

        // Initialize list adapter
        ConnectionListAdapter listAdapter = new ConnectionListAdapter(NewMeeting.this,
                connections);
        connectionList.setAdapter(listAdapter);
    }

    /**
     *  Create the options menu:
     *  */
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
        Intent backIntent = new Intent(this, MeetingsOverview.class);
        startActivity(backIntent);
    }
    private void toProfile() {
        Intent profileIntent = new Intent(this, Profile.class);
        startActivity(profileIntent);
    }
    private void logout() {
        ((MainApplication)getApplication()).reset();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
    private void toSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingManager.addListener(this);
        connectionsManager.addListener(this);
        connectionsManager.requestConnections();
    }

    @Override
    protected void onStop() {
        super.onStop();
        meetingManager.removeListener(this);
        connectionsManager.removeListener(this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<String> restoredConnections = savedInstanceState.getStringArrayList("connections");
        boolean[] connectionsChecked = savedInstanceState.getBooleanArray("connectionsChecked");

        synchronized (connections) {
            for (int i = 0; i < restoredConnections.size(); i++) {
                connections.add(restoredConnections.get(i));
                boolean set = false;
                if (i < connectionsChecked.length) {
                    set = connectionsChecked[i];
                }
                ((ConnectionListAdapter) connectionList.getAdapter()).setChecked(i, set);
            }
            ((ConnectionListAdapter) connectionList.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("connections", connections);
        outState.putBooleanArray("connectionsChecked",
                ((ConnectionListAdapter) connectionList.getAdapter()).getCheckedArray());
    }

    public void addManualUser(View view) {
        new ConnectionPickDialog(this, new ConnectionPickDialogEventListener() {
            @Override
            public void onPickedConnection(String username) {
                synchronized (connections) {
                    if (!connections.contains(username)) {
                        connections.add(username);
                    }
                    ((ConnectionListAdapter)connectionList.getAdapter()).notifyDataSetChanged();
                }
            }
        }, authenticationManager).show();
    }

    public void checkMeeting(View view) {
        boolean allCorrect = true;
        String name = String.valueOf(etName.getText());
        String strLocation = String.valueOf(etLocation.getText());
        String description = String.valueOf(etDescription.getText());
        String date = String.valueOf(txtDate.getText());

        if (name.equals("")) {
            etName.setError("Please enter a meeting name");
            allCorrect = false;
        }
        if (date.equals("")) {
            txtDate.setError("Please set a date");
            allCorrect = false;
        }

        Calendar cal = Calendar.getInstance();
        Date meetingMoment = new Date(mYear - 1900, mMonth, mDay, mHour, mMinute);

        try {
            Location2D location = Location2D.parse(strLocation);
            if (allCorrect) {
                createMeeting(name, description, meetingMoment, location);
            }
        } catch (ParseException e) {
            etLocation.setError("Invalid location");
        }
    }
    private void createMeeting(String name, String description, Date meetingMoment,
                               Location2D location) {
        Meeting meeting = new Meeting(name, description, meetingMoment, location,
                authenticationManager.getUsername());
        createdMeetingId = meeting.getId();
        List<String> peopleToAdd = new ArrayList<>();
        synchronized (connections) {
            Log.println(Log.DEBUG, "Debug6", "Manually adding user99: " + connections.size());
            for (int i = 0; i < connections.size(); i++) {
                if (((ConnectionListAdapter)connectionList.getAdapter()).isChecked(i)) {
                    peopleToAdd.add(connections.get(i));
                }
            }
        }
        meetingManager.addMeeting(meeting, peopleToAdd);
        btnCreate.setEnabled(false);
    }

    public void toAllMeetings(View view) {
        Intent intent = new Intent(this, MeetingsOverview.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreatedMeeting(UUID id) {
        if (id.equals(createdMeetingId)) {
            Intent intent = new Intent(this, MeetingsOverview.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRemovedMeeting(UUID id) {
    }

    @Override
    public void onFailure(UUID id, String reason) {
        if (id.equals(createdMeetingId)) {
            createdMeetingId = null;
            Toast.makeText(this, "Failed to create meeting: " + reason,
                    Toast.LENGTH_LONG).show();
            btnCreate.setEnabled(true);
        }
    }

    @Override
    public void onReceivedConnections(final ArrayList<ExternalUserProfile> receivedConnections) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (connections) {
                    for (ExternalUserProfile profile : receivedConnections) {
                        connections.add(profile.getUsername());
                    }
                    ((ConnectionListAdapter)connectionList.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    public void toPicker(View view){
        Intent pickIntent = new Intent(this, LocationPicker.class);
        startActivityForResult(pickIntent,0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                etLocation.setText(data.getData().toString());
            }
        }
    }

    public void showDatePicker(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    public void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        txtDate.setText(String.valueOf(txtDate.getText()) + "\nat: " + mHour + ":" + mMinute);
                        date = c.getTime();
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        txtDate.setText(currentDateString);
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;

        showTimePicker();
    }
}
