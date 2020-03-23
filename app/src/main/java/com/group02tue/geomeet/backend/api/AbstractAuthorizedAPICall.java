package com.group02tue.geomeet.backend.api;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.loopj.android.http.RequestParams;

public abstract class AbstractAuthorizedAPICall extends AbstractAPICall {
    private String username = "";
    private String authenticationKey = "";

    public AbstractAuthorizedAPICall(AuthenticationManager authenticationManager, String url, APIResponseListener responseListener) {
        super(url, responseListener);
        authenticationManager.addAuthenticationInfoToCall(this);
    }

    /**
     * Add information to this call to authorize it.
     * @param username Username of caller
     * @param authenticationKey Authentication key of caller
     */
    public void addAuthenticationInfo(String username, String authenticationKey) {
        this.username = username;
        this.authenticationKey = authenticationKey;
    }

    @Override
    public void execute() {
        RequestParams params = generateParams();
        params.add(ParamKeys.USERNAME, username);
        params.add(ParamKeys.AUTHENTICATION_KEY, authenticationKey);
        super.execute(params);
}
}
