package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AddHoursActivity extends AppCompatActivity {

    private TextView editStartingDay;
    private TextView editEndingDate;
    private TextView editStartingTime;
    private TextView editEndingTime;
    private EditText editAdditionalInfo;
    private String idPerson;
    private Button myButton;
    private String startingDay;
    private String endingDay;
    private String startingTime;
    private String endingTime;
    private String additionalInfo;
    private Date dateMin;
    private Date datePlus;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours);

        Intent intent = getIntent();

        userID = intent.getStringExtra("userID");

        editStartingDay = findViewById(R.id.startingDayEdit);
        editEndingDate = findViewById(R.id.endingDateEdit);
        editStartingTime = findViewById(R.id.startingEdit);
        editEndingTime = findViewById(R.id.endingEdit);
        editAdditionalInfo = findViewById(R.id.additionalInfoEdit);
        myButton = findViewById(R.id.sendHoursButton);

        setCurDate();
        openTimeDialog();
        openTimeDialog2();
        openDateDialog();
        openDateDialog2();
    }
    private void openDateDialog(){
        editStartingDay.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AddHoursActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            startingDay = dayOfMonth + "." + month + "." + year;
            endingDay = startingDay;
            editStartingDay.setText(startingDay);
            editEndingDate.setText(endingDay);
        };
    }

    private void openDateDialog2(){
        editEndingDate.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int vuosi = cal.get(Calendar.YEAR);
            int kuukausi = cal.get(Calendar.MONTH);
            int paiva = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AddHoursActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener2, vuosi, kuukausi, paiva);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener2 = (view, year, month, dayOfMonth) -> {
            month += 1;
            endingDay = dayOfMonth + "." + month + "." + year;
            editEndingDate.setText(endingDay);
        };
    }

    private void openTimeDialog(){
        editStartingTime.setOnClickListener(v -> {
            mTimeSetListener = (timePicker, hour, minute) -> {

                startingTime = checkNumber(hour) + ":" + checkNumber(minute);
                editStartingTime.setText(startingTime);
            };


            int hour = 12;
            int minute = 0;

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddHoursActivity.this, android.R.style.Theme_Holo_Light_Dialog, mTimeSetListener, hour, minute, true);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });
    }

    private void openTimeDialog2(){
        editEndingTime.setOnClickListener(v -> {
            mTimeSetListener = (view, hourOfDay, minute) -> {

                endingTime = checkNumber(hourOfDay) + ":" + checkNumber(minute);
                editEndingTime.setText(endingTime);
            };

            int hour = 12;
            int minute = 0;
            boolean is24hour = true;

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddHoursActivity.this, android.R.style.Theme_Holo_Light_Dialog, mTimeSetListener, hour, minute, is24hour);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });
    }
    public String checkNumber(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void setCurDate(){
        Calendar now = Calendar.getInstance();
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);

        month += 1;
        startingDay = day + "." + month + "." + year;
        endingDay = startingDay;

        editStartingDay.setText(startingDay);
        editEndingDate.setText(endingDay);
    }

    private void endActivity(){
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void setAdditionalInfo(){
        additionalInfo = editAdditionalInfo.getText().toString();
    }

    public void sendHours(View view) {
        setAdditionalInfo();
        String strtDay = startingDay;
        String endD = endingDay;
        String strtTm = startingTime;
        String endTm = endingTime;
        String addInfo = additionalInfo;

        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idPerson", idPerson);
            jsonBody.put("StartDate", strtDay);
            jsonBody.put("EndDate", endD);
            jsonBody.put("StartTime", strtTm);
            jsonBody.put("EndTime", endTm);
            jsonBody.put("AdditionalInfo", addInfo);

            final String mRequestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final String url = null;

            final StringRequest groupRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        endActivity();

                        Log.d("RESPONSE", response);
                    }, error -> Log.e("ERROR", error.toString())) {
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            requestQueue.add(groupRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // do sending

    }
}