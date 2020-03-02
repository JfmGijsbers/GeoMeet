package com.group02tue.geomeet.backend.api.authentication;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAPICall extends AbstractAPICall {
    private final static String LOGIN_CALL_URL = BASE_URL + "/php/SQLfiles/webtech.login.php";
    private final String username;
    private final String key;       // Can be password or authentication key

    public LoginAPICall(LoginAPIResponseListener responseListener, String username, String key) {
        super(LOGIN_CALL_URL, responseListener);
        this.username = username;
        this.key = key;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.USERNAME, username);
        params.add(ParamKeys.PASSWORD, key);
        return params;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.USERNAME) && response.has(JSONKeys.AUTHENTICATION_KEY)
                && response.getString(JSONKeys.USERNAME).equals(username)) {
            String authenticationKey = response.getString(JSONKeys.AUTHENTICATION_KEY);
            ((LoginAPIResponseListener)responseListener).onLoggedIn(authenticationKey);
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
