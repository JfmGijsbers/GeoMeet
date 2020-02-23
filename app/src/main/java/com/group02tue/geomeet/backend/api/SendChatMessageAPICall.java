package com.group02tue.geomeet.backend.api;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

public class SendChatMessageAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/sendchat.php";

    private final String messageReceiver;
    private final String messageContent;

    public SendChatMessageAPICall(AuthenticationManager authenticationManager,
                                  BooleanAPIResponseListener responseListener,
                                  String messageReceiver, String messageContent) {
        super(authenticationManager, CALL_URL, responseListener);
        this.messageReceiver = messageReceiver;
        this.messageContent = messageContent;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.CHAT_MESSAGE_RECEIVER, messageReceiver);
        params.add(ParamKeys.CHAT_MESSAGE_CONTENT, messageContent);
        return params;
    }
}
