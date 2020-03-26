package com.group02tue.geomeet.backend.social;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class ImmutableMeetingAdapter extends TypeAdapter<ImmutableMeeting> {
    public final static String ID_KEY = "id";
    public final static String NAME_KEY = "name";
    public final static String LOCATION_KEY = "location";
    public final static String MOMENT_KEY = "moment";

    @Override
    public void write(JsonWriter out, ImmutableMeeting value) throws IOException {
        value.serialize(out);
    }

    @Override
    public ImmutableMeeting read(JsonReader in) throws IOException {
        Location2D location = null;
        UUID id = null;
        String meetingName = null;
        Date moment = null;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals(NAME_KEY)) {
                meetingName = in.nextString();
            } else if (name.equals(ID_KEY)) {
                id = UUID.fromString(in.nextString());
            } else if (name.equals(LOCATION_KEY)) {
                try {
                    location = Location2D.parse(in.nextString());
                } catch (ParseException e) {
                    throw new IOException("Failed to parse location.");
                }
            } else if (name.equals(MOMENT_KEY)) {
                try {
                    moment = MainApplication.DATE_FORMAT.parse(in.nextString());
                } catch (ParseException e) {
                    throw new IOException("Failed to load date.");
                }
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        if (location != null && id != null && meetingName != null && moment != null) {
            return new ImmutableMeeting(id, meetingName, location, moment);
        } else {
            throw new IOException("Invalid immutable meeting Json detected");
        }
    }
}
