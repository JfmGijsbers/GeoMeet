package com.group02tue.geomeet.backend.social;


import androidx.core.util.Consumer;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonWriter;
import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.Location2D;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.meetings.CreateMeetingAPICall;
import com.group02tue.geomeet.backend.api.meetings.InviteUserToMeetingAPICall;
import com.group02tue.geomeet.backend.api.meetings.RemoveUserFromMeetingAPICall;
import com.group02tue.geomeet.backend.api.meetings.UpdateMeetingAPICall;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;

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
    private final String adminUsername;

    public Meeting(String name, String description, Date moment, Location2D location, String adminUsername) {
        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.moment = moment;
        this.location = location;
        this.adminUsername = adminUsername;
        members = new HashSet<>();
    }

    public Meeting(UUID id, String name, String description, Date moment, Location2D location, String adminUsername, Set<String> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.moment = moment;
        this.location = location;
        this.members = members;
        this.adminUsername = adminUsername;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {return name; }
    public String getDescription() { return description; }
    public Date getMoment() { return moment; }
    public Location2D getLocation() { return location; }


    /**
     * Sends a message in the chat of the meeting.
     * @param message Message content to send
     * @param chatManager Chat manager
     */
    public void sendMessage(String message, ChatManager chatManager) {
        chatManager.sendMessage(id.toString(), message);
    }

    /**
     * Invites a new user to the meeting. Only executable by the admin of the meeting.
     * @param username Username to invite
     * @param authenticationManager Authentication manager
     * @param responseListener Listener for responses
     */
    public void inviteUser(final String username, AuthenticationManager authenticationManager,
                           final BooleanAPIResponseListener responseListener) {
        new InviteUserToMeetingAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                synchronized (members) {
                    members.add(username);
                }
                responseListener.onSuccess();
            }

            @Override
            public void onFailure(String reason) {
                responseListener.onFailure(reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                responseListener.onFailure(response);
            }
        }, id, username).execute();
    }

    /**
     * Removes a user from a meeting. Only executable by the admin of the meeting.
     * @param username Username to invite
     * @param authenticationManager Authentication manager
     * @param responseListener Listener for responses
     */
    public void removeUser(final String username, AuthenticationManager authenticationManager,
                           final BooleanAPIResponseListener responseListener) {
        new RemoveUserFromMeetingAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                synchronized (members) {
                    if (isMember(username)) {
                        members.remove(username);
                    }
                }
                responseListener.onSuccess();
            }

            @Override
            public void onFailure(String reason) {
                responseListener.onFailure(reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                responseListener.onFailure(response);
            }
        }, id, username).execute();
    }

    /**
     * Checks if a certain user a member of the meeting.
     * @param username Username to check
     * @return Is a member?
     */
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
        writer.name(MeetingAdapter.ADMIN_USERNAME_KEY).value(adminUsername);
        writer.endObject();
    }
}
