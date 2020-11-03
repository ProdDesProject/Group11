package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpView extends AppCompatActivity {

    private String emailInput;
    private String usernameInput;
    private String passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
    }


    public void createAccountButtonClicked(View view) {
        EditText editTextUsername = findViewById(R.id.usernameEditText);
        EditText editTextPassword = findViewById(R.id.passwordEditText);
        EditText editTextEmail = findViewById(R.id.emailEditText);
        usernameInput = editTextUsername.getText().toString();
        passwordInput = editTextPassword.getText().toString();
        emailInput = editTextEmail.getText().toString();

        if (usernameInput.length() == 0 || passwordInput.length() == 0 || emailInput.length() == 0) {
            Toast.makeText(this, "Enter your email, username and password", Toast. LENGTH_SHORT). show();
        }
        else {
            //register functionality
        }
    }
}