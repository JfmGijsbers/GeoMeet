package com.group02tue.geomeet.backend.chat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChatMessageAdapter extends TypeAdapter<ChatMessage> {
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Convert object into Json.
     * @param writer Writer
     * @param value Object to convert
     * @throws IOException Failure in conversion process
     */
    @Override
    public void write(JsonWriter writer, ChatMessage value) throws IOException {
        value.serialize(writer);
    }

    /**
     * Convert Json into object.
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
}
