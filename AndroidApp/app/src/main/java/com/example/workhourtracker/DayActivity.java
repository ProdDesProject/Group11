package com.example.workhourtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "refreshToken";

    private String selectedDate;
    private String startTime;
    private String endTime;

    private String userid;
    private String token;
    private String dayString;
    private int dayInt;
    private String monthString;
    private String yearString;

    private String startDate;
    private String endDate;
    private String splitTimeStamp;


    private SharedPreferences _preferences;

    ArrayList<String> startTimeList;
    ArrayList<String> endTimeList;
    ArrayList<String> descriptionList;


    private String jsonString;
    ArrayList<String> hourIdList;
    JSONObject jsonObject;
    String hourid;
    ListArrayAdapter listAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Intent intent = getIntent();

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

        userid = intent.getStringExtra("userid");
        token = intent.getStringExtra("token");
        yearString = intent.getStringExtra("yearString");
        monthString = intent.getStringExtra("monthString");
        dayString = intent.getStringExtra("dayString");

        try {
            getStartAndEndDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView textView = findViewById(R.id.textView);
        textView.setText("Date: " + dayString + "." + monthString + "." + yearString);

        getWorkHours();

    }


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        getWorkHours();
    }

    public void getStartAndEndDate() throws ParseException {
        startDate = yearString + "-" + monthString + "-" + dayString + "T00:00:00Z";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(startDate));
        c.add(Calendar.DATE, 1);
        endDate = sdf.format(c.getTime());
        endDate = endDate + "T00:00:00Z";

    }

    public void getWorkHours(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String url = "https://workh.herokuapp.com/workhours/" + userid + "?startDate="+ startDate +"&endDate=" + endDate;

        final StringRequest groupRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    Toast.makeText(this, "Workhours retrieved", Toast. LENGTH_SHORT). show();
                    Log.d("RESPONSE", response);
                    jsonString = response;

                    jsonDataToListView();

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("cookie1",String.valueOf(error.networkResponse.statusCode ));
                if (error.networkResponse.statusCode == 401) {
                    refreshRequest();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                } else {
                    // irrecoverable errors. show error to user.
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                token = _preferences.getString("Token","");
                Log.d("cookie1","on getWorkHours token: "+token);
                params.put("Authorization", "Bearer "+ token);
                return params;
            }

        };
        requestQueue.add(groupRequest);
    }

    public void jsonDataToListView(){

        listView = findViewById(R.id.listView);

        listAdapter = new ListArrayAdapter(this, R.layout.row_layout);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);


        try {

            hourIdList = new ArrayList<String>();
            startTimeList = new ArrayList<String>();
            endTimeList = new ArrayList<String>();
            descriptionList = new ArrayList<String>();
            int count = 0;
            String startTime, endTime, description;
            JSONArray jsonArray = new JSONArray(jsonString);
            while(count < jsonArray.length())
            {
                splitTimeStamp = "[T, ., Z]";

                JSONObject JO = jsonArray.getJSONObject(count);

                startTime = JO.getString("starttime");
                startTimeList.add(startTime);
                String[] timeStampSplitted = startTime.split(splitTimeStamp);
                startTime = timeStampSplitted[0] + "  " + timeStampSplitted[1];

                endTime = JO.getString("endtime");
                endTimeList.add(endTime);
                timeStampSplitted = endTime.split(splitTimeStamp);
                endTime = timeStampSplitted[0] + "  " + timeStampSplitted[1];

                description = JO.getString("description");

                hourid = JO.getString("hoursid");

                HourListingsClass hourListings = new HourListingsClass(startTime, endTime, description, hourid);
                listAdapter.add(hourListings);
                count++;

                hourIdList.add(hourid);
                descriptionList.add(description);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(hourIdList.get(position));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("");
        builder.setMessage("What do you want to do?");



        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int which) {
                //Do nothing
            }
        });

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //DELETE Request
                //hourid = hourIdList.get(position);
                deleteRequest(hourIdList.get(position));
            }
        });

        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //PUT Request

                Intent intent = new Intent(DayActivity.this, EditActivity.class);
                intent.putExtra("hourid",hourIdList.get(position));
                intent.putExtra("token",token);
                intent.putExtra("startTime",startTimeList.get(position));
                intent.putExtra("endTime",endTimeList.get(position));
                intent.putExtra("description",descriptionList.get(position));
                startActivity(intent);

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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

    public void deleteRequest(String selectedHourId) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String mRequestBody = "jsonBody.toString()";

        final String url = "https://workh.herokuapp.com/workhours/" + selectedHourId;

        final StringRequest groupRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(DayActivity.this, "Posting deleted", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE", response);

                    getWorkHours();
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 401) {
                    refreshRequest();

                } else {
                    // irrecoverable errors. show error to user.
                }
            }
        }) {

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
                token = _preferences.getString("Token","");
                params.put("Authorization", "Bearer "+ token);
                return params;
            }
        };
        requestQueue.add(groupRequest);
    }

}
