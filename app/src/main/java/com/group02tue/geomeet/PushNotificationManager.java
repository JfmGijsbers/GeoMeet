package com.group02tue.geomeet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.group02tue.geomeet.backend.chat.ChatMessage;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;


public class PushNotificationManager {
    private static final String CHANNEL_ID = "pushNotifications";
    private static final int NEW_MESSAGE_NOTIFICATION_ID = 0;
    private static final int NEW_INVITE_NOTIFICATION_ID = 1;

    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Push notifications";
            String description = "Updates about incoming messages and meeting invites";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Displays a notification for when a new message has been received by the user.
     * @param context Context
     * @param chatMessage Message received
     * @param meetingManager Meeting manager
     */
    public void displayNewMessageNotification(Context context, ChatMessage chatMessage,
                                              MeetingManager meetingManager) {
        Log.println(Log.DEBUG, "BackendNotifications", "Showing notifications for new message with id: " + chatMessage.getId().toString());
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New message from " + chatMessage.getSender())
                .setContentText(chatMessage.getContent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        try {
            // Request meeting and create an intent to open the activity with all information of the meeting
            Meeting meeting = meetingManager.getLocalMeeting(UUID.fromString(chatMessage.getReceiver()));
            Intent intent = new Intent(context, SeeMeeting.class);
            MeetingsOverview.putMeetingInIntent(intent, meeting);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
        } catch (NoSuchElementException e) {
            // No meeting: don't let the user open the activity
        }

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NEW_MESSAGE_NOTIFICATION_ID, builder.build());
    }

    /**
     * Displays a notification for when the user has been invited to a new meeting.
     * @param context Context
     * @param meeting Meeting to which the user has been invited
     */
    public void displayNewMeetingInviteNotification(Context context, ImmutableMeeting meeting) {
        Log.println(Log.DEBUG, "BackendNotifications", "Showing notifications for new meeting invite with id: " + meeting.id.toString());
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Meeting invite received")
                .setContentText("You have been invited for " + meeting.name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent intent = new Intent(context, MeetingsOverview.class);        // TODO: should maybe be a different activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NEW_INVITE_NOTIFICATION_ID, builder.build());
    }
}
