package com.group02tue.geomeet.backend.api.profiles;

import com.group02tue.geomeet.backend.api.APIResponseListener;
import com.group02tue.geomeet.backend.social.ExternalUserProfile;

public interface GetProfileAPIResponseListener extends APIResponseListener {
    void onFoundProfile(ExternalUserProfile profile);
    void onProfileNotFound();
}
