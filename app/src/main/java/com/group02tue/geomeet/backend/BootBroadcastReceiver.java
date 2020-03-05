package com.group02tue.geomeet.backend;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class BootBroadcastReceiver extends BroadcastReceiver {
    /**
     * Called when the phone boots and if enabled.
     * @param context Context
     * @param intent Broadcast intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmBroadcastReceiver.start(context);  // Start the alarm
        }
    }

    /**
     * Enables the start of the alarm receiver on boot.
     * @param context Context
     */
    public static void enableStartOnBoot(Context context) {
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Disables the start of the alarm receiver on boot.
     * @param context Context
     */
    public static void disableStartOnBoot(Context context) {
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}