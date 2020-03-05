package com.group02tue.geomeet.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import static android.content.Context.ALARM_SERVICE;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final long ALARM_INTERVAL = 10000;    // Interval to execute the alarm, in ms
    private static final int START_BROADCAST_REQUEST_CODE = 45785397;   // Random ID for intent

    /**
     * Called when the alarm expires.
     * @param context Context
     * @param intent Broadcast intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: what to execute when alarm expires
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
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), START_BROADCAST_REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}