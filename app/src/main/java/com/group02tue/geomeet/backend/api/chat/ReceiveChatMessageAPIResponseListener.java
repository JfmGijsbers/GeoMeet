package com.group02tue.geomeet.backend.api.chat;

import com.group02tue.geomeet.backend.api.APIResponseListener;
import com.group02tue.geomeet.backend.chat.ChatMessage;

import java.util.ArrayList;

public interface ReceiveChatMessageAPIResponseListener extends APIResponseListener {
    /**
     * Successfully received chat messages from server.
     * @param messages Messages received
     */
    void onSuccess(ArrayList<ChatMessage> messages);
}
