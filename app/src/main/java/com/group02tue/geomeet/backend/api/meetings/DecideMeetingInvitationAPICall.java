package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.AbstractCommandingAPICall;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import java.util.UUID;

public class DecideMeetingInvitationAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/join.php";
    private final RequestParams params;

    public DecideMeetingInvitationAPICall(AuthenticationManager authenticationManager,
                                      BooleanAPIResponseListener responseListener,
                                      UUID id, Boolean join) {
        super(authenticationManager, CALL_URL, responseListener);

        params = new RequestParams();
        params.add(ParamKeys.ID, id.toString());
        params.add(ParamKeys.JOIN, join.toString());
    }

    @Override
    protected RequestParams generateParams() {
        return params;
    }
}
