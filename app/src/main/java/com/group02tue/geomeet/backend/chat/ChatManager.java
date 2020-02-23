package com.group02tue.geomeet.backend.chat;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.util.Consumer;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatManager extends ObservableManager<ChatEventListener> {
    private final SharedPreferences preferences;
    private final static String MESSAGES_PREFERENCE = "messages";
    private final List<ChatMessage> messages;

    public ChatManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // TODO: test code below
        String strMessages = preferences.getString(MESSAGES_PREFERENCE, "");
        if (!strMessages.equals("")) {
            Gson gson = new Gson();
            Type messageListType = new TypeToken<List<ChatMessage>>(){}.getType();
            this.messages = gson.fromJson(strMessages, messageListType);
        } else {
            this.messages = new ArrayList<ChatMessage>();
        }
    }

    /**
     * Saves all messages on the disk. TODO: test this
     */
    private void saveMessages() {
        synchronized (messages) {
            Gson gson = new Gson();
            Type messageListType = new TypeToken<List<ChatMessage>>(){}.getType();
            SharedPreferences.Editor prefsEditor = preferences.edit();
            prefsEditor.putString(MESSAGES_PREFERENCE, gson.toJson(messages, messageListType));
            prefsEditor.apply();
        }
    }

    /**
     * Send a new chat message.
     * @param authenticationManager Authentication manager
     * @param receiver To who the message should be send
     * @param message The content of the message
     */
    public void sendMessage(AuthenticationManager authenticationManager,
                            String receiver, String message)  {
        final ChatMessage messageToSend = new ChatMessage(authenticationManager.getUsername(),
                receiver, message);
        synchronized (messages) {
            messages.add(messageToSend);
        }
        sendMessage(authenticationManager, messageToSend);
    }

    /**
     * Send a chat message.
     * @param authenticationManager Authentication manager
     * @param messageToSend Message to send
     */
    private void sendMessage(AuthenticationManager authenticationManager, final ChatMessage messageToSend) {
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


    public void checkForNewMessages() {

    }

    /**
     * Retry to send all message not sent yet.
     * @param authenticationManager
     */
    public void sendAllUnsendMessages(AuthenticationManager authenticationManager) {
        synchronized (messages) {
            for (ChatMessage message : messages) {
                if (!message.hasBeenSent()) {
                    sendMessage(authenticationManager, message);
                }
            }
        }
    }
}
