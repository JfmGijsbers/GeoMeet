package com.group02tue.geomeet.backend.social;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

/**
 * User profile of the user logged in.
 */
public class InternalUserProfile extends UserProfile {
    private final SharedPreferences preferences;                        // Preferences reference
    private final AuthenticationManager authenticationManager;          // Authentication manager

    private final static String FIRST_NAME_PREFERENCE = "firstName";    // First name key for prefs.
    private final static String LAST_NAME_PREFERENCE = "lastName";      // Last name key for prefs.
    private final static String EMAIL_PREFERENCE = "email";             // Email key for prefs.
    private final static String DESCRIPTION_PREFERENCE = "description"; // Description key for prefs.
    private final static String MEETINGS_PREFERENCE = "meetings";       // Meetings list key for prefs.

    public InternalUserProfile(Context context, AuthenticationManager authenticationManager) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.authenticationManager = authenticationManager;

        // Load data from disk
        firstName = preferences.getString(FIRST_NAME_PREFERENCE, "");
        lastName = preferences.getString(LAST_NAME_PREFERENCE, "");
        email = preferences.getString(EMAIL_PREFERENCE, "");
        description = preferences.getString(DESCRIPTION_PREFERENCE, "");
    }

    public boolean update(String firstName, String lastName, String email, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIRST_NAME_PREFERENCE, firstName);
        editor.putString(LAST_NAME_PREFERENCE, lastName);
        editor.putString(EMAIL_PREFERENCE, email);
        editor.putString(DESCRIPTION_PREFERENCE, description);
        editor.apply();
        // TODO: push to server + let server check new data (and only save after this check)
        return true;    // TODO: return false if we don't like the changes, not yet implemented
    }

    /**
     * Resets the user profile.
     */
    public void reset() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIRST_NAME_PREFERENCE, "");
        editor.putString(LAST_NAME_PREFERENCE, "");
        editor.putString(EMAIL_PREFERENCE, "");
        editor.putString(DESCRIPTION_PREFERENCE, "");
        editor.apply();
    }
}
