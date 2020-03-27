package com.group02tue.geomeet.backend.api.chat;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.api.AbstractCommandingAPICall;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatMessageAdapter;
import com.loopj.android.http.RequestParams;

import java.util.Date;
import java.util.UUID;

public class SendChatMessageAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/chat/send.php";

    private final String receiver;
    private final String content;
    private final UUID id;
    private final Date moment;

    public SendChatMessageAPICall(AuthenticationManager authenticationManager,
                                  BooleanAPIResponseListener responseListener,
                                  String receiver, String content, UUID id, Date moment) {
        super(authenticationManager, CALL_URL, responseListener);
        this.receiver = receiver;
        this.content = content;
        this.id = id;
        this.moment = moment;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.RECEIVER, receiver);
        params.add(ParamKeys.CONTENT, content);
        params.add(ParamKeys.ID, id.toString());
        params.add(ParamKeys.MOMENT, MainApplication.DATE_FORMAT.format(moment));
        return params;
    }
}
