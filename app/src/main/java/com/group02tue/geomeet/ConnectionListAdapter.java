package com.group02tue.geomeet;

import android.app.Activity;
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

import java.util.List;


public class ConnectionListAdapter extends ArrayAdapter<String>
        implements CheckBox.OnCheckedChangeListener {
    private final Activity context;
    private final List<String> users;
    private final SparseBooleanArray mCheckStates;

    public ConnectionListAdapter(Activity context, List<String> users) {
        super(context, R.layout.activity_connectionlist_item);
        this.context = context;
        this.users = users;
        mCheckStates = new SparseBooleanArray(users.size());
    }


    @Override
    public void add(String object) {
        users.add(object);
        super.add(object);
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
        if (position < users.size()) {
            txtTitle.setText(users.get(position));
        }
        return rowView;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }
}