package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.group02tue.geomeet.backend.authentication.AuthenticationEventListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;

public class MainActivity extends AppCompatActivity implements AuthenticationEventListener {
    private AuthenticationManager authenticationManager;
    private EditText email;
    private EditText password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();

        if (authenticationManager.areCredentialsStored()) {
            authenticationManager.login();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        authenticationManager.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        authenticationManager.removeListener(this);
    }

    /**
     * Login button call
     */
    public void login(View view) {
        authenticationManager.login(String.valueOf(email.getText()), String.valueOf(password.getText()));
        btnLogin.setEnabled(false);
    }

    /**
     * Register button call
     */
    public void toRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @Override
    public void onLoggedIn() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistered() {
        // N/A
    }

    @Override
    public void onAuthenticationFailure(String reason) {
        Toast.makeText(this, "Login failed: " + reason, Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }
}
