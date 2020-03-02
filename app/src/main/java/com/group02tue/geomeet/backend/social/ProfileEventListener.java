package com.group02tue.geomeet.backend.social;

import java.util.EventListener;

public interface ProfileEventListener extends EventListener {
    /**
     * Successfully updated the profile.
     */
    void onProfileUpdated();

    /**
     * Failed to update the profile.
     * @param reason Reason of failing
     */
    void onFailedToUpdateProfile(String reason);

    /**
     * Found a profile corresponding to a search.
     * @param requestedUser User asked
     * @param profile Profile found
     */
    void onProfileFound(String requestedUser, ExternalUserProfile profile);

    /**
     * Failed to find a profile.
     * @param requestedUser User asked
     */
    void onProfileNotFound(String requestedUser);
}
