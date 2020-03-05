package com.group02tue.geomeet.backend.api.chat;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;
import com.group02tue.geomeet.backend.chat.ChatMessageAdapter;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiveChatMessageAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/chat/receive.php";

    public ReceiveChatMessageAPICall(AuthenticationManager authenticationManager,
                                     ReceiveChatMessageAPIResponseListener responseListener) {
        super(authenticationManager, CALL_URL, responseListener);
    }

    @Override
    protected RequestParams generateParams() {
        return new RequestParams();
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.MESSAGES)) {
            // Convert json into chat messages
            ArrayList<ChatMessage> messages = new ArrayList<>();
            JSONArray array = response.getJSONArray(JSONKeys.MESSAGES);
            for (int i = 0; i < array.length(); i++) {
                JSONObject messageJson = array.getJSONObject(i);
                messages.add(ChatMessageAdapter.read(messageJson));
            }
            // "Return"
            ((ReceiveChatMessageAPIResponseListener)responseListener).onSuccess(messages);

        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
