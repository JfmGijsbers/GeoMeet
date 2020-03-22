package com.group02tue.geomeet.backend.api.profiles;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class QueryUsersAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/profile/query.php";
    private final String queryUserString;

    public QueryUsersAPICall(AuthenticationManager authenticationManager,
                             QueryUsersAPIResponseListener responseListener,
                             String queryUserString) {
        super(authenticationManager, CALL_URL, responseListener);
        this.queryUserString = queryUserString;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.QUERY_STRING, queryUserString);
        return params;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.FOUND)) {
            HashSet<String> foundUsers = new HashSet<>();
            JSONArray array = response.getJSONArray(JSONKeys.FOUND);
            for (int i = 0; i < array.length(); i++) {
                String membersJson = array.getString(i);
                if (membersJson != null) {
                    foundUsers.add(membersJson);
                }
            }
            ((QueryUsersAPIResponseListener)responseListener).onFoundUsernames(queryUserString, foundUsers);
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
