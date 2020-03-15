package com.group02tue.geomeet;

import com.group02tue.geomeet.backend.social.ExternalUserProfile;

public class UserListItem {
    private final String username;
    private final ExternalUserProfile profile;

    public UserListItem(String username, ExternalUserProfile profile) {
        this.username = username;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public ExternalUserProfile getProfile() {
        return profile;
    }
}
