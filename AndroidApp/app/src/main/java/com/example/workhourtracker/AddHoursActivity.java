package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import java.util.Objects;


public class AddHoursActivity extends AppCompatActivity {

    private TextView editStartingDay;
    private TextView editEndingDate;
    private TextView editStartingTime;
    private TextView editEndingTime;
    private EditText editAdditionalInfo;

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "refreshToken";

    private Button myButton;
    private String startingDay;
    private String endingDay;
    private String startingTime;
    private String endingTime;
    private String additionalInfo;
    private String startingTimeStamp;
    private String endingTimeStamp;

    private SharedPreferences _preferences;

    private String userid;
    private String token;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours);
        editStartingDay = findViewById(R.id.startingDayEdit);
        editEndingDate = findViewById(R.id.endingDateEdit);
        editStartingTime = findViewById(R.id.startingEdit);
        editEndingTime = findViewById(R.id.endingEdit);
        editAdditionalInfo = findViewById(R.id.additionalInfoEdit);
        myButton = findViewById(R.id.sendHoursButton);

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

        endingTime = "12:00:00";
        startingTime = "12:00:00";

        Intent addHoursIntent = getIntent();
        token = addHoursIntent.getStringExtra("token");
        userid = addHoursIntent.getStringExtra("userID");
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
            startingDay = year + "-" + checkNumber(month) + "-" + checkNumber(dayOfMonth);
            endingDay = startingDay;
            editStartingDay.setText(startingDay);
            editEndingDate.setText(endingDay);
        };
    }



    private void openTimeDialog(){
        editStartingTime.setOnClickListener(v -> {
            mTimeSetListener = (timePicker, hour, minute) -> {

                startingTime = checkNumber(hour) + ":" + checkNumber(minute) + ":00";
                editStartingTime.setText(startingTime);
            };


            int hour = 12;
            int minute = 0;

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddHoursActivity.this, android.R.style.Theme_Holo_Light_Dialog, mTimeSetListener, hour, minute, true);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });
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
            endingDay = year + "-" + checkNumber(month) + "-" + checkNumber(dayOfMonth);
            editEndingDate.setText(endingDay);
        };
    }




    private void openTimeDialog2(){
        editEndingTime.setOnClickListener(v -> {
            mTimeSetListener = (view, hourOfDay, minute) -> {


                endingTime = checkNumber(hourOfDay) + ":" + checkNumber(minute) + ":00";

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

        startingDay = year + "-" + checkNumber(month) + "-" + checkNumber(day);

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


    public void getTimeStamps(){
        startingTimeStamp = startingDay + "T" + startingTime + "Z";
        endingTimeStamp = endingDay + "T" + endingTime + "Z";
    }

    public void sendHours() {
        setAdditionalInfo();

        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userid", userid);
            jsonBody.put("description", additionalInfo);
            jsonBody.put("startTime", startingTimeStamp);
            jsonBody.put("endTime", endingTimeStamp);


            final String mRequestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final String url = "https://workh.herokuapp.com/workhours";

            final StringRequest groupRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(this, "New post added", Toast. LENGTH_SHORT). show();
                        Log.d("RESPONSE", response);
                        endActivity();



                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 401) {
                        refreshRequest();
                        sendHours();
                    } else {
                        // irrecoverable errors. show error to user.
                    }
                }
            })  {
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


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String newToken = _preferences.getString("Token","");
                    token = newToken;
                    params.put("Authorization", "Bearer "+ token);
                    return params;
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


    }

    public void buttonOnClick(View view) {
        getTimeStamps();
        sendHours();

    }

    public void refreshRequest(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String url = "https://workh.herokuapp.com/refresh_token";
        Log.d("RESPONSE1", "this is a test2");

        final StringRequest groupRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    //Toast.makeText(this, "Stuff happened", Toast. LENGTH_SHORT). show();
                    Log.d("cookie11", response);

                    try {
                        //Parse response JSONdata
                        JSONObject obj = new JSONObject(response);
                        token = obj.getString("token");
                        SharedPreferences.Editor prefEditor = _preferences.edit();
                        prefEditor.putString("Token", token);
                        prefEditor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("ERROR", error.toString())) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                Log.i("cookies",rawCookies);

                if (response.headers.containsKey(SET_COOKIE_KEY)
                        && response.headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
                    String cookie = response.headers.get(SET_COOKIE_KEY);
                    if (cookie.length() > 0) {
                        String[] splitCookie = cookie.split(";");
                        String[] splitSessionId = splitCookie[0].split("=");
                        cookie = splitSessionId[1];
                        SharedPreferences.Editor prefEditor = _preferences.edit();
                        prefEditor.putString(SESSION_COOKIE, cookie);
                        //Log.d("cookie", cookie);
                        prefEditor.commit();
                    }
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ token);
                String refreshCookie = _preferences.getString(SESSION_COOKIE,"");
                Log.d("cookie1", "On refreshReuest: " + refreshCookie);
                params.put("Cookie", "refreshToken="+ refreshCookie);

                return params;
            }
        };
        requestQueue.add(groupRequest);
    }
}

