package com.group02tue.geomeet.backend.authentication;

import java.util.EventListener;

public interface AuthenticationEventListener extends EventListener {
    void onSuccess();
    void onFailure();
}
