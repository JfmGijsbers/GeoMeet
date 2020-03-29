package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.group02tue.geomeet.backend.chat.ChatEventListener;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;

import java.util.List;
public class MessageListActivity extends AppCompatActivity implements ChatEventListener {
    private RecyclerView messageRecycler;
    private MessageListAdapter messageAdapter;
    private List<ChatMessage> messages;
    private TextView chatBox;

    private ChatManager chatManager;
    private String meetingId;

    private Handler syncChatTimer = new Handler();      // Timer, implemented as handler
    private final static int SYNC_CHAT_TIMER_INTERVAL = 5000;  // Run timer code every ... ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        meetingId = getIntent().getStringExtra("meetingId");
        chatBox = findViewById(R.id.edittext_chatbox);
        chatManager = ((MainApplication)getApplication()).getChatManager();

        // Fill list
        messages = chatManager.getMessages(meetingId);
        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);
        messageAdapter = new MessageListAdapter(messages,
                ((MainApplication)getApplication()).getAuthenticationManager());
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        fixChat();

        messageRecycler.addOnLayoutChangeListener( new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int leftWas, int topWas, int rightWas, int bottomWas) {
                fixChat();
            }
        });
    }

    private void fixChat() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.notifyDataSetChanged();
                messageRecycler.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        chatManager.addListener(this);
        chatManager.checkForNewMessages();
        syncChatTimer.postDelayed(runnableCode, SYNC_CHAT_TIMER_INTERVAL);   // Start timer
    }

    @Override
    protected void onStop() {
        super.onStop();
        syncChatTimer.removeCallbacksAndMessages(null); // 'Stop' the timer
        chatManager.removeListener(this);
    }

    /**
     * Timer elapsed callback
     */
    final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            chatManager.sendAllUnsendMessages();
            chatManager.checkForNewMessages();
            // Repeat this the same runnable code block again
            syncChatTimer.postDelayed(runnableCode, SYNC_CHAT_TIMER_INTERVAL);
        }
    };

    @Override
    public void onNewMessageReceived(ChatMessage message) {
        synchronized (messages) {
            Log.println(Log.DEBUG, "Debug", "1");
            messages.add(message);
            fixChat();
        }
    }

    @Override
    public void onMessageSent(ChatMessage message) {
    }

    @Override
    public void onFailedToSendMessage(ChatMessage message, String reason) {
        Toast.makeText(this, "Failed to send message: " + reason,
                Toast.LENGTH_LONG).show();
    }

    public void onNewMessage(View view) {
        String message = String.valueOf(chatBox.getText());
        if (!message.isEmpty()) {
            Log.println(Log.DEBUG, "Debug", "2");
            ChatMessage messageSending = chatManager.sendMessage(meetingId, message);
            synchronized (messages) {
                Log.println(Log.DEBUG, "Debug", "3");
                messages.add(messageSending);
                fixChat();
                Log.println(Log.DEBUG, "Debug", "4");
            }
        }
        chatBox.setText("");
        fixChat();
    }
    
}
