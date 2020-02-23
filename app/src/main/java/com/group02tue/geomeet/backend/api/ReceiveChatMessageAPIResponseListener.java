package com.group02tue.geomeet.backend.api;

import com.group02tue.geomeet.backend.chat.ChatMessage;

import java.util.ArrayList;

public interface ReceiveChatMessageAPIResponseListener extends APIResponseListener {
    void onSuccess(ArrayList<ChatMessage> messages);
}
