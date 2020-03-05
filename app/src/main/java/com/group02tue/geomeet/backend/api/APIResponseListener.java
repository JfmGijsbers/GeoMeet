package com.group02tue.geomeet.backend.api;

import java.util.EventListener;

public interface APIResponseListener extends EventListener {
    /**
     * Failed to execute the API call, or API returned invalid data.
     * @param response Reason of failure
     */
    void onFailure(APIFailureReason response);
}
