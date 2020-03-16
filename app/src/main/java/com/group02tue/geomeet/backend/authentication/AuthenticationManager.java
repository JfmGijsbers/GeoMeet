package com.group02tue.geomeet.backend.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.util.Consumer;
import androidx.preference.PreferenceManager;

import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.authentication.LoginAPICall;
import com.group02tue.geomeet.backend.api.authentication.LoginAPIResponseListener;
import com.group02tue.geomeet.backend.api.authentication.RegisterAPICall;
import com.group02tue.geomeet.backend.api.authentication.RegisterAPIResponseListener;
import com.group02tue.geomeet.backend.social.UserProfile;

public class AuthenticationManager extends ObservableManager<AuthenticationEventListener> {
    private final SharedPreferences preferences;                            // Preferences reference
    private final static String USERNAME_PREFERENCE = "username";           // Username preference key
    private final static String AUTHENTICATION_KEY_PREFERENCE = "authkey";  // Authentication key preference key

    private String authenticationKey = "";  // Current authentication key in memory
    private String username = "";           // Current username in memory

    public AuthenticationManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        authenticationKey = preferences.getString(AUTHENTICATION_KEY_PREFERENCE, "");
        username = preferences.getString(USERNAME_PREFERENCE, "");
    }


    public void register(final String username, String password, String firstName, String lastName, String email) {
        new RegisterAPICall(new RegisterAPIResponseListener() {
            @Override
            public void onRegistered(String authenticationKey) {
                setUsername(username);
                setAuthenticationKey(authenticationKey);
                notifyListeners(new Consumer<AuthenticationEventListener>() {
                    @Override
                    public void accept(AuthenticationEventListener authenticationEventListener) {
                        authenticationEventListener.onRegistered();
                    }
                });
            }

            @Override
            public void onFailure(final String reason) {
                notifyListeners(new Consumer<AuthenticationEventListener>() {
                    @Override
                    public void accept(AuthenticationEventListener authenticationEventListener) {
                        authenticationEventListener.onAuthenticationFailure(reason);
                    }
                });
            }

            @Override
            public void onFailure(APIFailureReason response) {
                onFailure("Server error: " + response.toString());
            }
        }, username, password, firstName, lastName, email).execute();
    }

    /**
     * Tries to login using the credentials in memory.
     */
    public void login() {
        login(username, authenticationKey);
    }

    /**
     * Tries to login. Saves the username and authentication key on disk if login is successful.
     * @param username Username
     * @param key Password or authentication key
     */
    public void login(final String username, String key) {
        setUsername(username);
        new LoginAPICall(new LoginAPIResponseListener() {
            @Override
            public void onLoggedIn(String authenticationKey) {
                setAuthenticationKey(authenticationKey);
                notifyListeners(new Consumer<AuthenticationEventListener>() {
                    @Override
                    public void accept(AuthenticationEventListener authenticationEventListener) {
                        authenticationEventListener.onLoggedIn();
                    }
                });
            }

            @Override
            public void onFailure(APIFailureReason response) {
                notifyListeners(new Consumer<AuthenticationEventListener>() {
                    @Override
                    public void accept(AuthenticationEventListener authenticationEventListener) {
                        authenticationEventListener.onAuthenticationFailure("Failed to login");
                    }
                });
            }
        }, username, key).execute();
    }

    /**
     * Checks if credentials are currently saved on disk.
     * @return Saved?
     */
    public boolean areCredentialsStored() {
        return authenticationKey.equals("") && username.equals("");
    }

    /**
     * Sets an authentication key and save it.
     * @param authenticationKey Key to set
     */
    private void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTHENTICATION_KEY_PREFERENCE, authenticationKey);
        editor.apply();
    }

    /**
     * Sets a username and save it.
     * @param username Username to set
     */
    private void setUsername(String username) {
        this.username = username;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME_PREFERENCE, username);
        editor.apply();
    }

    /**
     * Resets all data.
     */
    public void reset() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USERNAME_PREFERENCE);
        editor.remove(AUTHENTICATION_KEY_PREFERENCE);
        editor.apply();
        username = "";
        authenticationKey = "";
    }

    /**
     * Adds authentication key and username to an API call which requires to be authorized.
     * @param call Call to add info to
     */
    public void addAuthenticationInfoToCall(AbstractAuthorizedAPICall call) {
        call.addAuthenticationInfo(username, authenticationKey);
    }

    /**
     * Gets the username of the current logged in user.
     * @return Username or empty if no user logged in.
     */
    public String getUsername() {
        return username;
    }
}
