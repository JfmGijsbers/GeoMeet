package com.group02tue.geomeet.backend.chat;

import java.util.EventListener;

public interface ChatEventListener extends EventListener {
    /**
     * Received a new message.
     * @param message Message received
     */
    void onNewMessageReceived(ChatMessage message);

    /**
     * Successfully sent a message.
     * @param message Message sent
     */
    void onMessageSent(ChatMessage message);

    /**
     * Failed to send a message.
     * @param message Message failed to send
     * @param reason Reason of failing
     */
    void onFailedToSendMessage(ChatMessage message, String reason);
}
