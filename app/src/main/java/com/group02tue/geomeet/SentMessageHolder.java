package com.group02tue.geomeet;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SentMessageHolder extends RecyclerView.ViewHolder {
    private TextView messageText, timeText;

    SentMessageHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
    }

    void bind(String message) {
        messageText.setText(message);
    }
}
