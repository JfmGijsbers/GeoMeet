package com.group02tue.geomeet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.group02tue.geomeet.backend.AlarmBroadcastReceiver;
import com.group02tue.geomeet.backend.BootBroadcastReceiver;

public class NotificationsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public final static String SHOW_ANY_NOTIFICATION_KEY = "all_notifications";
    public final static String NOTIFICATION_NEW_MEETING_KEY = "notification_newmeeting";
    public final static String NOTIFICATION_NEW_MESSAGE_KEY = "notification_newmessage";
    public final static String NOTIFICATION_NEW_CONNECTION_KEY = "notification_newconnection";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey);
        Log.e("preference", String.valueOf(getValue()));
        showCustomSettings(getValue());
    }

    private void showCustomSettings(boolean bool) {
        Preference newmeeting = findPreference(NOTIFICATION_NEW_MEETING_KEY);
        Preference newmessage = findPreference(NOTIFICATION_NEW_MESSAGE_KEY);
        Preference newconnection = findPreference(NOTIFICATION_NEW_CONNECTION_KEY);

        PreferenceCategory specific = findPreference("category_specific");

        specific.setVisible(bool);

        newmeeting.setVisible(bool);
        newmessage.setVisible(bool);
        newconnection.setVisible(bool);
    }

    private boolean getValue() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean notifications = sp.getBoolean(
                SHOW_ANY_NOTIFICATION_KEY, true);
        return notifications;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        showCustomSettings(getValue());
        if (SHOW_ANY_NOTIFICATION_KEY.equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) {
                AlarmBroadcastReceiver.start(getContext());
                BootBroadcastReceiver.enableStartOnBoot(getContext());
            } else {
                BootBroadcastReceiver.disableStartOnBoot(getContext());
                AlarmBroadcastReceiver.stop(getContext());
            }

        }
    }
}
