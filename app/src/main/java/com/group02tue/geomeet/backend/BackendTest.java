package com.group02tue.geomeet.backend;

import android.content.Context;
import android.util.Log;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.authentication.AuthenticationEventListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatEventListener;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;
import com.group02tue.geomeet.backend.social.UserProfile;

public class BackendTest implements AuthenticationEventListener, ChatEventListener {
    private AuthenticationManager authenticationManager;
    private ChatManager chatManager;
    private UserProfile userProfile;

    public BackendTest(MainApplication application) {
        authenticationManager = application.getAuthenticationManager();
        chatManager = application.getChatManager();
        userProfile = application.getUserProfile();
    }

    public void start() {
        Log.println(Log.DEBUG, "Backend", "Starting");
        authenticationManager.addListener(this);
        authenticationManager.login("firstUser", "supersecret");
        chatManager.addListener(this);
    }

    public void stop() {
        chatManager.removeListener(this);
        authenticationManager.removeListener(this);
    }


    @Override
    public void onLoggedIn() {
        Log.println(Log.DEBUG, "Backend", "Successfully logged in");
    }

    @Override
    public void onRegistered() {
        Log.println(Log.DEBUG, "Backend", "Successfully registered");
    }

    @Override
    public void onAuthenticationFailure(String reason) {
        Log.println(Log.DEBUG, "Backend", "Failed to login/register, reason: " + reason);
    }

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
}
