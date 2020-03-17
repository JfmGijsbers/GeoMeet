package com.group02tue.geomeet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group02tue.geomeet.backend.social.ExternalUserProfile;

import java.util.List;


public class ConnectionListAdapter extends ArrayAdapter<ExternalUserProfile> {
    private final Activity context;
    private final List<ExternalUserProfile> users;

    public ConnectionListAdapter(Activity context, List<ExternalUserProfile> users) {
        super(context, R.layout.activity_connectionlist_item);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position,View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_connectionlist_item, null,
                true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.profileName);
        if (position < users.size()) {
            txtTitle.setText(users.get(position).getFirstName() + " " +
                    users.get(position).getLastName());
        }
        return rowView;
    }
}