package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private MessageListAdapter messageAdapter;
    // TODO: actually import a messageList
    List<BaseMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageList = new ArrayList<BaseMessage>();
        BaseMessage message = new BaseMessage("Hoi hoi", new User("Jeroen"), 200);
        messageList.add(message);
        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);
        messageAdapter = new MessageListAdapter(this, messageList);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
