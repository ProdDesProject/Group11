package com.example.workhourtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginView extends AppCompatActivity {

    private String usernameInput;
    private String passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        signUpInstanceButton();
    }

    public void loginButtonClicked(View view) {
        EditText editTextUsername = findViewById(R.id.usernameEditText);
        EditText editTextPassword = findViewById(R.id.passwordEditText);
        usernameInput = editTextUsername.getText().toString();
        passwordInput = editTextPassword.getText().toString();

        if (usernameInput.length()==0 || passwordInput.length()==0) {
            Toast.makeText(this, "Enter your username and password", Toast. LENGTH_SHORT). show();
        } else {
            //login functionality
        }
    }

    public void signUpInstanceButton(){
        TextView textViewNewAccount = findViewById(R.id.newAccountTextView);
        String textViewText = "Don't have account? create a new account";

        SpannableString ss = new SpannableString(textViewText);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                goToSignUpView();
            }
        };

        ss.setSpan(clickableSpan1, 20, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewNewAccount.setText(ss);
        textViewNewAccount.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void goToSignUpView(){
        Intent signUpIntent = new Intent(this, SignUpView.class);
        startActivity(signUpIntent);
    }
}