package com.group02tue.geomeet.backend.chat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChatMessageAdapter extends TypeAdapter<ChatMessage> {
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Convert chat message into Json using gson. To be used for local storage.
     * @param writer Writer
     * @param value Object to convert
     * @throws IOException Failure in conversion process
     */
    @Override
    public void write(JsonWriter writer, ChatMessage value) throws IOException {
        value.serialize(writer);
    }

    /**
     * Convert json into chat message using gson. To be used for local storage.
     * @param in Reader
     * @return New Json object
     * @throws IOException Failure occurred while reading/processing the Json
     */
    @Override
    public ChatMessage read(JsonReader in) throws IOException {
        String sender = null;
        String receiver = null;
        UUID id = null;
        String content = null;
        boolean isSent = true;
        Date moment = null;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("sender")) {
                sender = in.nextString();
            } else if (name.equals("receiver")) {
                receiver = in.nextString();
            } else if (name.equals("id")) {
                id = UUID.fromString(in.nextString());
            } else if (name.equals("content")) {
                content = in.nextString();
            } else if (name.equals("isSent")) {
                isSent = in.nextBoolean();
            } else if (name.equals("moment")) {
                try {
                    moment = DATE_FORMAT.parse(in.nextString());
                } catch (ParseException e) {
                    throw new IOException("Failed to load date.");
                }
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        if (sender != null && receiver != null && id != null && content != null && moment != null) {
            return new ChatMessage(id, sender, receiver, content, moment, isSent);
        } else {
            throw new IOException("Invalid ChatMessage Json detected");
        }
    }

    /**
     * Convert a JSONObject into a chat message. To be used for server communication.
     * @param jsonObject Object to convert
     * @return Chat
     * @throws JSONException Failed to parse the date
     */
    public static ChatMessage read(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("sender") && jsonObject.has("receiver") && jsonObject.has("id")
        && jsonObject.has("content") && jsonObject.has("moment")) {
            try {
                return new ChatMessage(UUID.fromString(jsonObject.getString("id")),
                        jsonObject.getString("sender"),
                        jsonObject.getString("receiver"),
                        jsonObject.getString("content"),
                        DATE_FORMAT.parse(jsonObject.getString("moment")),
                        true);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
