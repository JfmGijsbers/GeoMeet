package com.group02tue.geomeet;

import android.app.Application;

import com.group02tue.geomeet.backend.BackendTest;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.social.InternalUserProfile;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.UserProfile;

import java.text.SimpleDateFormat;

public class MainApplication extends Application {
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private AuthenticationManager authenticationManager;
    private ChatManager chatManager;
    private InternalUserProfile internalUserProfile;
    private InternalUserProfile.ProfileManager profileManager;
    private MeetingManager meetingManager;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticationManager = new AuthenticationManager(getApplicationContext());
        chatManager = new ChatManager(getApplicationContext(), authenticationManager);
        internalUserProfile = new InternalUserProfile(getApplicationContext(), authenticationManager);
        profileManager = internalUserProfile.new ProfileManager();
        meetingManager = new MeetingManager(getApplicationContext(), authenticationManager);

        // NOTE: part below is for testing
        //BackendTest test = new BackendTest(this);
        //test.start();
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
    public ChatManager getChatManager() {
        return chatManager;
    }
    public InternalUserProfile getInternalUserProfile() {
        return internalUserProfile;
    }
    public InternalUserProfile.ProfileManager getProfileManager() {
        return profileManager;
    }
    public MeetingManager getMeetingManager() { return meetingManager; }

    public void reset() {
        authenticationManager.reset();
        chatManager.reset();
        profileManager.resetLocal();
    }
}
