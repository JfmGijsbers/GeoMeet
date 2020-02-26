package com.group02tue.geomeet.backend.api;

import java.util.EventListener;

public interface RegisterAPIResponseListener extends APIResponseListener {
    void onRegistered(String authenticationKey);
    /**
     * Failed to register. Reasons can be invalid username, password, email, and more.
     * @param reason
     */
    void onFailure(String reason);
}
