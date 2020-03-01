package com.group02tue.geomeet.backend.social;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.IpSecManager;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group02tue.geomeet.R;
import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class MeetingManager extends ObservableManager<MeetingEventListener> {
    private final SharedPreferences preferences;                    // Preferences reference
    private final AuthenticationManager authenticationManager;      // Authentication manager

    private final static String MEETINGS_PREFERENCE = "meetings";
    private final Map<UUID, Meeting> meetings;

    public MeetingManager(Context context, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Load meetings from disk
        String strMeetings = preferences.getString(MEETINGS_PREFERENCE, "");
        if (!strMeetings.equals("")) {
            Gson gson = new Gson();
            Type meetingListType = new TypeToken<Map<UUID, Meeting>>(){}.getType();
            this.meetings = gson.fromJson(strMeetings, meetingListType);
        } else {
            this.meetings = new HashMap<>();
        }
    }

    private void saveMeetings() {
        synchronized (meetings) {
            Gson gson = new Gson();
            Type meetingListType = new TypeToken<Map<UUID, Meeting>>(){}.getType();
            SharedPreferences.Editor prefsEditor = preferences.edit();
            prefsEditor.putString(MEETINGS_PREFERENCE, gson.toJson(meetings, meetingListType));
            prefsEditor.apply();
        }
    }

    public void addMeeting(Meeting meeting) {
        synchronized (meetings) {
            meetings.put(meeting.getId(), meeting);
            saveMeetings(); // TODO: sync
        }
    }

    public void removeMeeting(UUID id) {
        synchronized (meetings) {

        }
    }

    public Meeting getMeeting(UUID id) throws NoSuchElementException {
        synchronized (meetings) {
            if (meetings.containsKey(id)) {
                return meetings.get(id);
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public void acceptInvitation(UUID id) {

    }

    public void rejectInvitation(UUID id) {

    }

    /**
     * Invite a user to a meeting
     * @param meeting Meeting to invite a user to
     * @param userToInvite The user to invite
     */
    public void inviteUserToMeeting(Meeting meeting, String userToInvite) {
        meeting.inviteUser(userToInvite, new Runnable() {
            @Override
            public void run() {
                saveMeetings();
            }
        }, authenticationManager);
    }

    /**
     * Remove a user from a meeting.
     * @param meeting Meeting to remove a user from
     * @param userToRemove The user to remove from the meeting
     */
    public void removeUserFromMeeting(Meeting meeting, String userToRemove) {
        meeting.removeUser(userToRemove, new Runnable() {
            @Override
            public void run() {
                saveMeetings();
            }
        }, authenticationManager);
    }
}
