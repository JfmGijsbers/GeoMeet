package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.api.chat.ReceiveChatMessageAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatMessageAdapter;
import com.group02tue.geomeet.backend.social.Meeting;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashSet;
import java.util.UUID;

public class QueryMeetingAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/qmeeting.php";
    private final UUID idToQuery;

    public QueryMeetingAPICall(AuthenticationManager authenticationManager,
                               QueryMeetingAPIResponseListener responseListener,
                               UUID idToQuery) {
        super(authenticationManager, CALL_URL, responseListener);
        this.idToQuery = idToQuery;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.ID, idToQuery.toString());
        return params;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.NAME) && response.has(JSONKeys.LOCATION) && response.has(JSONKeys.MOMENT) &&
            response.has(JSONKeys.DESCRIPTION) && response.has(JSONKeys.MEMBERS)) {

            // Get members
            HashSet<String> members = new HashSet<>();
            JSONArray array = response.getJSONArray(JSONKeys.MEMBERS);
            for (int i = 0; i < array.length(); i++) {
                JSONObject membersJson = array.getJSONObject(i);
                if (membersJson.has(JSONKeys.MEMBER)) {
                    members.add(membersJson.getString(JSONKeys.MEMBER));
                }
            }

            // Return the meeting
            try {
                ((QueryMeetingAPIResponseListener)responseListener).onSuccess(new Meeting(
                        idToQuery,
                        response.getString(JSONKeys.NAME),
                        response.getString(JSONKeys.DESCRIPTION),
                        MainApplication.DATE_FORMAT.parse(response.getString(JSONKeys.MOMENT)),
                        Location2D.parse(response.getString(JSONKeys.LOCATION)),
                        members
                ));
            } catch (ParseException e) {
                responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
            }
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
