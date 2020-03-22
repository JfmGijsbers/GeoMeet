package com.group02tue.geomeet.backend.social;

import androidx.core.util.Consumer;

import com.group02tue.geomeet.backend.ObservableManager;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.connections.GetConnectionsAPICall;
import com.group02tue.geomeet.backend.api.connections.GetConnectionsAPIResponseListener;
import com.group02tue.geomeet.backend.api.profiles.QueryUsersAPICall;
import com.group02tue.geomeet.backend.api.profiles.QueryUsersAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import java.util.ArrayList;

public class ConnectionsManager extends ObservableManager<ConnectionsEventListener> {
    private final AuthenticationManager authenticationManager;

    public ConnectionsManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Makes a request to the server to get all connections.
     */
    public void requestConnections() {
        new GetConnectionsAPICall(authenticationManager, new GetConnectionsAPIResponseListener() {
            @Override
            public void onReceivedConnections(final ArrayList<ExternalUserProfile> connections) {
                notifyListeners(new Consumer<ConnectionsEventListener>() {
                    @Override
                    public void accept(ConnectionsEventListener connectionsEventListener) {
                        connectionsEventListener.onReceivedConnections(connections);
                    }
                });
            }

            @Override
            public void onFailure(APIFailureReason response) {
            }
        }).execute();
    }

    /**
     * Queries all users corresponding to the given queryUserString.
     * @param authenticationManager Authentication manager
     * @param responseListener Listener for responses from the server
     * @param queryUserString What to look for
     */
    public static void QueryUsernames(AuthenticationManager authenticationManager,
                                      QueryUsersAPIResponseListener responseListener,
                                      String queryUserString) {
        new QueryUsersAPICall(authenticationManager, responseListener, queryUserString).execute();
    }
}
