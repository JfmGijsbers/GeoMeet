package com.group02tue.geomeet;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    private TextView messageText, timeText, nameText;

    ReceivedMessageHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
        nameText = itemView.findViewById(R.id.text_message_name);
    }

    void bind(String message, Date date, String name) {
        messageText.setText(message);
        nameText.setText(name);
    }
}
