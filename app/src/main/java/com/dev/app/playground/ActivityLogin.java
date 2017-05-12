package com.dev.app.playground;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.google.gson.Gson;

public class ActivityLogin extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private String sEmail, sPassword;
    ProgressDialog progressDialog;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        Button mLogin = (Button) findViewById(R.id.loginButton);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.length() < 1) {
                    mEmail.requestFocus();
                    mEmail.setError("Field empty");
                } else if (mPassword.length() < 1) {
                    mEmail.requestFocus();
                    mPassword.setError("Field empty");
                } else {
                    sEmail = mEmail.getText().toString();
                    sPassword = mPassword.getText().toString();
                    new LoginAsyncTask().execute();
                }

            }
        });

        findViewById(R.id.Login_SignBtn).setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
                     }
                 }

                );
    }


//**
// checking internet connection
// **

    class LoginAsyncTask extends AsyncTask<String, String, String> {

        int flag = 0;
        loginDetail lDetail;
        Gson gson = new Gson();
        GlobalState globalState = GlobalState.singleton;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ActivityLogin.this, "", "Logging in");
        }

        @Override
        protected String doInBackground(String... params) {
            if (!GlobalState.isNetworkOnline() || !GlobalState.isGoogleReachableWithInetAddress()) {
                flag = 5;
            } else {
                HashMap<String, String> loginHm = new HashMap<>();
                loginHm.put("email", sEmail);
                loginHm.put("password", sPassword);

                JsonParser jsonParser = new JsonParser();
                JSONObject jsonObject = jsonParser.performPostCI(Constant.BASE_URL + Constant.LOGIN, loginHm);

                try {
                    if (jsonObject == null) {
                        flag = 1;

                    } else if (jsonObject.getString("error").equals("false")) {
                        flag = 2;

                        JSONObject jObject = jsonObject.getJSONObject("user");

                        String jId = jObject.getString("id");
                        String jFullname = jObject.getString("fullname");
                        String jEmail = jObject.getString("email");
                        String jCity = jObject.getString("city");
                        String jAddress = jObject.getString("address");
                        String jMobile = jObject.getString("mobile");
                        String jImage = jObject.getString("image");
                        String jToken = jObject.getString("api_token");

                        globalState.setPrefsIsLoggedIn("true", 1);

                        lDetail = new loginDetail(jId, jFullname, jEmail, jCity, jAddress, jMobile, jToken, jImage);
                        globalState.setPrefsValidUserInfo(gson.toJson(lDetail), 1);

                    } else if (jsonObject.getString("error").equals("Wrong email or password")) {
                        flag = 4;

                    } else {
                        flag = 3;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (flag == 1) {
                Toast.makeText(ActivityLogin.this, "Server Error", Toast.LENGTH_LONG).show();
            }
            else if (flag == 2) {
                //directing to main page
                startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
                finish();
            } else if (flag == 3) {
                Toast.makeText(ActivityLogin.this, "Unexpected Error", Toast.LENGTH_LONG).show();
            } else if (flag == 4) {
                Toast.makeText(ActivityLogin.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                mEmail.requestFocus();
            }else if (flag == 5){
                Toast.makeText(ActivityLogin.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else
            Snackbar.make(findViewById(R.id.masterView), "Press again to exit", Snackbar.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}