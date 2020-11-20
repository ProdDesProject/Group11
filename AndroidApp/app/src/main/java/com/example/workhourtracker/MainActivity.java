package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open LoginView
        Intent loginIntent = new Intent(this, LoginView.class);
        startActivity(loginIntent);

        //if user is already logged in,
        //go directly to the main menu view
    }
}