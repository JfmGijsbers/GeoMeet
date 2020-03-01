package com.group02tue.geomeet.backend.social;

import java.util.EventListener;
import java.util.UUID;

public interface MeetingEventListener extends EventListener {
    void onNewMeetingAvailable(UUID id);
    void onMeetingRemoved(UUID id);
    void onMeetingUpdated(UUID id);

}
