package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class DayActivity extends AppCompatActivity {

    private TextView day;
    String[] activity_list = {"aa"};
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Intent intent = getIntent();

        String date = intent.getStringExtra("Date");
        String id = intent.getStringExtra("userId");

        day = findViewById(R.id.calendarDate);
        day.setText(date);


        listView = (ListView) findViewById(R.id.list_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.added_hours,R.id.hours,activity_list);
        listView.setAdapter(adapter);





    }

}