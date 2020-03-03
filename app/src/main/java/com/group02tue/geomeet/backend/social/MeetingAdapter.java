package com.group02tue.geomeet.backend.social;

import android.location.Location;

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
import java.util.Set;
import java.util.UUID;

public class MeetingAdapter extends TypeAdapter<Meeting> {
    public final static String ID_KEY = "id";
    public final static String NAME_KEY = "name";
    public final static String LOCATION_KEY = "location";
    public final static String MOMENT_KEY = "moment";
    public final static String MEMBERS_KEY = "members";
    public final static String DESCRIPTION_KEY = "description";
    public final static String ADMIN_USERNAME_KEY = "adminUsername";


    @Override
    public void write(JsonWriter out, Meeting value) throws IOException {
        value.serialize(out);
    }

    @Override
    public Meeting read(JsonReader in) throws IOException {
        Location2D location = null;
        UUID id = null;
        String meetingName = null;
        String description = null;
        Date moment = null;
        Set<String> members = null;
        String adminUsername = null;

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
            } else if (name.equals(DESCRIPTION_KEY)) {
                description = in.nextString();
            } else if (name.equals(MEMBERS_KEY)) {
                Type setType = new TypeToken<HashSet<String>>() {
                }.getType();
                members = new Gson().fromJson(in.nextString(), setType);
            } else if (name.equals(ADMIN_USERNAME_KEY)) {
                adminUsername = in.nextString();
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        if (location != null && id != null && meetingName != null && description != null &&
            moment != null && members != null && adminUsername != null) {
            return new Meeting(id, meetingName, description, moment, location, adminUsername, members);
        } else {
            throw new IOException("Invalid meeting Json detected");
        }
    }
}
