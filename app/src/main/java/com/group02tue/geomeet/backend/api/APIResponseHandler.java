package com.group02tue.geomeet.backend.api;

public interface APIResponseHandler {
    /**
     * Failed to execute the API call, or API returned invalid data.
     * @param response Reason of failure
     */
    void onFailure(APIFailureReason response);
}
