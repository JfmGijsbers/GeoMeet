package com.group02tue.geomeet.backend.chat;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonWriter;
import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.BooleanAPIResponseListener;
import com.group02tue.geomeet.backend.api.chat.SendChatMessageAPICall;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@JsonAdapter(ChatMessageAdapter.class)
public class ChatMessage {
    private final UUID id;          // Unique ID of the message, saved
    private final String sender;    // Sender of the message, saved
    private final String receiver;  // Receiver of the message, saved
    private final String content;   // Content of the message, saved
    private final Date moment;      // Moment of sending, saved
    private boolean isSent;         // Has the message successfully been sent?, saved
    private boolean isBeingSent = false;    // Is the message being sent?, NOT saved

    public boolean hasBeenSent() {
        return isSent;
    }

    /**
     * Initializes a new chat message.
     * @param sender Sender of the message
     * @param receiver Receiver of the message
     * @param content Content of the message
     */
    public ChatMessage(String sender, String receiver, String content) {
        this(UUID.randomUUID(), sender, receiver, content, Calendar.getInstance().getTime(), false);
    }

    /**
     * Initializes a chat message.
     * @param id ID of the message
     * @param sender Sender of the message
     * @param receiver Receiver of the message
     * @param content Content of the message
     * @param moment Moment of sending
     * @param isSent Has the message been sent successfully?
     */
    public ChatMessage(UUID id, String sender, String receiver, String content, Date moment, boolean isSent) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isSent = isSent;
        this.moment = moment;
    }

    public UUID getId() {
        return id;
    }

    /**
     * Tries to send the message to the server.
     * @param authenticationManager Authentication manager
     * @param responseListener Listener which handles the result from the API
     */
    public void send(AuthenticationManager authenticationManager, final BooleanAPIResponseListener responseListener) {
        if (isBeingSent || isSent) {    // Already (being) sent: do no send the message again
            return;
        }

        isBeingSent = true;
        new SendChatMessageAPICall(authenticationManager, new BooleanAPIResponseListener() {
            @Override
            public void onSuccess() {
                isSent = true;
                isBeingSent = false;
                responseListener.onSuccess();
            }

            @Override
            public void onFailure(String reason) {
                isBeingSent = false;
                responseListener.onFailure(reason);
            }

            @Override
            public void onFailure(APIFailureReason response) {
                isBeingSent = false;
                responseListener.onFailure(response);
            }
        }, receiver, content, id, moment).execute();
    }

    /**
     * Serializes this message using a JsonWriter. To be used for data storage (gson).
     * @param writer Writer to use
     * @throws IOException Incorrect Json data
     */
    public void serialize(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("id").value(id.toString());
        writer.name("receiver").value(receiver);
        writer.name("sender").value(sender);
        writer.name("content").value(content);
        writer.name("isSent").value(isSent);
        writer.name("moment").value(MainApplication.DATE_FORMAT.format(moment));
        writer.endObject();
    }
}
