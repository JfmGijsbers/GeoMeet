package com.group02tue.geomeet;

import android.app.Activity;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MembersListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> members;

    public MembersListAdapter(Activity context, List<String> members) {
        super(context, R.layout.activity_memberlist_item);
        this.context = context;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_memberlist_item, null,
                true);
        TextView txtName = (TextView) rowView.findViewById(R.id.member_name);
        synchronized (members) {
            if (position < members.size()) {
                txtName.setText(members.get(position));
            }
        }
        return rowView;
    }
}
