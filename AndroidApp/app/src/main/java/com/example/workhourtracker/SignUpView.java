package com.example.workhourtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Map;

public class SignUpView extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String emailInput;
    private String usernameInput;
    private String passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
    }


    public void createAccountButtonClicked(View view) throws JSONException {
        EditText editTextUsername = findViewById(R.id.usernameEditText);
        EditText editTextPassword = findViewById(R.id.passwordEditText);
        EditText editTextEmail = findViewById(R.id.emailEditText);
        
        usernameInput = editTextUsername.getText().toString();
        passwordInput = editTextPassword.getText().toString();
        emailInput = editTextEmail.getText().toString();

        if (usernameInput.length() == 0 || passwordInput.length() == 0 || emailInput.length() == 0) {
            Toast.makeText(this, "Enter your email, username and password", Toast. LENGTH_SHORT). show();
        }
        else {

            try{
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", usernameInput);
                jsonBody.put("password", passwordInput);
                jsonBody.put("email", emailInput);

                final String mRequestBody = jsonBody.toString();
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                final String url = "https://workh.herokuapp.com/createUsers";

                final StringRequest groupRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Toast.makeText(this, "User created", Toast. LENGTH_SHORT). show();
                            onBackPressed();
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
        }

        //send

    }
}