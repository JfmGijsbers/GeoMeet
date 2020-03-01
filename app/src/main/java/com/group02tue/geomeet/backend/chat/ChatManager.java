package com.group02tue.geomeet.backend.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.core.util.Consumer;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ReceiveChatMessageAPICall;
import com.group02tue.geomeet.backend.api.ReceiveChatMessageAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatManager extends ObservableManager<ChatEventListener> {
    private final SharedPreferences preferences;                    // Preferences reference
    private final static String MESSAGES_PREFERENCE = "messages";   // Messages key for preferences
    private final Map<UUID, ChatMessage> messages;                  // Hashtable with messages
    private final AuthenticationManager authenticationManager;      // Authentication manager

    /**
     * Constructs the chat manager.
     * @param context Application context
     * @param authenticationManager Authentication manager
     */
    public ChatManager(Context context, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Load messages from disk
        String strMessages = preferences.getString(MESSAGES_PREFERENCE, "");
        if (!strMessages.equals("")) {
            Gson gson = new Gson();
            Type messageListType = new TypeToken<Map<UUID, ChatMessage>>(){}.getType();
            this.messages = gson.fromJson(strMessages, messageListType);
        } else {
            this.messages = new HashMap<>();
        }
    }

    /**
     * Saves all messages on the disk.
     */
    private void saveMessages() {
        synchronized (messages) {
            Gson gson = new Gson();
            Type messageListType = new TypeToken<Map<UUID, ChatMessage>>(){}.getType();
            SharedPreferences.Editor prefsEditor = preferences.edit();
            prefsEditor.putString(MESSAGES_PREFERENCE, gson.toJson(messages, messageListType));
            prefsEditor.apply();
        }
    }

    /**
     * Sends a new chat message.
     * @param receiver To who the message should be send
     * @param message The content of the message
     */
    public void sendMessage(String receiver, String message)  {
        final ChatMessage messageToSend = new ChatMessage(authenticationManager.getUsername(),
                receiver, message);
        synchronized (messages) {
            messages.put(messageToSend.getId(), messageToSend);
        }
        sendMessage(messageToSend);
    }

    /**
     * Sends a chat message.
     * @param messageToSend Message to send
     */
    private void sendMessage(final ChatMessage messageToSend) {
        messageToSend.send(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                notifyListeners(new Consumer<ChatEventListener>() {
                    @Override
                    public void accept(ChatEventListener listener) {
                        listener.onMessageSent(messageToSend);
                    }
                });
                saveMessages();
            }

            @Override
            public void onFailure(final String reason) {
                notifyListeners(new Consumer<ChatEventListener>() {
                    @Override
                    public void accept(ChatEventListener listener) {
                        listener.onFailedToSendMessage(messageToSend, reason);
                    }
                });
                saveMessages();
            }

            @Override
            public void onFailure(APIFailureReason response) {
                onFailure("Server error: " + response.toString());
            }
        });
    }

    /**
     * Checks the server for new chat messages.
     */
    public void checkForNewMessages() {
        new ReceiveChatMessageAPICall(authenticationManager, new ReceiveChatMessageAPIResponseListener() {
            @Override
            public void onSuccess(final ArrayList<ChatMessage> messages) {
                notifyListeners(new Consumer<ChatEventListener>() {
                    @Override
                    public void accept(ChatEventListener listener) {
                        for (ChatMessage message : messages) {
                            listener.onNewMessageReceived(message);
                        }
                    }
                });
                saveMessages();
            }

            @Override
            public void onFailure(APIFailureReason response) {
                // TODO: log error
            }
        }).execute();
    }

    /**
     * Retries to send all message which have not been sent yet.
     */
    public void sendAllUnsendMessages() {
        synchronized (messages) {
            for (ChatMessage message : messages.values()) {
                if (!message.hasBeenSent()) {
                    sendMessage(message);
                }
            }
        }
    }

    /**
     * Resets chat database.
     */
    public void reset() {
        synchronized (messages) {
            messages.clear();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(MESSAGES_PREFERENCE, "");
            editor.apply();
        }
    }

    /*
     * TODO 1: write this adapter (part of UI).
     * TODO 2: method which convert List<ChatMessage> into array adapter for ListView (part of backend).
     */
    // public Adapter getChatMessageAdapter() { ... }
}
