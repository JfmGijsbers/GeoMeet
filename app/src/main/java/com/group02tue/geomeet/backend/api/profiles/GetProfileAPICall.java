package com.group02tue.geomeet.backend.api.profiles;

import com.group02tue.geomeet.backend.api.AbstractAuthorizedAPICall;
import com.group02tue.geomeet.backend.api.JSONKeys;
import com.group02tue.geomeet.backend.api.ParamKeys;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class GetProfileAPICall extends AbstractAuthorizedAPICall {
    private final static String CALL_URL = BASE_URL + "/api/profile/get.php";
    private final String userToGet;

    public GetProfileAPICall(AuthenticationManager authenticationManager,
                             GetProfileAPIResponseListener responseListener,
                             String userToGet) {
        super(authenticationManager, CALL_URL, responseListener);
        this.userToGet = userToGet;
    }

    @Override
    protected RequestParams generateParams() {
        RequestParams params = new RequestParams();
        params.add(ParamKeys.USER_TO_GET, userToGet);
        return params;
    }

    @Override
    protected void processResponse(JSONObject response) throws JSONException {
        ExternalUserProfile profile = ExternalUserProfile.read(userToGet, response);
        if (profile != null) {
            ((GetProfileAPIResponseListener) responseListener).onFoundProfile(profile);
        } else {
            ((GetProfileAPIResponseListener) responseListener).onProfileNotFound();
        }
    }
}
