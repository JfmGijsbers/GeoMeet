package com.group02tue.geomeet.backend.api.connections;

import com.group02tue.geomeet.backend.api.APIResponseListener;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;

import java.util.ArrayList;


public interface GetConnectionsAPIResponseListener extends APIResponseListener {
    /**
     * Received a list of connections from the server.
     * @param connections
     */
    void onReceivedConnections(ArrayList<ExternalUserProfile> connections);
}
