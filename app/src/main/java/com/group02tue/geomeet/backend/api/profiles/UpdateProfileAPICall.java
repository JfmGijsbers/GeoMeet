package com.group02tue.geomeet.backend.api.profiles;

import com.group02tue.geomeet.backend.api.AbstractCommandingAPICall;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

public class UpdateProfileAPICall extends AbstractCommandingAPICall {
    private final static String CALL_URL = BASE_URL + "/api/profile/update.php";

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String description;

    public UpdateProfileAPICall(AuthenticationManager authenticationManager,
                                BooleanAPIResponseListener responseListener,
                                String firstName, String lastName, String email, String description) {
        super(authenticationManager, CALL_URL, responseListener);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.FIRST_NAME, firstName);
        params.add(ParamKeys.LAST_NAME, lastName);
        params.add(ParamKeys.EMAIL, email);
        params.add(ParamKeys.DESCRIPTION, description);
        return params;
    }
}
