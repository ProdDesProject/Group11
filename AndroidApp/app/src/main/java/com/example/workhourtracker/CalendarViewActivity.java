package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarViewActivity extends AppCompatActivity {


    CalendarView calendarView;
    int ADD_NEW_PART_INTENT_ID = 2345;

    String dayString;
    String monthString;
    String yearString;

    String selectedDate;
    String id = "a12s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

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
        //TextView textView = findViewById(R.id.textView1);
        //textView.setText(selectedDate);


        //move to the next view with selected date
        //example code from another project
        //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        //Intent dayActivityIntent = new Intent(this, DayActivity.class);
        //dayActivityIntent.putExtra("choosedDay", selectedDate);

        Intent dayActivityIntent = new Intent(this, DayActivity.class);
        dayActivityIntent.putExtra("Date", selectedDate);
        dayActivityIntent.putExtra("userId", id);

        startActivityForResult(dayActivityIntent, ADD_NEW_PART_INTENT_ID);

    }
}