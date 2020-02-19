package com.group02tue.geomeet.backend.api;

public interface LoginAPIResponseHandler extends APIResponseHandler {
    /**
     * Successfully logged in.
     * @param authenticationKey Authentication key provided by the server
     */
    void onSuccess(String authenticationKey);
}
