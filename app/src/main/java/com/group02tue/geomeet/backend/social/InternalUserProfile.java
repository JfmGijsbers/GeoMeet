package com.group02tue.geomeet.backend.social;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.util.Consumer;
import androidx.preference.PreferenceManager;

import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.profiles.GetProfileAPICall;
import com.group02tue.geomeet.backend.api.profiles.GetProfileAPIResponseListener;
import com.group02tue.geomeet.backend.api.profiles.UpdateProfileAPICall;
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

    public InternalUserProfile(Context context, AuthenticationManager authenticationManager) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.authenticationManager = authenticationManager;

        // Load data from disk
        firstName = preferences.getString(FIRST_NAME_PREFERENCE, "");
        lastName = preferences.getString(LAST_NAME_PREFERENCE, "");
        email = preferences.getString(EMAIL_PREFERENCE, "");
        description = preferences.getString(DESCRIPTION_PREFERENCE, "");
    }

    public class ProfileManager extends ObservableManager<ProfileEventListener> {
        /**
         * Updates the user profile, locally and online.
         * @param firstName New first name
         * @param lastName New last name
         * @param email New email
         * @param description New description
         */
        public void update(final String firstName, final String lastName,
                              final String email, final String description) {
            new UpdateProfileAPICall(authenticationManager, new BooleanAPIResponseListener() {
                @Override
                public void onSuccess() {
                    // Update internally after server has confirmed the update
                    InternalUserProfile.this.firstName = firstName;
                    InternalUserProfile.this.lastName = lastName;
                    InternalUserProfile.this.email = email;
                    InternalUserProfile.this.description = description;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(FIRST_NAME_PREFERENCE, firstName);
                    editor.putString(LAST_NAME_PREFERENCE, lastName);
                    editor.putString(EMAIL_PREFERENCE, email);
                    editor.putString(DESCRIPTION_PREFERENCE, description);
                    editor.apply();
                    // Notify
                    notifyListeners(new Consumer<ProfileEventListener>() {
                        @Override
                        public void accept(ProfileEventListener profileEventListener) {
                            profileEventListener.onProfileUpdated();
                        }
                    });
                }

                @Override
                public void onFailure(final String reason) {
                    notifyListeners(new Consumer<ProfileEventListener>() {
                        @Override
                        public void accept(ProfileEventListener profileEventListener) {
                            profileEventListener.onFailedToUpdateProfile(reason);
                        }
                    });
                }

                @Override
                public void onFailure(APIFailureReason response) {
                    onFailure("Server error: " + response.toString());
                }
            }, firstName, lastName, email, description).execute();
        }

        /**
         * Resets the user profile locally.
         */
        public void resetLocal() {
            firstName = "";
            lastName = "";
            email = "";
            description = "";

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(FIRST_NAME_PREFERENCE, "");
            editor.putString(LAST_NAME_PREFERENCE, "");
            editor.putString(EMAIL_PREFERENCE, "");
            editor.putString(DESCRIPTION_PREFERENCE, "");
            editor.apply();
        }

        /**
         * Gets a user profile from the server.
         * @param username Username of user to look for
         */
        public void getProfile(final String username) {
            new GetProfileAPICall(authenticationManager, new GetProfileAPIResponseListener() {
                @Override
                public void onFoundProfile(final ExternalUserProfile profile) {
                    notifyListeners(new Consumer<ProfileEventListener>() {
                        @Override
                        public void accept(ProfileEventListener profileEventListener) {
                            profileEventListener.onProfileFound(username, profile);
                        }
                    });
                }

                @Override
                public void onProfileNotFound() {
                    notifyListeners(new Consumer<ProfileEventListener>() {
                        @Override
                        public void accept(ProfileEventListener profileEventListener) {
                            profileEventListener.onProfileNotFound(username);
                        }
                    });
                }

                @Override
                public void onFailure(APIFailureReason response) {
                    onProfileNotFound();
                }
            }, username).execute();
        }
    }
}
