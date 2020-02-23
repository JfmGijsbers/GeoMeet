package com.group02tue.geomeet.backend.chat;

import java.util.EventListener;

public interface ChatEventListener extends EventListener {
    void onNewMessageReceived(ChatMessage message);
    void onMessageSent(ChatMessage message);
    void onFailedToSendMessage(ChatMessage message, String reason);
}
