package com.group02tue.geomeet.backend.api;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveChatMessageAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/receive.php";

    public ReceiveChatMessageAPICall(AuthenticationManager authenticationManager,
                                     ReceiveChatMessageAPIResponseListener responseListener) {
        super(authenticationManager, CALL_URL, responseListener);
    }

    @Override
    protected RequestParams generateParams() {
        return null;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        // https://stackoverflow.com/questions/38273414/loopj-jsonobject-with-inside-jsonarray-jsonobjects
    }
}
