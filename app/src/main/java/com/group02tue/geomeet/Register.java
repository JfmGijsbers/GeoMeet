package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.group02tue.geomeet.backend.AlarmBroadcastReceiver;
import com.group02tue.geomeet.backend.authentication.AuthenticationEventListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.InternalUserProfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements AuthenticationEventListener {
    private EditText etEmail, etPass, etPass2, etFirstname, etLastname, etUsername;
    private Button btnRegister;
    private AuthenticationManager authenticationManager;
    private InternalUserProfile.ProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        etPass2 = findViewById(R.id.et_confirmPassword);
        etFirstname = findViewById(R.id.et_firstname);
        etLastname = findViewById(R.id.et_lastname);
        etUsername = findViewById(R.id.et_username);

        btnRegister = findViewById(R.id.btn_register);

        authenticationManager = ((MainApplication)getApplication()).getAuthenticationManager();
        profileManager = ((MainApplication)getApplication()).getProfileManager();
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

    public void register(View view) {
        boolean noMistake = true;
        String email = String.valueOf(etEmail.getText());
        String pass = String.valueOf(etPass.getText());
        String pass2 = String.valueOf(etPass2.getText());
        String firstName = String.valueOf(etFirstname.getText());
        String lastName = String.valueOf(etLastname.getText());
        String username = String.valueOf(etUsername.getText());
        etEmail.setError(null);
        etPass.setError(null);
        etPass2.setError(null);
        etFirstname.setError(null);
        etLastname.setError(null);
        if (email.equals("")) {
            etEmail.setError("Please fill in an email address");
            noMistake = false;
        } else if (!isEmailValid(email)) {
            etEmail.setError("Please fill in a correct email address");
            noMistake = false;
        }
        if (pass.equals("")) {
            etPass.setError("Please fill in a password");
            noMistake = false;
        } else if (!validateInput(pass)) {
            etPass.setError("Please don't use invalid characters (\\, \' etc)");
            noMistake = false;
        }
        if (pass2.equals("")) {
            etPass2.setError("Please fill in a password");
            noMistake = false;
        } else if (!pass.equals(pass2)) {
            etPass2.setError("Passwords do not match");
            noMistake = false;
        } else if (!validateInput(pass2)) {
            etPass2.setError("Please don't use invalid characters (\\, \' etc)");
            noMistake = false;
        }
        if (firstName.equals("")) {
            etFirstname.setError("Please fill in your first name");
            noMistake = false;
        } else if (!validateInput(firstName)) {
            etFirstname.setError("Please don't use invalid characters (\\, \' etc)");
            noMistake = false;
        }
        if (lastName.equals("")) {
            etLastname.setError("Please fill in your last name");
            noMistake = false;
        } else if (!validateInput(lastName)) {
            etLastname.setError("Please don't use invalid characters (\\, \' etc)");
            noMistake = false;
        }
        if (username.equals("")) {
            etUsername.setError("Please fill in your last name");
            noMistake = false;
        }
        else if (!validateUsername(username)) {
            etUsername.setError("Username cannot contain special characters. \n " +
                    "Example correct: test123-t");
            noMistake = false;
        }

        if (noMistake) {
            authenticationManager.register(username, pass, firstName, lastName, email);
            profileManager.update(firstName, lastName, email, "");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            if (sp.getBoolean(
                    NotificationsFragment.SHOW_ANY_NOTIFICATION_KEY, true)) {
                AlarmBroadcastReceiver.start(this);
            }
            etPass.setText("");
            etUsername.setText("");
            etPass2.setText("");
            etEmail.setText("");
            etFirstname.setText("");
            etLastname.setText("");
            btnRegister.setEnabled(false);
        }
    }
    /*
    Input validation functions:
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validateInput(String input) {
        String expression = "^[\\w\\s]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static boolean validateUsername(String username) {
        String expression = "^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
    /*
    Functions for visiting other activities:
     */
    public void toLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoggedIn() {
        // N/A
    }

    @Override
    public void onRegistered() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthenticationFailure(String reason) {
        Toast.makeText(this, "Register failed: " + reason, Toast.LENGTH_LONG).show();
        btnRegister.setEnabled(true);
    }
}
