package com.group02tue.geomeet.backend.social;

import java.util.EventListener;
import java.util.UUID;

public interface MeetingAsAdminEventListener extends EventListener {
    /**
     * Successfully removed a user from a meeting.
     * @param id Id of meeting
     * @param userRemoved User who got removed
     */
    void onRemovedUserFromMeeting(UUID id, String userRemoved);

    /**
     * Successfully invited a user to a meeting.
     * @param id Id of meeting
     * @param userAdded User who has been added
     */
    void onInvitedUserToMeeting(UUID id, String userAdded);

    /**
     * Failed to process a call.
     * @param id Id of meeting related to the call
     * @param reason Reason for failing
     */
    void onFailedToEditMeeting(UUID id, String reason);

    /**
     * Successfully updated a meeting
     * @param id Id of meeting which got updated
     */
    void onUpdatedMeeting(UUID id);
}
