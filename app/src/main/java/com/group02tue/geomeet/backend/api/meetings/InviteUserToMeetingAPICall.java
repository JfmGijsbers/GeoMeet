package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.AbstractCommandingAPICall;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import java.util.UUID;

public class InviteUserToMeetingAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/invite.php";
    private final RequestParams params;

    public InviteUserToMeetingAPICall(AuthenticationManager authenticationManager,
                                BooleanAPIResponseListener responseListener,
                                UUID id, String userToInvite) {
        super(authenticationManager, CALL_URL, responseListener);

        params = new RequestParams();
        params.add(ParamKeys.ID, id.toString());
        params.add(ParamKeys.USER_TO_INVITE, userToInvite);
    }

    @Override
    protected RequestParams generateParams() {
        return params;
    }
}
