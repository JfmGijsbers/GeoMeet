package com.group02tue.geomeet.backend.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.LoginAPICall;
import com.group02tue.geomeet.backend.api.LoginAPIResponseListener;

public class AuthenticationManager {
    private final SharedPreferences preferences;
    private final static String USERNAME_PREFERENCE = "username";
    private final static String AUTHENTICATION_KEY_PREFERENCE = "authkey";

    private String authenticationKey = "";
    private String username = "";

    public AuthenticationManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        authenticationKey = preferences.getString(AUTHENTICATION_KEY_PREFERENCE, "");
        username = preferences.getString(USERNAME_PREFERENCE, "");
    }

    /**
     * Tries to login using the credentials in memory.
     * @param responseHandler Handler for response
     */
    public void login(final AuthenticationEventListener responseHandler) {
        login(username, authenticationKey, responseHandler);
    }

    /**
     * Tries to login. Saves the username and authentication key on disk if login is successful.
     * @param username Username
     * @param key Password or authentication key
     * @param eventListener Handler for authentication events
     */
    public void login(final String username, String key, final AuthenticationEventListener eventListener) {
        setUsername(username);
        LoginAPICall apiCall = new LoginAPICall(new LoginAPIResponseListener() {
            @Override
            public void onSuccess(String authenticationKey) {
                setAuthenticationKey(authenticationKey);
                eventListener.onSuccess();
            }

            @Override
            public void onFailure(APIFailureReason response) {
                eventListener.onFailure();
            }
        }, username, key);
        apiCall.execute();
    }

    /**
     * Checks if credentials are currently saved on disk.
     * @return Saved?
     */
    public boolean areCredentialsStored() {
        return preferences.getString(AUTHENTICATION_KEY_PREFERENCE, "").equalsIgnoreCase("") &&
                preferences.getString(USERNAME_PREFERENCE, "").equalsIgnoreCase("");
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
