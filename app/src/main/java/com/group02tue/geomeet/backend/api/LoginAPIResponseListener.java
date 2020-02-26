package com.group02tue.geomeet.backend.api;

public interface LoginAPIResponseListener extends APIResponseListener {
    /**
     * Successfully logged in.
     * @param authenticationKey Authentication key provided by the server
     */
    void onLoggedIn(String authenticationKey);
}
