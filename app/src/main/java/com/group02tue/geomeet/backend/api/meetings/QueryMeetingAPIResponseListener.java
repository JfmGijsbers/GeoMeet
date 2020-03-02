package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.APIResponseListener;
import com.group02tue.geomeet.backend.social.Meeting;

public interface QueryMeetingAPIResponseListener extends APIResponseListener {
    void onSuccess(Meeting meeting);
}
