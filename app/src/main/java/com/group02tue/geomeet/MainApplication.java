package com.group02tue.geomeet;

import android.app.Application;

import com.group02tue.geomeet.backend.BackendTest;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.social.UserProfile;

public class MainApplication extends Application {
    private AuthenticationManager authenticationManager;
    private ChatManager chatManager;
    private UserProfile userProfile;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticationManager = new AuthenticationManager(getApplicationContext());
        chatManager = new ChatManager(getApplicationContext(), authenticationManager);
        userProfile = new UserProfile(getApplicationContext(), authenticationManager);

        // NOTE: part below is for testing
        BackendTest test = new BackendTest(this);
        test.start();
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
    public ChatManager getChatManager() {
        return chatManager;
    }
    public UserProfile getUserProfile() { return userProfile; }
}
