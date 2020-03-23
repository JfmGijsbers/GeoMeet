package com.group02tue.geomeet.backend.social;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.UUID;

/**
 * All events related to meeting synchronization between the server and app.
 */
public interface MeetingSyncEventListener extends EventListener {
    /**
     * Received an update for a meeting and/or joined a new meeting.
     * @param meeting Newly received meeting
     */
    void onMeetingUpdatedReceived(Meeting meeting);

    /**
     * User has been removed from a meeting.
     * @param id Id of meeting of which the user is no longer part
     */
    void onLeftMeeting(UUID id);

    /**
     * Failed to process a call.
     * @param id Id to which the call was related
     * @param reason Reason for failing
     */
    void onFailure(UUID id, String reason);

    /**
     * Received a list of meetings to which the user has been invited.
     * @param meetings Meetings invited to
     */
    void onReceivedMeetingInvitations(ArrayList<ImmutableMeeting> meetings);
}
