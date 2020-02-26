package com.group02tue.geomeet.backend.api;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAPICall extends AbstractAPICall {
    private final static String CALL_URL = BASE_URL + "/api/register.php";

    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String email;

    public RegisterAPICall(RegisterAPIResponseListener responseListener, String username, String password, String firstName, String lastName, String email) {
        super(CALL_URL, responseListener);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.USERNAME, username);
        params.add(ParamKeys.PASSWORD, password);
        params.add(ParamKeys.FIRST_NAME, firstName);
        params.add(ParamKeys.LAST_NAME, lastName);
        params.add(ParamKeys.EMAIL, email);
        return params;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.USERNAME) && response.has(JSONKeys.AUTHENTICATION_KEY) &&
            response.getString(JSONKeys.USERNAME).equals(username)) {
            ((RegisterAPIResponseListener)responseListener).onRegistered(response.getString(JSONKeys.AUTHENTICATION_KEY));
        } else if (response.has(JSONKeys.CALL_FAIL)) {
            ((RegisterAPIResponseListener)responseListener).onFailure(response.getString(JSONKeys.CALL_FAIL));
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
