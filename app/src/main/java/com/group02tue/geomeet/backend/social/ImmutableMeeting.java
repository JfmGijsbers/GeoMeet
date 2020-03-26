package com.group02tue.geomeet.backend.social;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonWriter;
import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.api.JSONKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * This meeting only contains core information about an information and can be used to display
 * meeting memberships.
 */
@JsonAdapter(ImmutableMeetingAdapter.class)
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
            Location2D.parse(object.getString(JSONKeys.LOCATION)),
            MainApplication.DATE_FORMAT.parse(object.getString(JSONKeys.MOMENT))
        );
    }

    /**
     * Serializes this immutable meeting using a JsonWriter. To be used for data storage (gson).
     * @param writer Writer to use
     * @throws IOException Incorrect Json data
     */
    public void serialize(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(ImmutableMeetingAdapter.ID_KEY).value(id.toString());
        writer.name(ImmutableMeetingAdapter.NAME_KEY).value(name);
        writer.name(ImmutableMeetingAdapter.LOCATION_KEY).value(location.toString());
        writer.name(ImmutableMeetingAdapter.MOMENT_KEY).value(MainApplication.DATE_FORMAT.format(moment));
        writer.endObject();
    }
}
