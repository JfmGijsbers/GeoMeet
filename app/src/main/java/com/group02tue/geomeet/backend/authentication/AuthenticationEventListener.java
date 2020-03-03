package com.group02tue.geomeet.backend.authentication;

import java.util.EventListener;

public interface AuthenticationEventListener extends EventListener {
    /**
     * Successfully logged in.
     */
    void onLoggedIn();

    /**
     * Successfully registered.
     */
    void onRegistered();

    /**
     * Failed to login/register.
     * @param reason Reason for failing
     */
    void onAuthenticationFailure(String reason);
}
