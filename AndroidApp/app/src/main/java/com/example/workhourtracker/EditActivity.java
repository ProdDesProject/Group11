package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.SafeIterableMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private String hourid;
    private String token;
    private String startTime;
    private String endTime;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        hourid = intent.getStringExtra("hourid");
        token = intent.getStringExtra("token");
        startTime = intent.getStringExtra("startTine");
        endTime = intent.getStringExtra("endTime");
        description = intent.getStringExtra("description");

        //EditText editStartTime = findViewById(R.id.paivamaaraAloitus);
        //EditText editEndTime = findViewById(R.id.paivamaaraLoppu);
        //EditText editDescription = findViewById(R.id.)

    }




    public void editfunction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        //jsonBody.put("description", additionalInfo);
        //jsonBody.put("startTime", startingTimeStamp);
        //jsonBody.put("endTime", endingTimeStamp);
        final String mRequestBody = jsonBody.toString();

        final String url = "https://workh.herokuapp.com/workhours/" + hourid;

        final StringRequest groupRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    Toast.makeText(EditActivity.this, "Workhours edited", Toast. LENGTH_SHORT). show();
                    Log.d("RESPONSE", response);

                }, error -> Log.e("ERROR", error.toString())) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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


}