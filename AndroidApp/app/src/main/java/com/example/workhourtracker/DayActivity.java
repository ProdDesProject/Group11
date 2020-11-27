package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DayActivity extends AppCompatActivity {

    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        selectedDate = "DD.MM.YYYY";
        Intent myIntent = getIntent();
        selectedDate = myIntent.getExtras().getString("choosedDay");

    }
}