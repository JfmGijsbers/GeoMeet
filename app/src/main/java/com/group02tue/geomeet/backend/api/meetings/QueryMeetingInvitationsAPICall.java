package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.api.chat.ReceiveChatMessageAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;
import com.group02tue.geomeet.backend.chat.ChatMessageAdapter;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class QueryMeetingInvitationsAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/qinvites.php";

    public QueryMeetingInvitationsAPICall(AuthenticationManager authenticationManager,
                                            QueryImmutableMeetingsAPIResponseListener responseListener) {
        super(authenticationManager, CALL_URL, responseListener);
    }

    @Override
    protected RequestParams generateParams() {
        return new RequestParams();
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        if (response.has(JSONKeys.INVITES)) {
            ArrayList<ImmutableMeeting> invites = new ArrayList<>();
            JSONArray array = response.getJSONArray(JSONKeys.INVITES);
            for (int i = 0; i < array.length(); i++) {
                JSONObject inviteJson = array.getJSONObject(i);
                if (ImmutableMeeting.checkJsonForMeeting(inviteJson)) {
                    try {
                        invites.add(ImmutableMeeting.fromJson(inviteJson));
                    } catch (ParseException e) {
                        // Ignore: we don't need it if it is incorrect
                    }
                }
            }
            ((QueryImmutableMeetingsAPIResponseListener)responseListener).onSuccess(invites);
        } else {
            responseListener.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }
}
