package com.group02tue.geomeet.backend.social;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Set;
import java.util.UUID;

/**
 * All events related to actions where a meeting is added or removed.
 */
public interface MeetingSemiAdminEventListener extends EventListener {
    /**
     * Successfully created a meeting.
     * @param id Id of created meeting
     */
    void onCreatedMeeting(UUID id);

    /**
     * Successfully removed a meeting.
     * @param id Id of meeting removed
     */
    void onRemovedMeeting(UUID id);

    /**
     * Failed to process a call.
     * @param id Id to which the call was related
     * @param reason Reason for failing
     */
    void onFailure(UUID id, String reason);
}
