package com.group02tue.geomeet.backend.social;


import androidx.core.util.Consumer;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonWriter;
import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@JsonAdapter(MeetingAdapter.class)
public class Meeting {
    private final UUID id;
    private String name;
    private String description;
    private Date moment;
    private Location2D location;
    private final Set<String> members;

    public Meeting(String name, String description, Date moment, Location2D location) {
        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.moment = moment;
        this.location = location;
        members = new HashSet<>();
    }

    public Meeting(UUID id, String name, String description, Date moment, Location2D location, Set<String> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.moment = moment;
        this.location = location;
        this.members = members;
    }

    public UUID getId() {
        return id;
    }

    public void inviteUser(String username, Runnable saveAction, AuthenticationManager authenticationManager) {
        synchronized (members) {
            members.add(username);
            saveAction.run();
            // TODO: sync
        }
    }

    public void removeUser(String username, Runnable saveAction, AuthenticationManager authenticationManager) {
        synchronized (members) {
            if (isMember(username)) {
                members.remove(username);
                saveAction.run();
                // TODO: sync
            }
        }
    }

    public boolean isMember(String username) {
        synchronized (members) {
            return members.contains(username);
        }
    }

    /**
     * Serializes this meeting using a JsonWriter. To be used for data storage (gson).
     * @param writer Writer to use
     * @throws IOException Incorrect Json data
     */
    public void serialize(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(MeetingAdapter.MEMBERS_KEY).value(new Gson().toJson(members));
        writer.name(MeetingAdapter.NAME_KEY).value(name);
        writer.name(MeetingAdapter.DESCRIPTION_KEY).value(description);
        writer.name(MeetingAdapter.ID_KEY).value(id.toString());
        writer.name(MeetingAdapter.LOCATION_KEY).value(location.toString());
        writer.name(MeetingAdapter.MOMENT_KEY).value(MainApplication.DATE_FORMAT.format(moment));
        writer.endObject();
    }
}
