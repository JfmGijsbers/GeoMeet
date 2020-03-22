package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.chat.ChatEventListener;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity implements ChatEventListener {
    private RecyclerView messageRecycler;
    private MessageListAdapter messageAdapter;
    private List<ChatMessage> messages;
    private TextView chatBox;

    private ChatManager chatManager;
    private String meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        meetingId = getIntent().getStringExtra("meetingId");
        chatBox = findViewById(R.id.edittext_chatbox);
        chatManager = ((MainApplication)getApplication()).getChatManager();

        messages = chatManager.getMessages(meetingId);
        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);
        messageAdapter = new MessageListAdapter(messages,
                ((MainApplication)getApplication()).getAuthenticationManager());
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatManager.addListener(this);
        chatManager.checkForNewMessages();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatManager.removeListener(this);
    }

    @Override
    public void onNewMessageReceived(ChatMessage message) {
        messages.add(message);
    }

    @Override
    public void onMessageSent(ChatMessage message) {
        messages.add(message);
    }

    @Override
    public void onFailedToSendMessage(ChatMessage message, String reason) {
        Toast.makeText(this, "Failed to send message: " + reason,
                Toast.LENGTH_LONG).show();
    }

    public void onNewMessage(View view) {
        String message = String.valueOf(chatBox.getText());
        if (!message.isEmpty()) {
            chatManager.sendMessage(meetingId, message);
        }
        chatBox.setText("");
    }
}
