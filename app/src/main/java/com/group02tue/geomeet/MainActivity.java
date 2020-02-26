package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.group02tue.geomeet.backend.authentication.AuthenticationEventListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

public class MainActivity extends AppCompatActivity {
    private AuthenticationManager authenticationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void login(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
    public void toRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
