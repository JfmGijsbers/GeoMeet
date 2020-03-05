package com.group02tue.geomeet.backend.social;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.api.JSONKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * This meeting only contains core information about an information and can be used to display
 * meeting memberships.
 */
public class ImmutableMeeting {
    public final UUID id;
    public final String name;
    public final Location2D location;
    public final Date moment;

    public ImmutableMeeting(final UUID id, final String name, final Location2D location, final Date moment) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.moment = moment;
    }

    /**
     * Checks if json contains an immutable meeting
     * @param object Object to check
     * @return Contains it?
     */
    public static boolean checkJsonForMeeting(JSONObject object) {
        if (object.has(JSONKeys.ID) && object.has(JSONKeys.NAME) && object.has(JSONKeys.LOCATION) &&
                object.has(JSONKeys.MOMENT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets an ImmutableMeeting from a json object. Make sure to use checkJsonForMeeting before
     * executing this method.
     * @param object Object containing the meeting
     * @return An immutable meeting
     * @throws JSONException Incorrect json
     * @throws ParseException Incorrect date or location in the json
     */
    public static ImmutableMeeting fromJson(JSONObject object) throws JSONException, ParseException {
        return new ImmutableMeeting(
            UUID.fromString(object.getString(JSONKeys.ID)),
            object.getString(JSONKeys.NAME),
            Location2D.parse(object.getString(JSONKeys.DESCRIPTION)),
            MainApplication.DATE_FORMAT.parse(object.getString(JSONKeys.MOMENT))
        );
    }
}
