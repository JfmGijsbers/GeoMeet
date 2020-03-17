package com.group02tue.geomeet.backend.social;

import java.util.ArrayList;
import java.util.EventListener;

public interface ConnectionsEventListener extends EventListener {
    /**
     * Received connections from the server.
     * @param connections Connections received
     */
    void onReceivedConnections(ArrayList<ExternalUserProfile> connections);
}
