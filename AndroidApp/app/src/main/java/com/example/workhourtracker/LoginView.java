package com.example.workhourtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LoginView extends AppCompatActivity {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "refreshToken";

    private SharedPreferences _preferences;

    private String usernameInput;
    private String passwordInput;
    int ADD_NEW_PART_INTENT_ID = 2345;
    private String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

        signUpInstanceButton();
    }

    public void loginButtonClicked(View view) {
        EditText editTextUsername = findViewById(R.id.usernameEditText);
        EditText editTextPassword = findViewById(R.id.passwordEditText);
        usernameInput = editTextUsername.getText().toString();
        passwordInput = editTextPassword.getText().toString();

        if (usernameInput.length() == 0 || passwordInput.length() == 0) {
            Toast.makeText(this, "Enter your username and password", Toast.LENGTH_SHORT).show();
        } else {
            //login functionality

            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", usernameInput);
                jsonBody.put("password", passwordInput);

                final String mRequestBody = jsonBody.toString();
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                final String url = "https://workh.herokuapp.com/login";

                final StringRequest groupRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("RESPONSE", response);

                            try {
                                //Parse response JSONdata
                                JSONObject obj = new JSONObject(response);
                                String userID = obj.getString("userID");
                                String token = obj.getString("token");

                                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

                                //Go next activity with userID and token
                                Intent mainMenuIntent = new Intent(this, MainMenuView.class);
                                mainMenuIntent.putExtra("token", token);
                                mainMenuIntent.putExtra("userID", userID);
                                startActivityForResult(mainMenuIntent, ADD_NEW_PART_INTENT_ID);

                                

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> showError(error.toString())) {

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
                        responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                        }
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = super.getHeaders();


                        if (params == null
                                || params.equals(Collections.emptyMap())) {
                            params = new HashMap<String, String>();
                        }

                        Log.d("RESPONSE", params.toString());
                        if (params.containsKey(SET_COOKIE_KEY)){
                            Log.d("RESPONSE", "set-cookies yes");
                        }

                        if (params.containsKey(SET_COOKIE_KEY)
                                && params.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
                            String cookie = params.get(SET_COOKIE_KEY);
                            if (cookie.length() > 0) {
                                String[] splitCookie = cookie.split(";");
                                String[] splitSessionId = splitCookie[0].split("=");
                                cookie = splitSessionId[1];
                                SharedPreferences.Editor prefEditor = _preferences.edit();
                                prefEditor.putString(SESSION_COOKIE, cookie);
                                Log.d("RESPONSE", cookie);
                                prefEditor.commit();
                            }
                        }


                        return params;
                    }
                };
                requestQueue.add(groupRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void showError(String loginError) {
        Log.e("ERROR", loginError);
        Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
    }

    public void signUpInstanceButton() {
        //Create new account -textView with functionality
        TextView textViewNewAccount = findViewById(R.id.newAccountTextView);
        String textViewText = "Don't have account? create a new account";

        SpannableString ss = new SpannableString(textViewText);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                goToSignUpView();
            }
        };

        ss.setSpan(clickableSpan1, 20, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewNewAccount.setText(ss);
        textViewNewAccount.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void goToSignUpView() {
        Intent signUpIntent = new Intent(this, SignUpView.class);
        startActivity(signUpIntent);
    }

    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }
}