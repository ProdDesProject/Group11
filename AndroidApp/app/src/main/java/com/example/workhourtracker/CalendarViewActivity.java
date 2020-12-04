package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarViewActivity extends AppCompatActivity {


    private CalendarView calendarView;
    private String token;
    private String userID;
    private int ADD_NEW_PART_INTENT_ID = 2345;

    private String dayString;
    private String monthString;
    private String yearString;


    private String selectedDate;

    String selectedDate;
    String id = "a12s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        userID = intent.getStringExtra("userID");

        calendarView = findViewById(R.id.calendarViewId);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month = month + 1;//koska tammikuu = 0, helmikuu = 1 jne.

            //valittu päivämäärä verrattavaan muotoon muiden päivämäärien kanssa
            if(dayOfMonth < 10) {
                dayString = Integer.toString(dayOfMonth);
                dayString = "0" + dayString;
            }

            if (dayOfMonth > 9) {
                dayString = Integer.toString(dayOfMonth);
            }
            if (month < 10) {
                monthString = Integer.toString(month);
                monthString = "0" + monthString;
            }

            if (month > 9) {
                monthString = Integer.toString(month);
            }
            yearString = Integer.toString(year);

            //curDate = String.valueOf(dayOfMonth + "/" + month + "/" + year); //klikattavan päivän päivämäärä
            selectedDate = dayString + "." + monthString + "." + yearString;
            goToIntent();
        });
    }

    private void goToIntent()
    {
        Intent dayActivityIntent = new Intent(this, DayActivity.class);
        dayActivityIntent.putExtra("Date", selectedDate);
        dayActivityIntent.putExtra("userId", id);

        startActivityForResult(dayActivityIntent, ADD_NEW_PART_INTENT_ID);

    }
}