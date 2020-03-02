package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.api.AbstractCommandingAPICall;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

import java.util.Date;
import java.util.UUID;

public class UpdateMeetingAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/meeting/update.php";
    private final RequestParams params;

    public UpdateMeetingAPICall(AuthenticationManager authenticationManager,
                                BooleanAPIResponseListener responseListener,
                                UUID id, String name, Location2D location, Date moment, String description) {
        super(authenticationManager, CALL_URL, responseListener);

        params = new RequestParams();
        params.add(ParamKeys.ID, id.toString());
        params.add(ParamKeys.NAME, name);
        params.add(ParamKeys.LOCATION, location.toString());
        params.add(ParamKeys.MOMENT, MainApplication.DATE_FORMAT.format(moment));
        params.add(ParamKeys.DESCRIPTION, description);
    }

    @Override
    protected RequestParams generateParams() {
        return params;
    }
}
