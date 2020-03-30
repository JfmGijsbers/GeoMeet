package com.group02tue.geomeet.backend.social;

import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.api.profiles.GetProfileAPIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User profile of a user different from the user logged in.
 */
public class ExternalUserProfile extends UserProfile {
    private final String username;
    public String getUsername() { return username; }

    public ExternalUserProfile(String username, String firstName, String lastName, String email, String description) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Convert a jsonObject into an external user profile.
     * @param username Username of user of who the profile is
     * @param jsonObject Object to convert
     * @return An external user profile or null if no valid profile exists in the json
     * @throws JSONException Invalid data in the json
     */
    public static ExternalUserProfile read(String username, JSONObject jsonObject) throws JSONException {
        if (jsonObject.has(JSONKeys.FIRST_NAME) && jsonObject.has(JSONKeys.LAST_NAME) &&
                jsonObject.has(JSONKeys.DESCRIPTION)) {
            String email = "";
            if (jsonObject.has(JSONKeys.EMAIL)) {
                email = jsonObject.getString(JSONKeys.EMAIL);
            }

            return new ExternalUserProfile(
                    username,
                    jsonObject.getString(JSONKeys.FIRST_NAME),
                    jsonObject.getString(JSONKeys.LAST_NAME),
                    email,
                    jsonObject.getString(JSONKeys.DESCRIPTION)
            );
        } else {
            return null;
        }
    }
}
