package com.group02tue.geomeet.backend.api;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAPICall extends AbstractAPICall {
    private final static String LOGIN_CALL_URL = BASE_URL + "/test/test.php";
    private final String username;
    private final String password;

    public LoginAPICall(LoginAPIResponseHandler responseHandler, String username, String password) {
        super(LOGIN_CALL_URL, responseHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.USERNAME, username);
        params.add(ParamKeys.PASSWORD, password);
        return params;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.USERNAME) && response.has(JSONKeys.AUTHENTICATION_KEY)
                && response.getString(JSONKeys.USERNAME).equals(username)) {
            String authenticationKey = response.getString(JSONKeys.AUTHENTICATION_KEY);
            ((LoginAPIResponseHandler)responseHandler).onSuccess(authenticationKey);
        } else {
            responseHandler.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
