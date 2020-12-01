package com.example.workhourtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuView extends AppCompatActivity {

    private String token;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_view);

        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        userID = intent.getStringExtra("userID");
    }


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
        Intent addHoursIntent = new Intent(this, AddHoursActivity.class);
        startActivity(addHoursIntent);
    }

    public void contactButtonClicked(View view) {
        //go to the contact view
        Intent contactInfoIntent = new Intent(this, ContactInfoView.class);
        startActivity(contactInfoIntent);
    }

    public void logoutButtonClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Log out");
        builder.setMessage("Are you sure you want to log out?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                logoutFunctionality();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void logoutFunctionality(){
        //Logout functionality
        token = "";
        userID = "";
        //close activity and go back to MainActivity/loginActivity after logout
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}