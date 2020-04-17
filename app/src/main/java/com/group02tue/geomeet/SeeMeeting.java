package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingAsAdminManager;
import com.group02tue.geomeet.backend.social.MeetingManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

public class SeeMeeting extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView txtTitle, txtLocation, txtAdmin, txtDate, txtDescription;
    ListView connectionList;
    private UUID meetingId;
    private MeetingManager meetingManager;
    private MeetingManager.MeetingSyncManager meetingSyncManager;
    private AuthenticationManager authenticationManager;
    private final ArrayList<String> membersList = new ArrayList<>();
    private String adminUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_meeting);
        meetingManager = ((MainApplication) getApplication()).getMeetingManager();
        meetingSyncManager = ((MainApplication) getApplication()).getMeetingSyncManager();
        authenticationManager = ((MainApplication) getApplication()).getAuthenticationManager();

        Button btnAddUsers = findViewById(R.id.seeMeeting_inviteButton);
        Button btnRemoveUsers = findViewById(R.id.seeMeeting_removeButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
         * First, we need to retrieve all the data from the intent
         */
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        Date date = (Date) intent.getSerializableExtra("date");
        String strLocation = intent.getStringExtra("location");
        adminUsername = intent.getStringExtra("hostedBy");
        meetingId = UUID.fromString(intent.getStringExtra("meetingId"));
        try {
            Location2D location = Location2D.parse(strLocation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String member = "";
        int i = 0;
        while (member != null) {
            member = intent.getStringExtra("member" + i);
            i++;
            if (member != null && !member.isEmpty()) {
                membersList.add(member);
            }
        }
        connectionList = findViewById(R.id.seeMeeting_comingConnectionsListView);
        connectionList.setAdapter(new MembersListAdapter(this, membersList));

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
        txtAdmin.setText("Meeting organizer: " + adminUsername);
        txtDate.setText(date.toString());
        txtDescription.setText(description);

        /*
        Then, optionally we disable the add and remove buttons
         */
        //Log.d("Debug", "username: " + authenticationManager.getUsername() + " vs " + hostedBy);
        if (!authenticationManager.getUsername().equals(adminUsername)) {
            btnAddUsers.setVisibility(View.INVISIBLE);
            btnRemoveUsers.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Create the options menu:
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.meeting_menu, menu);
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
            case R.id.delete:
                deleteMeeting();
                return true;
            case R.id.edit:
                Toast.makeText(this, "If you are an admin, you can edit the name " +
                        "and date by clicking on the corresponding element.", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editMeetingName(View view) {
        if (adminUsername.equals(authenticationManager.getUsername())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New name");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(txtTitle.getText());
            builder.setView(input);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newName = input.getText().toString();
                    try {
                        if (!newName.isEmpty()) {
                            Meeting meeting = meetingManager.getLocalMeeting(meetingId);
                            MeetingAsAdminManager adminManager = new MeetingAsAdminManager(meetingManager,
                                    authenticationManager, meeting);
                            adminManager.updateMeeting(newName, meeting.getDescription(), meeting.getMoment(),
                                    meeting.getLocation());
                            txtTitle.setText(newName);
                        }
                    } catch (NoSuchElementException e) {
                        // No meeting -> do not update
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    public void editMeetingDate(View view) {
        if (adminUsername.equals(authenticationManager.getUsername())) {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        }
    }

    private int mMinute, mHour, mDay, mMonth, mYear;

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
                        Date date = new Date(mYear - 1900, mMonth, mDay, mHour, mMinute);
                        txtDate.setText(date.toString());
                        try {
                            Meeting meeting = meetingManager.getLocalMeeting(meetingId);
                            MeetingAsAdminManager adminManager = new MeetingAsAdminManager(meetingManager,
                                    authenticationManager, meeting);
                            adminManager.updateMeeting(meeting.getName(), meeting.getDescription(), date,
                                    meeting.getLocation());
                        } catch (NoSuchElementException e) {
                            // No meeting -> do not update
                        }
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

    private void deleteMeeting() {
        try {
            Meeting meeting = meetingManager.getLocalMeeting(meetingId);
            if (meeting.getAdminUsername().equals(authenticationManager.getUsername())) {
                meetingManager.removeMeeting(meetingId);
                back();
            } else {
                meetingSyncManager.leaveMeeting(meetingId);
                back();
            }
        } catch (NoSuchElementException e) {
            back();
        }
    }

    public void addUsers(final View view) {
        new ConnectionPickDialog(this, new ConnectionPickDialogEventListener() {
            @Override
            public void onPickedConnection(String username) {
                try {
                    synchronized (membersList) {
                        if (membersList.contains(username)) {
                            Toast.makeText(view.getContext(), "User picked is already a member",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    MeetingAsAdminManager adminManager = new MeetingAsAdminManager(meetingManager,
                            authenticationManager, meetingManager.getLocalMeeting(meetingId));
                    adminManager.inviteUserToMeeting(username);
                    Toast.makeText(view.getContext(), "User successfully invited",
                            Toast.LENGTH_LONG).show();
                } catch (NoSuchElementException e) {
                    // No meeting -> do not add
                }
            }
        }, authenticationManager).show();
    }

    public void removeUsers(final View view) {
        new ConnectionPickDialog(this, new ConnectionPickDialogEventListener() {
            @Override
            public void onPickedConnection(String username) {
                try {
                    synchronized (membersList) {
                        if (!membersList.contains(username) || adminUsername.equals(username)) {
                            Toast.makeText(view.getContext(), "User picked cannot be removed",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        MeetingAsAdminManager adminManager = new MeetingAsAdminManager(meetingManager,
                                authenticationManager, meetingManager.getLocalMeeting(meetingId));
                        adminManager.removeUserFromMeeting(username);

                        if (membersList.contains(username)) {
                            membersList.remove(username);
                            ((MembersListAdapter) connectionList.getAdapter()).notifyDataSetChanged();
                        }
                    }
                } catch (NoSuchElementException e) {
                    // No meeting -> do not add
                }
            }
        }, authenticationManager).show();
    }

    public void toMap(View view) {
        Intent mapIntent = new Intent(this, LocationViewer.class);
        mapIntent.putExtra("fromSeeMeeting", -1);
        mapIntent.putExtra("location", getIntent().getStringExtra("location"));
        startActivity(mapIntent);
    }

    public void toChat(View view) {
        Intent chatIntent = new Intent(this, MessageListActivity.class);
        chatIntent.putExtra("meetingId", meetingId.toString());
        startActivity(chatIntent);
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

    private void back() {
        Intent backIntent = new Intent(this, MeetingsOverview.class);
        startActivity(backIntent);
        finish();
    }
}
