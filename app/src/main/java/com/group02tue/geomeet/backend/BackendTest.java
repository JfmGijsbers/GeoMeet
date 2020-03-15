package com.group02tue.geomeet.backend;

import android.content.Context;
import android.util.Log;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationEventListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatEventListener;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.InternalUserProfile;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingAsAdminEventListener;
import com.group02tue.geomeet.backend.social.MeetingAsAdminManager;
import com.group02tue.geomeet.backend.social.MeetingEventListener;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.ProfileEventListener;
import com.group02tue.geomeet.backend.social.UserProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class BackendTest implements AuthenticationEventListener, ChatEventListener, MeetingEventListener, ProfileEventListener, MeetingAsAdminEventListener {
    private AuthenticationManager authenticationManager;
    private ChatManager chatManager;
    private MeetingManager meetingManager;
    private InternalUserProfile internalUserProfile;
    private InternalUserProfile.ProfileManager profileManager;

    public BackendTest(MainApplication application) {
        authenticationManager = application.getAuthenticationManager();
        chatManager = application.getChatManager();
        internalUserProfile = application.getInternalUserProfile();
        meetingManager = application.getMeetingManager();
        profileManager = application.getProfileManager();
    }

    public void start() {
        Log.println(Log.DEBUG, "Backend", "Starting");
        authenticationManager.addListener(this);
        chatManager.addListener(this);
        meetingManager.addListener(this);
        profileManager.addListener(this);

        authenticationManager.login("firstUser", "supersecret");
        //authenticationManager.register("tester", "1", "FirstName", "LastName", "email@email.nl");
        //authenticationManager.login("tester", "1");
        //authenticationManager.login();
    }

    public void stop() {
        chatManager.removeListener(this);
        authenticationManager.removeListener(this);
        meetingManager.removeListener(this);
        profileManager.removeListener(this);
    }


    // Authentication
    @Override
    public void onLoggedIn() {
        Log.println(Log.DEBUG, "Backend", "Successfully logged in");
        //Meeting meeting = new Meeting("MyMeeting", "Test meeting", new Date(), new Location2D(1, 1), authenticationManager.getUsername());
        //meetingManager.addMeeting(meeting);
        ///Meeting meeting = meetingManager.getMeeting(UUID.fromString("4a631983-a19b-4292-8e7f-029a3bf8466b"));
        //MeetingAsAdminManager meetingAsAdminManager = new MeetingAsAdminManager(meetingManager, authenticationManager, meeting);
        //meetingAsAdminManager.removeUserFromMeeting("tester");
        //meetingAsAdminManager.inviteUserToMeeting("tester");

        //meetingManager.requestMeetingInvitations();
        //meetingManager.decideInvitation(UUID.fromString("4a631983-a19b-4292-8e7f-029a3bf8466b"), true);
        //Set<UUID> memberships = meetingManager.getMeetingMemberships();

        //meeting.sendMessage("hey all!", chatManager);
        //chatManager.checkForNewMessages();

        //profileManager.update("First", "Last", "mail@mail.com", "Testing!");
        //profileManager.getProfile(authenticationManager.getUsername());
    }

    @Override
    public void onRegistered() {
        Log.println(Log.DEBUG, "Backend", "Successfully registered");
    }

    @Override
    public void onAuthenticationFailure(String reason) {
        Log.println(Log.DEBUG, "Backend", "Failed to login/register, reason: " + reason);
    }

    // Chat
    @Override
    public void onNewMessageReceived(ChatMessage message) {
        Log.println(Log.DEBUG, "BackendChat", "Received a new message");
    }

    @Override
    public void onMessageSent(ChatMessage message) {
        Log.println(Log.DEBUG, "BackendChat", "Successfully sent a message");
    }

    @Override
    public void onFailedToSendMessage(ChatMessage message, String reason) {
        Log.println(Log.DEBUG, "BackendChat", "Failed to send a message, reason: " + reason);
    }


    // Meetings
    @Override
    public void onRemovedMeeting(UUID id) {
        Log.println(Log.DEBUG, "BackendMeeting", "Removed meeting: " + id.toString());
    }

    @Override
    public void onCreatedMeeting(UUID id) {
        Log.println(Log.DEBUG, "BackendMeeting", "Created meeting: " + id.toString());
    }

    @Override
    public void onFailure(UUID id, String reason) {
        Log.println(Log.DEBUG, "BackendMeeting", "Failed to execute call for meeting: " + id.toString() + ", with reason: " + reason);
    }

    @Override
    public void onMeetingUpdatedReceived(Meeting meeting) {
        Log.println(Log.DEBUG, "BackendMeeting", "Received update for meeting " + meeting.getId().toString());
    }

    @Override
    public void onLeftMeeting(UUID id) {
        Log.println(Log.DEBUG, "BackendMeeting", "Left meeting: " + id.toString());
    }

    @Override
    public void onReceivedMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
        Log.println(Log.DEBUG, "BackendMeeting", "Invited to meetings: " + meetings.size());
    }

    // Meeting as admin
    @Override
    public void onRemovedUserFromMeeting(UUID id, String userRemoved) {
        Log.println(Log.DEBUG, "BackendMeetingAdmin", "Removed user from meeting: " + id.toString());
    }

    @Override
    public void onInvitedUserToMeeting(UUID id, String userAdded) {
        Log.println(Log.DEBUG, "BackendMeetingAdmin", "Invited user to meeting: " + id.toString());
    }

    @Override
    public void onFailedToEditMeeting(UUID id, String reason) {
        Log.println(Log.DEBUG, "BackendMeetingAdmin", "Failed to modify meeting: " + id.toString());
    }

    @Override
    public void onUpdatedMeeting(UUID id) {
        Log.println(Log.DEBUG, "BackendMeetingAdmin", "Successfully updated meeting: " + id.toString());
    }

    // Profile
    @Override
    public void onProfileUpdated() {
        Log.println(Log.DEBUG, "BackendProfile", "Updated profile");
    }

    @Override
    public void onFailedToUpdateProfile(String reason) {
        Log.println(Log.DEBUG, "BackendProfile", "Failed to update profile");
    }

    @Override
    public void onProfileFound(String requestedUser, ExternalUserProfile profile) {
        Log.println(Log.DEBUG, "BackendProfile", "Found profile for " + requestedUser + " with name " + profile.getFirstName());
    }

    @Override
    public void onProfileNotFound(String requestedUser) {
        Log.println(Log.DEBUG, "BackendProfile", "Failed to find profile for " + requestedUser);
    }
}
