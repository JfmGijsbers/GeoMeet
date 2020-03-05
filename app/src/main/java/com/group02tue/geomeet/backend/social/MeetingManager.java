package com.group02tue.geomeet.backend.social;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.IpSecManager;

import androidx.core.util.Consumer;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group02tue.geomeet.R;
import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.meetings.CreateMeetingAPICall;
import com.group02tue.geomeet.backend.api.meetings.DecideMeetingInvitationAPICall;
import com.group02tue.geomeet.backend.api.meetings.DeleteMeetingAPICall;
import com.group02tue.geomeet.backend.api.meetings.QueryMeetingAPICall;
import com.group02tue.geomeet.backend.api.meetings.QueryMeetingAPIResponseListener;
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

    /**
     * Saves the meetings on the disk.
     */
    public void saveMeetings() {
        synchronized (meetings) {
            Gson gson = new Gson();
            Type meetingListType = new TypeToken<Map<UUID, Meeting>>(){}.getType();
            SharedPreferences.Editor prefsEditor = preferences.edit();
            prefsEditor.putString(MEETINGS_PREFERENCE, gson.toJson(meetings, meetingListType));
            prefsEditor.apply();
        }
    }

    /**
     * Adds a new meeting both locally and online.
     * @param meeting Meeting to add
     */
    public void addMeeting(final Meeting meeting) {
        new CreateMeetingAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                synchronized (meetings) {
                    meetings.put(meeting.getId(), meeting);
                    saveMeetings();
                }
                notifyListeners(new Consumer<MeetingEventListener>() {
                    @Override
                    public void accept(MeetingEventListener meetingEventListener) {
                        meetingEventListener.onCreatedMeeting(meeting.getId());
                    }
                });
            }

            @Override
            public void onFailure(final String reason) {
                notifyListenersAboutFailure(meeting.getId(), reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                onFailure("Server error: " + response.toString());
            }
        }, meeting.getId(), meeting.getName(), meeting.getLocation(), meeting.getMoment(), meeting.getDescription()).execute();
    }

    /**
     * Removes a meeting both locally and online.
     * @param id Id of meeting to remove
     */
    public void removeMeeting(final UUID id) {
        new DeleteMeetingAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                synchronized (meetings) {
                    if (meetings.containsKey(id)) {
                        meetings.remove(id);
                        saveMeetings();
                    }
                }
                notifyListeners(new Consumer<MeetingEventListener>() {
                    @Override
                    public void accept(MeetingEventListener meetingEventListener) {
                        meetingEventListener.onRemovedMeeting(id);
                    }
                });
            }

            @Override
            public void onFailure(String reason) {
                notifyListenersAboutFailure(id, reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                onFailure("Server error: " + response.toString());
            }
        }, id).execute();
    }

    /**
     * Gets a meeting from the local cache and request a update for this specific meeting.
     * @param id Id of meeting to look for
     * @return Meeting from the cache
     * @throws NoSuchElementException Meeting not found in cache, meeting may still be delivered later
     */
    public Meeting getMeeting(final UUID id) throws NoSuchElementException {
        new QueryMeetingAPICall(authenticationManager, new QueryMeetingAPIResponseListener() {
            @Override
            public void onSuccess(final Meeting meeting) {
                synchronized (meetings) {
                    meetings.put(id, meeting);
                    saveMeetings();
                }
                notifyListeners(new Consumer<MeetingEventListener>() {
                    @Override
                    public void accept(MeetingEventListener meetingEventListener) {
                        meetingEventListener.onMeetingUpdatedReceived(meeting);
                    }
                });
            }

            @Override
            public void onFailure(APIFailureReason response) {
                // Ignore
            }
        }, id).execute();
        return getLocalMeeting(id);
    }

    private Meeting getLocalMeeting(UUID id) throws NoSuchElementException {
        synchronized (meetings) {
            if (meetings.containsKey(id)) {
                return meetings.get(id);
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Accepts or denies an invitation for a meeting.
     * @param id Id of meeting for which the invite was
     * @param join Join the meeting?
     */
    public void decideInvitation(final UUID id, final boolean join) {
        new DecideMeetingInvitationAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                saveMeetings();
                if (join) {
                    notifyListeners(new Consumer<MeetingEventListener>() {
                        @Override
                        public void accept(MeetingEventListener meetingEventListener) {
                            meetingEventListener.onJoinedMeeting(id);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String reason) {
                notifyListenersAboutFailure(id, reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                onFailure("Server error: " + response.toString());
            }
        }, id, join).execute();
    }

    /**
     * Notifies listeners about failure in editing a meeting.
     * @param id ID of meeting which failed
     * @param reason Reason of failing
     */
    private void notifyListenersAboutFailure(final UUID id, final String reason) {
        notifyListeners(new Consumer<MeetingEventListener>() {
            @Override
            public void accept(MeetingEventListener meetingEventListener) {
                meetingEventListener.onFailure(id, reason);
            }
        });
    }
}
