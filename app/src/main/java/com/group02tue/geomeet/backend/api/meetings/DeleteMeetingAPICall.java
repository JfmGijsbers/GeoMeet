package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.AbstractCommandingAPICall;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import java.util.UUID;

public class DeleteMeetingAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/delete.php";
    private final RequestParams params;

    public DeleteMeetingAPICall(AuthenticationManager authenticationManager,
                                BooleanAPIResponseListener responseListener,
                                UUID id) {
        super(authenticationManager, CALL_URL, responseListener);

        params = new RequestParams();
        params.add(ParamKeys.ID, id.toString());
    }

    @Override
    protected RequestParams generateParams() {
        return params;
    }
}
