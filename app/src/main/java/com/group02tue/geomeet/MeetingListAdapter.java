package com.group02tue.geomeet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group02tue.geomeet.backend.social.Meeting;

import java.util.Collection;
import java.util.List;


public class MeetingListAdapter extends ArrayAdapter<Meeting> {
    private final Activity context;
    private final List<Meeting> meetings;

    public MeetingListAdapter(Activity context, List<Meeting> meetings) {
        super(context, R.layout.activity_meetinglist_item, meetings);
        this.context = context;
        this.meetings = meetings;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_meetinglist_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.meetingTitle);
        TextView txtAttending = rowView.findViewById(R.id.meetingAttending);
        TextView txtDate = rowView.findViewById(R.id.meetingDate);
        if (position < meetings.size()) {
            txtTitle.setText(meetings.get(position).getName());
            txtAttending.setText(String.valueOf(meetings.get(position).getAttendingCount()));
            txtDate.setText(MainApplication.UI_DATE_FORMAT.format(meetings.get(position).getMoment()));
        }
        return rowView;
    }
}