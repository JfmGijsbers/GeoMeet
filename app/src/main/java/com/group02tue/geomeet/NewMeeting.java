package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class NewMeeting extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                NewMeeting.this, 2020, 03, 03);
        datePickerDialog.show();
    }

}
