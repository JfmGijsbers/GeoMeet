package com.group02tue.geomeet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MeetinglistAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public MeetinglistAdapter(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.activity_meetinglist_item, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position,View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_meetinglist_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        txtTitle.setText(web[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}