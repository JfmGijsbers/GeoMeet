package com.group02tue.geomeet.backend.api;

/**
 * To be used to differ between success and failure of API call, without any return values from
 * the API.
 */
public interface BooleanAPIResponseListener extends APIResponseListener {
    /**
     * To be called if API call has been finished successfully.
     */
    void onSuccess();

    /**
     * To be called if API call has been executed successfully, but has not finished successfully.
     * @param reason Reason why the call failed to finish
     */
    void onFailure(String reason);
}
