package com.group02tue.geomeet.backend.social;

import java.util.EventListener;
import java.util.UUID;

public interface MeetingEventListener extends EventListener {
    /**
     * Successfully removed a meeting.
     * @param id Id of meeting removed
     */
    void onRemovedMeeting(UUID id);

    /**
     * Successfully joined a meeting.
     * @param id Id of joined meeting
     */
    void onJoinedMeeting(UUID id);

    /**
     * Successfully created a meeting.
     * @param id Id of created meeting
     */
    void onCreatedMeeting(UUID id);

    /**
     * Failed to process a call.
     * @param id Id to which the call was related
     * @param reason Reason for failing
     */
    void onFailure(UUID id, String reason);

    /**
     * Received an update for a meeting.
     * @param meeting Newly received meeting
     */
    void onMeetingUpdatedReceived(Meeting meeting);
}
