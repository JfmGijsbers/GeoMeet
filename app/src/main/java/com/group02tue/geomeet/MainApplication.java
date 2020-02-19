package com.group02tue.geomeet;

import android.app.Application;

import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

public class MainApplication extends Application {
    private AuthenticationManager authenticationManager = new AuthenticationManager(getApplicationContext());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
}
