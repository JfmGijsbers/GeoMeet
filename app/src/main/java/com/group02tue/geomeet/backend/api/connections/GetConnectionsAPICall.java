package com.group02tue.geomeet.backend.api.connections;

import android.util.Log;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetConnectionsAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/connections/get.php";

    public GetConnectionsAPICall(AuthenticationManager authenticationManager,
                                 GetConnectionsAPIResponseListener responseListener) {
        super(authenticationManager, CALL_URL, responseListener);
    }

    @Override
    protected RequestParams generateParams() {
        return new RequestParams();
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.CONNECTIONS)) {
            ArrayList<ExternalUserProfile> connections = new ArrayList<>();
            JSONArray array = response.getJSONArray(JSONKeys.CONNECTIONS);
            for (int i = 0; i < array.length(); i++) {
                JSONObject profileJson = array.getJSONObject(i);
                if (profileJson.has(JSONKeys.USERNAME) && profileJson.has(JSONKeys.CONNECTION_COUNT)) {
                    ExternalUserProfile profile = ExternalUserProfile.read(
                            profileJson.getString(JSONKeys.USERNAME), profileJson);
                    if (profile != null) {
                        int connectionCount = profileJson.getInt(JSONKeys.CONNECTION_COUNT);    // TODO: not used
                        connections.add(profile);

                    }
                }
            }
            ((GetConnectionsAPIResponseListener)responseListener).onReceivedConnections(connections);
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
