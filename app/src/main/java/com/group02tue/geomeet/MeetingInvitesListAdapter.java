package com.group02tue.geomeet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.group02tue.geomeet.backend.social.ImmutableMeeting;
import com.group02tue.geomeet.backend.social.Meeting;

import java.util.List;

public class MeetingInvitesListAdapter extends ArrayAdapter<ImmutableMeeting> {
    private final Activity context;
    private final List<ImmutableMeeting> meetingInvites;

    public MeetingInvitesListAdapter(Activity context, List<ImmutableMeeting> meetingInvites) {
        super(context, R.layout.activity_invite_list_item, meetingInvites);
        this.context = context;
        this.meetingInvites = meetingInvites;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_invite_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.invite_title);
        TextView txtDate = rowView.findViewById(R.id.invite_date);
        Button btnAcceptInvite = rowView.findViewById(R.id.btn_accept_invite);
        btnAcceptInvite.setTag(position);
        btnAcceptInvite.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAcceptOrReject(v, true);
            }
        });

        Button btnRejectInvite = rowView.findViewById(R.id.btn_reject_invite);
        btnRejectInvite.setTag(position);
        btnRejectInvite.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAcceptOrReject(v, false);
            }
        });

        if (position < meetingInvites.size()) {
            txtTitle.setText(meetingInvites.get(position).name);
            txtDate.setText(MainApplication.UI_DATE_FORMAT.format(meetingInvites.get(position).moment));
        }
        return rowView;
    }

    private void onClickAcceptOrReject(View v, boolean accept) {
        int pos = (Integer) v.getTag();
        synchronized (meetingInvites) {
            if (pos < meetingInvites.size()) {
                ImmutableMeeting meetingInvite = meetingInvites.get(pos);
                ((MainApplication) context.getApplication()).getMeetingManager().decideInvitation(
                        meetingInvite.id, accept,
                        ((MainApplication) context.getApplication()).getMeetingSyncManager());
                meetingInvites.remove(pos);
                //notifyDataSetChanged();
                Intent dashboardIntent = new Intent(context, Dashboard.class);
                context.startActivity(dashboardIntent);
            }
        }
    }
}
