package com.group02tue.geomeet.backend.social;

import androidx.core.util.Consumer;

import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.meetings.UpdateMeetingAPICall;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import java.util.Date;
import java.util.UUID;

public class MeetingAsAdminManager extends ObservableManager<MeetingAsAdminEventListener> {
    private final MeetingManager meetingManager;
    private final AuthenticationManager authenticationManager;
    private final Meeting meeting;

    public MeetingAsAdminManager(MeetingManager meetingManager, AuthenticationManager authenticationManager, Meeting meeting) {
        this.meetingManager = meetingManager;
        this.meeting = meeting;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Invites a user to a meeting
     * @param userToInvite The user to invite
     */
    public void inviteUserToMeeting(final String userToInvite) {
        meeting.inviteUser(userToInvite, authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                meetingManager.saveMeetings();
                notifyListeners(new Consumer<MeetingAsAdminEventListener>() {
                    @Override
                    public void accept(MeetingAsAdminEventListener meetingEventListener) {
                        meetingEventListener.onInvitedUserToMeeting(meeting.getId(), userToInvite);
                    }
                });
            }

            @Override
            public void onFailure(String reason) {
                notifyListenersFailedToEditEvent(meeting.getId(), reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                notifyListenersFailedToEditEvent(meeting.getId(), "Server error: " + response.toString());
            }
        });
    }

    /**
     * Removes a user from a meeting.
     * @param userToRemove The user to remove from the meeting
     */
    public void removeUserFromMeeting(final String userToRemove) {
        meeting.removeUser(userToRemove, authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                meetingManager.saveMeetings();
                notifyListeners(new Consumer<MeetingAsAdminEventListener>() {
                    @Override
                    public void accept(MeetingAsAdminEventListener meetingEventListener) {
                        meetingEventListener.onRemovedUserFromMeeting(meeting.getId(), userToRemove);
                    }
                });
            }

            @Override
            public void onFailure(String reason) {
                notifyListenersFailedToEditEvent(meeting.getId(), reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                notifyListenersFailedToEditEvent(meeting.getId(), "Server error: " + response.toString());
            }
        });
    }

    /**
     * Updates a meeting.
     * @param name Name to set
     * @param description Description to set
     * @param moment Moment to set
     * @param location Location to set
     */
    public void updateMeeting(String name, String description, Date moment, Location2D location) {
        new UpdateMeetingAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                meetingManager.saveMeetings();
                notifyListeners(new Consumer<MeetingAsAdminEventListener>() {
                    @Override
                    public void accept(MeetingAsAdminEventListener meetingAsAdminEventListener) {
                        meetingAsAdminEventListener.onUpdatedMeeting(meeting.getId());
                    }
                });
            }

            @Override
            public void onFailure(String reason) {
                notifyListenersFailedToEditEvent(meeting.getId(), reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                onFailure("Server error: " + response);
            }
        }, meeting.getId(), name, location, moment, description).execute();
    }

    /**
     * Notifies listeners about failure in editing a meeting.
     * @param id ID of meeting which failed
     * @param reason Reason of failing
     */
    private void notifyListenersFailedToEditEvent(final UUID id, final String reason) {
        notifyListeners(new Consumer<MeetingAsAdminEventListener>() {
            @Override
            public void accept(MeetingAsAdminEventListener meetingEventListener) {
                meetingEventListener.onFailedToEditMeeting(id, reason);
            }
        });
    }
}
