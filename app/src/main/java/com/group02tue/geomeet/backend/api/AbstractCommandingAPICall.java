package com.group02tue.geomeet.backend.api;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Special type of API call which commands the server to do something. These type of calls always
 * return success or failure (and if failure, also a reason).
 */
public abstract class AbstractCommandingAPICall extends AbstractAuthorizedAPICall {
    public AbstractCommandingAPICall(AuthenticationManager authenticationManager,
                                     String url,
                                     BooleanAPIResponseListener responseListener) {
        super(authenticationManager, url, responseListener);
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.CALL_OK)) {
            ((BooleanAPIResponseListener) responseListener).onSuccess();
        } else if (response.has(JSONKeys.CALL_FAIL)) {
            String failureReason = response.getString(JSONKeys.CALL_FAIL);
            ((BooleanAPIResponseListener) responseListener).onFailure(failureReason);
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
