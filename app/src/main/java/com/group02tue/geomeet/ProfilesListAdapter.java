package com.group02tue.geomeet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group02tue.geomeet.backend.social.ExternalUserProfile;

import java.util.List;


public class ProfilesListAdapter extends ArrayAdapter<ExternalUserProfile> {
    private final Activity context;
    private final List<ExternalUserProfile> profiles;

    public ProfilesListAdapter(Activity context, List<ExternalUserProfile> profiles) {
        super(context, R.layout.activity_memberlist_item);
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_memberlist_item, null,
                true);
        TextView txtName = (TextView) rowView.findViewById(R.id.member_name);
        synchronized (profiles) {
            if (position < profiles.size()) {
                txtName.setText(profiles.get(position).getFirstName() + " " +
                        profiles.get(position).getLastName());
            }
        }
        return rowView;
    }
}
