package com.group02tue.geomeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText etEmail, etPass, etPass2, etFirstname, etLastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        etPass2 = findViewById(R.id.et_confirmPassword);
        etFirstname = findViewById(R.id.et_firstname);
        etLastname = findViewById(R.id.et_lastname);
    }
    public void register(View view) {
        boolean noMistake = true;
        String email = String.valueOf(etEmail.getText());
        String pass = String.valueOf(etPass.getText());
        String pass2 = String.valueOf(etPass2.getText());
        String firstname = String.valueOf(etFirstname.getText());
        String lastname = String.valueOf(etLastname.getText());
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
        }
        if (pass2.equals("")) {
            etPass2.setError("Please fill in a password");
            noMistake = false;
        } else if (!pass.equals(pass2)) {
            etPass2.setError("Passwords do not match");
            noMistake = false;
        }
        if (firstname.equals("")) {
            etFirstname.setError("Please fill in your first name");
            noMistake = false;
        }
        if (lastname.equals("")) {
            etLastname.setError("Please fill in your last name");
            noMistake = false;
        }
        if (noMistake) {
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        }
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void toLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
