package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class QueryMeetingMembershipsAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/qmships.php";

    public QueryMeetingMembershipsAPICall(AuthenticationManager authenticationManager,
                                          QueryImmutableMeetingsAPIResponseListener responseListener) {
        super(authenticationManager, CALL_URL, responseListener);
    }

    @Override
    protected RequestParams generateParams() {
        return new RequestParams();
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.MEMBERSHIPS)) {
            ArrayList<ImmutableMeeting> memberships = new ArrayList<>();
            JSONArray array = response.getJSONArray(JSONKeys.MEMBERSHIPS);
            for (int i = 0; i < array.length(); i++) {
                JSONObject membershipJson = array.getJSONObject(i);
                if (ImmutableMeeting.checkJsonForMeeting(membershipJson)) {
                    try {
                        memberships.add(ImmutableMeeting.fromJson(membershipJson));
                    } catch (ParseException e) {
                        // Ignore: we don't need it if it is incorrect
                    }
                }
            }
            ((QueryImmutableMeetingsAPIResponseListener)responseListener).onSuccess(memberships);
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
