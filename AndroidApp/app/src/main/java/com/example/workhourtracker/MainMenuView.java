package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_view);
    }

    //android:background="#EFEFEF"

    //If you press back button, application minimize.
    //Whitout this function, pressing the back button will take you to the login view
    //- without logging out
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public void browseButtonClicked(View view) {
        //go to the calendar view
        Intent browseCalendarIntent = new Intent(this, CalendarViewActivity.class);
        startActivity(browseCalendarIntent);
    }

    public void addHoursButtonClicked(View view) {
        //go to the addWorkHours view
    }

    public void contactButtonClicked(View view) {
        //go to the contact view
        Intent contactInfoIntent = new Intent(this, ContactInfoView.class);
        startActivity(contactInfoIntent);
    }

    public void logoutButtonClicked(View view) {
        //Logout functionality

        //close activity and go back to MainActivity/loginActivity after logout
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}