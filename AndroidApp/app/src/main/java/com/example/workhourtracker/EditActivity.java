package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.SafeIterableMap;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    TextView editStartingDay;
    TextView editEndingDay;
    TextView editStartingTime;
    TextView editEndingTime;
    EditText editAdditionalInfo;
    Button myButton;

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "refreshToken";

    private String hourid;
    private String token;

    private SharedPreferences _preferences;

    private String startingDay;
    private String endingDay;
    private String startingTime;
    private String endingTime;
    private String additionalInfo;

    private String startingTimeStamp;
    private String endingTimeStamp;

    private String splitTimeStamp = "[T, ., Z]";
    private String splitDate = "[-, T, Z]";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        hourid = intent.getStringExtra("hourid");
        token = intent.getStringExtra("token");

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

        startingTime = intent.getStringExtra("startTime");
        endingTime = intent.getStringExtra("endTime");
        additionalInfo = intent.getStringExtra("description");

        editStartingDay = findViewById(R.id.startingDayEdit);
        editEndingDay = findViewById(R.id.endingDateEdit);
        editStartingTime = findViewById(R.id.startingEdit);
        editEndingTime = findViewById(R.id.endingEdit);
        editAdditionalInfo = findViewById(R.id.additionalInfoEdit);
        myButton = findViewById(R.id.sendHoursButton);

        String[] startTimeStampSplitted = startingTime.split(splitTimeStamp);
        startingDay = startTimeStampSplitted[0];
        startingTime = startTimeStampSplitted[1];
        editStartingDay.setText(startingDay);
        editStartingTime.setText(startingTime);

        String[] endTimeStampSplitted = endingTime.split(splitTimeStamp);
        endingDay = endTimeStampSplitted[0];
        endingTime = endTimeStampSplitted[1];
        editEndingDay.setText(endingDay);
        editEndingTime.setText(endingTime);

        editAdditionalInfo.setText(additionalInfo);

        openDateDialog();
        openDateDialog2();
        openTimeDialog();
        openTimeDialog2();



    }


    private void openDateDialog(){
        editStartingDay.setOnClickListener(view -> {

            String[] startDayTimeStamp = startingDay.split(splitDate);
            int year = Integer.parseInt(startDayTimeStamp[0]);
            int month = Integer.parseInt(startDayTimeStamp[1]);
            int day = Integer.parseInt(startDayTimeStamp[2]);


            DatePickerDialog dialog = new DatePickerDialog(EditActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            startingDay = year + "-" + checkNumber(month) + "-" + checkNumber(dayOfMonth);
            endingDay = startingDay;
            editStartingDay.setText(startingDay);
            editEndingDay.setText(endingDay);
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

            TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, android.R.style.Theme_Holo_Light_Dialog, mTimeSetListener, hour, minute, true);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });
    }




    private void openDateDialog2(){
        editEndingDay.setOnClickListener(view -> {

            String[] startDayTimeStamp = endingDay.split(splitDate);
            int vuosi = Integer.parseInt(startDayTimeStamp[0]);
            int kuukausi = Integer.parseInt(startDayTimeStamp[1]);
            int paiva = Integer.parseInt(startDayTimeStamp[2]);

            DatePickerDialog dialog = new DatePickerDialog(EditActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener2, vuosi, kuukausi, paiva);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener2 = (view, year, month, dayOfMonth) -> {
            month += 1;
            endingDay = year + "-" + checkNumber(month) + "-" + checkNumber(dayOfMonth);
            editEndingDay.setText(endingDay);
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

            TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, android.R.style.Theme_Holo_Light_Dialog, mTimeSetListener, hour, minute, is24hour);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });
    }
    public String checkNumber(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void setAdditionalInfo(){
        additionalInfo = editAdditionalInfo.getText().toString();
    }

    public void getTimeStamps(){
        startingTimeStamp = startingDay + "T" + startingTime + "Z";
        endingTimeStamp = endingDay + "T" + endingTime + "Z";
    }

    public void onClick(View view) {
        setAdditionalInfo();
        getTimeStamps();
        editfunction();
    }



    public void editfunction(){

        try {

            JSONObject jsonBody = new JSONObject();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            jsonBody.put("description", additionalInfo);
            jsonBody.put("startTime", startingTimeStamp);
            jsonBody.put("endTime", endingTimeStamp);

            final String mRequestBody = jsonBody.toString();

            final String url = "https://workh.herokuapp.com/workhours/" + hourid;

            final StringRequest groupRequest = new StringRequest(Request.Method.PUT, url,
                    response -> {
                        Toast.makeText(EditActivity.this, "Workhours edited", Toast. LENGTH_SHORT). show();
                        Log.d("RESPONSE", response);

                        onBackPressed();

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 401) {
                        refreshRequest();
                        editfunction();
                    } else {
                        // irrecoverable errors. show error to user.
                    }
                }
            })  {
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    token = _preferences.getString("Token","");
                    params.put("Authorization", "Bearer "+ token);
                    return params;
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


            };
            requestQueue.add(groupRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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