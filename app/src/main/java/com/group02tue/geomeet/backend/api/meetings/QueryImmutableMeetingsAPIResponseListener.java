package com.group02tue.geomeet.backend.api.meetings;

import com.group02tue.geomeet.backend.api.APIResponseListener;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;

import java.util.ArrayList;

public interface QueryImmutableMeetingsAPIResponseListener extends APIResponseListener {
    void onSuccess(ArrayList<ImmutableMeeting> meetings);
}
