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

public class NotificationsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey);
        Log.e("preference", String.valueOf(getValue()));
        showCustomSettings(getValue());
    }



    private void showCustomSettings(boolean bool) {
        Preference newmeeting = findPreference("notification_newmeeting");
        Preference newmessage = findPreference("notification_newmessage");
        Preference newconnection = findPreference("notification_newconnection");

        PreferenceCategory specific = findPreference("category_specific");

        specific.setVisible(bool);

        newmeeting.setVisible(bool);
        newmessage.setVisible(bool);
        newconnection.setVisible(bool);
    }
    private boolean getValue() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean notifications = sp.getBoolean(
                "all_notifications", true);
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
        Log.e("Changed", "True");
        showCustomSettings(getValue());
    }
}
