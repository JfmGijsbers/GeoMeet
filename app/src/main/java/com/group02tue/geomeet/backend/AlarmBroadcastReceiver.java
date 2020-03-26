package com.group02tue.geomeet.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.group02tue.geomeet.MainApplication;
import com.group02tue.geomeet.PushNotificationManager;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.chat.ChatEventListener;
import com.group02tue.geomeet.backend.chat.ChatManager;
import com.group02tue.geomeet.backend.chat.ChatMessage;
import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;
import com.group02tue.geomeet.backend.social.MeetingManager;
import com.group02tue.geomeet.backend.social.MeetingSyncEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final long ALARM_INTERVAL = 10000;    // Interval to execute the alarm, in ms
    private static final int START_BROADCAST_REQUEST_CODE = 0;   // Random ID for intent

    /**
     * Called when the alarm expires.
     * @param context Context
     * @param intent Broadcast intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.println(Log.DEBUG, "BackendAlarm", "Received alarm broadcast");
        AuthenticationManager authenticationManager =
                ((MainApplication)context.getApplicationContext()).getAuthenticationManager();
        if (authenticationManager.areCredentialsStored()) {
            synchronize(context);
        }
    }

    /**
     * Synchronizes data with the server.
     * @param context
     */
    private void synchronize(final Context context) {
        final PushNotificationManager pushNotificationManager =
                ((MainApplication)context.getApplicationContext()).getPushNotificationManager();
        final MeetingManager.MeetingSyncManager meetingSyncManager =
                ((MainApplication)context.getApplicationContext()).getMeetingSyncManager();
        final ChatManager chatManager =
                ((MainApplication)context.getApplicationContext()).getChatManager();

        // Sync chat
        chatManager.addListener(new ChatEventListener() {
            @Override
            public void onNewMessageReceived(ChatMessage message) {
                pushNotificationManager.displayNewMessageNotification(context, message,
                        ((MainApplication)context.getApplicationContext()).getMeetingManager());
                chatManager.removeListener(this);
            }

            @Override
            public void onMessageSent(ChatMessage message) {
                chatManager.removeListener(this);
            }

            @Override
            public void onFailedToSendMessage(ChatMessage message, String reason) {
                chatManager.removeListener(this);
            }
        });
        chatManager.checkForNewMessages();
        chatManager.sendAllUnsendMessages();

        // Sync meetings
        meetingSyncManager.addListener(new MeetingSyncEventListener() {
            @Override
            public void onMeetingUpdatedReceived(Meeting meeting) {
                meetingSyncManager.removeListener(this);
            }

            @Override
            public void onLeftMeeting(UUID id) {
                meetingSyncManager.removeListener(this);
            }

            @Override
            public void onFailure(UUID id, String reason) {
                meetingSyncManager.removeListener(this);
            }

            @Override
            public void onReceivedNewMeetingInvitations(ArrayList<ImmutableMeeting> meetings) {
                for (ImmutableMeeting invite : meetings) {
                    pushNotificationManager.displayNewMeetingInviteNotification(context, invite);
                }
                meetingSyncManager.removeListener(this);
            }
        });
        meetingSyncManager.requestMeetingInvitations();
    }

    /**
     * Starts the alarm. Uses the default alarm interval.
     * @param context Context
     */
    public static void start(Context context) {
        start(context, ALARM_INTERVAL);
    }

    /**
     * Starts the alarm.
     * @param context Context
     * @param interval Alarm interval to use (in ms)
     */
    public static void start(Context context, long interval) {
        Log.println(Log.DEBUG, "BackendAlarm", "Starting alarm");
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), START_BROADCAST_REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval, interval, pendingIntent);
    }

    /**
     * Stops the alarm.
     * @param context Context
     */
    public static void stop(Context context) {
        Log.println(Log.DEBUG, "BackendAlarm", "Stopping alarm");
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), START_BROADCAST_REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}