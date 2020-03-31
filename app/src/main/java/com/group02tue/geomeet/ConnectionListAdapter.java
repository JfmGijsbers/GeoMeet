package com.group02tue.geomeet;

import android.app.Activity;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.group02tue.geomeet.backend.social.ExternalUserProfile;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ConnectionListAdapter extends ArrayAdapter<String>
        implements CheckBox.OnCheckedChangeListener {
    private final Activity context;
    private final List<String> users;
    private final Map<Integer, Boolean> mCheckStates;

    public ConnectionListAdapter(Activity context, List<String> users) {
        super(context, R.layout.activity_connectionlist_item);
        this.context = context;
        this.users = users;
        mCheckStates = new ArrayMap<>();
        for (int i = 0; i < users.size(); i++) {
            mCheckStates.put(i, false);     // All false -> TODO: ask for bool input and use this
        }
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_connectionlist_item, null,
                true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.profileName);
        CheckBox chkUser = (CheckBox)rowView.findViewById(R.id.listItemCheckbox);
        chkUser.setOnCheckedChangeListener(this);
        chkUser.setTag(position);
        if (mCheckStates.containsKey(position)) {
            chkUser.setChecked(mCheckStates.get(position));
        }
        synchronized (users) {
            if (position < users.size()) {
                txtTitle.setText(users.get(position));
            }
        }
        return rowView;
    }

    public boolean isChecked(int position) {
        synchronized (mCheckStates) {
            if (mCheckStates.containsKey(position)) {
                return mCheckStates.get(position);
            } else {
                return false;
            }
        }
    }

    public void setChecked(int position, boolean state) {
        synchronized (mCheckStates) {
            mCheckStates.put(position, state);
            notifyDataSetChanged();
        }
    }

    public boolean[] getCheckedArray() {
        synchronized (mCheckStates) {
            int maxKey;
            if (mCheckStates.size() > 0) {
                maxKey = Collections.max(mCheckStates.keySet()) + 1;
            } else {
                return new boolean[0];
            }


            boolean[] checkedArray = new boolean[maxKey];
            for (int key : mCheckStates.keySet()) {
                checkedArray[key] = mCheckStates.get(key);
            }
            return checkedArray;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        synchronized (mCheckStates) {
            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
        }
    }
}