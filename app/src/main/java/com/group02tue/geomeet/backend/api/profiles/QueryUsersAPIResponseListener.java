package com.group02tue.geomeet.backend.api.profiles;

import com.group02tue.geomeet.backend.api.APIResponseListener;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;

import java.util.HashSet;

public interface QueryUsersAPIResponseListener extends APIResponseListener {
    /**
     * Found a list of usernames corresponding to a certain requested username.
     * @param requestedUsername Username used in the query
     * @param usernames Usernames found
     */
    void onFoundUsernames(String requestedUsername, HashSet<String> usernames);
}
