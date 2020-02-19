package com.group02tue.geomeet.backend.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public abstract class AbstractAPICall extends JsonHttpResponseHandler {
    private static AsyncHttpClient client = new AsyncHttpClient();  // Client for Http calls

    protected final static String BASE_URL = "http://test.com";     // Base URL of the API
    private final String url;                                       // URL of API
    protected final APIResponseHandler responseHandler;             // Handler for failure handling


    /**
     * Constructor
     * @param url URL of the API
     * @param responseHandler Handler for failure handling
     */
    public AbstractAPICall(String url, APIResponseHandler responseHandler) {
        this.url = url;
        this.responseHandler = responseHandler;
    }
    /**
     * Executes the call.
     */
    public void execute() {
        execute(generateParams());
    }

    /**
     * Executes the call.
     * @param params Params to use in the call
     */
    protected void execute(RequestParams params) {
        client.post(url, params, this);
    }

    /**
     * Generates parameters for the HTTP call.
     * @return Params generated
     */
    protected abstract RequestParams generateParams();

    /**
     * Processes the successfully executed the HTTP call.
     * @param statusCode Status code
     * @param headers Headers
     * @param response The JSON data returned by the API
     */
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            if (response.has(JSONKeys.INVALID_INPUT)) {
                responseHandler.onFailure(APIFailureReason.INVALID_INPUT);
            } else if (response.has(JSONKeys.UNAUTHORIZED)) {
                responseHandler.onFailure(APIFailureReason.UNAUTHORIZED);
            } else {
                processResponse(response);
            }
        } catch (JSONException e) {
            responseHandler.onFailure(APIFailureReason.INVALID_OUTPUT);
        }
    }

    /**
     * Failed to execute the API call.
     */
    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
        responseHandler.onFailure(APIFailureReason.NO_CONNECTION);
    }

    /**
     * Processes the data from the API call in the backend and checks its integrity.
     * @param response Data from the API call
     * @throws JSONException If the JSON data is invalid
     */
    protected abstract void processResponse(JSONObject response) throws JSONException;
}
