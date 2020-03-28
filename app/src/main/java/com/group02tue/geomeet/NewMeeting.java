package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.group02tue.geomeet.backend.Location2D;
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
import java.util.UUID;

import com.group02tue.geomeet.backend.social.ConnectionsEventListener;
import com.group02tue.geomeet.backend.social.ProfileEventListener;


public class NewMeeting extends AppCompatActivity implements MeetingSemiAdminEventListener,
        ConnectionsEventListener, DatePickerDialog.OnDateSetListener {
    private EditText etName, etLocation, etDescription;
    private EditText manualUser;
    private TextView txtDate, txtTime;
    private Button btnCreate;
    private ListView connectionList;
    private MeetingManager meetingManager;
    private MeetingManager.MeetingSyncManager meetingSyncManager;
    private AuthenticationManager authenticationManager;
    private ConnectionsManager connectionsManager;

    private Date date;
    private int mMinute, mHour, mDay, mMonth, mYear;

    private MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

    //private ExternalUserProfile[] testList = {
     //       new ExternalUserProfile("user","a", "b", "email", "test") };

    private UUID createdMeetingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

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
                new ArrayList<String>());
        connectionList.setAdapter(listAdapter);
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

    public void addManualUser(View view) {
        String username = String.valueOf(manualUser.getText());
        ((ConnectionListAdapter)connectionList.getAdapter()).add(username);
        ((ConnectionListAdapter) connectionList.getAdapter()).notifyDataSetChanged();
    }

    public void checkMeeting(View view) {
        boolean allCorrect = true;
        String name = String.valueOf(etName.getText());
        String strLocation = String.valueOf(etLocation.getText());
        String description = String.valueOf(etDescription.getText());

        if (name.equals("")) {
            etName.setError("Please enter a meeting name");
            allCorrect = false;
        }
        // TODO: meeting location validation
        Calendar cal = Calendar.getInstance();
        Date meetingMoment = new Date(mYear, mMonth, mDay, mHour, mMinute);
        Toast.makeText(this, meetingMoment.toString(), Toast.LENGTH_LONG).show();
        /*
        try {
            Location2D location = Location2D.parse(strLocation);
            if (allCorrect) {
                createMeeting(name, description, meetingMoment, location);
            }
        } catch (ParseException e) {
            etLocation.setError("Invalid location");
        }

         */

    }
    private void createMeeting(String name, String description, Date meetingMoment,
                               Location2D location) {
        Meeting meeting = new Meeting(name, description, meetingMoment, location,
                authenticationManager.getUsername());
        createdMeetingId = meeting.getId();
        meetingManager.addMeeting(meeting);
        btnCreate.setEnabled(false);


    }

    public void toAllMeetings(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();   // TODO: code duplication
    }

    @Override
    public void onCreatedMeeting(UUID id) {
        if (id.equals(createdMeetingId)) {
            // Add all invited users to the meeting
            MeetingAsAdminManager adminManager = new MeetingAsAdminManager(meetingManager,
                    authenticationManager, meetingManager.getMeeting(id, meetingSyncManager));
            for (int i = 0; i < connectionList.getAdapter().getCount(); i++) {
                if (((ConnectionListAdapter)connectionList.getAdapter()).isChecked(i)) {
                    String username = (String)connectionList.getAdapter().getItem(i);
                    adminManager.inviteUserToMeeting(username);
                }
            }

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
    public void onReceivedConnections(final ArrayList<ExternalUserProfile> connections) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ExternalUserProfile profile : connections) {
                    ((ConnectionListAdapter)connectionList.getAdapter()).add(
                            profile.getUsername());
                }
                ((ConnectionListAdapter)connectionList.getAdapter()).notifyDataSetChanged();
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
                        txtDate.setText("At " + String.valueOf(txtDate.getText()) + "\n" + mHour + ":" + mMinute);
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
