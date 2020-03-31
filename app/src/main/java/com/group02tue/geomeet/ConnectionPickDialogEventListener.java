package com.group02tue.geomeet;

import java.util.EventListener;

public interface ConnectionPickDialogEventListener extends EventListener {
    void onPickedConnection(String username);
}
