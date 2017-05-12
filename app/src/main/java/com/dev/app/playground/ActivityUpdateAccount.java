package com.dev.app.playground;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Saugat Shrestha on 12/28/2016.
 */

public class ActivityUpdateAccount extends AppCompatActivity {

    EditText mChangeName, mChangeEmail, mChangeCity, mChangeAddress, mChangeZip, mChangeMobile, mChangePostal, mChangePhone;

    String sChangeEmail, sChangeName, sChangeCity, sChangeZip, sChangePostal, sChangeAddress, sChangePhone, sChangeMobile;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mChangeName = (EditText) findViewById(R.id.changeName);
        mChangeEmail = (EditText) findViewById(R.id.changeEmail);
        mChangeCity = (EditText) findViewById(R.id.changeCity);
        mChangeAddress = (EditText) findViewById(R.id.changeAddress);
        mChangeZip = (EditText) findViewById(R.id.changeZip);
        mChangeMobile = (EditText) findViewById(R.id.changeMobile);
        mChangePostal = (EditText) findViewById(R.id.changePostal);
        mChangePhone = (EditText) findViewById(R.id.changePhone);

        //button click and validation
        findViewById(R.id.saveChanges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validating signUp
                if (mChangeName.length() < 5) {
                    mChangeName.requestFocus();
                    mChangeName.setError("must be atleast 5 characters");
                }
                //email validation
                else if (!validate()) {
                    mChangeEmail.requestFocus();
                    mChangeEmail.setError("Invalid email");
                } else if (mChangeEmail.length() < 1) {
                    mChangeEmail.requestFocus();
                    mChangeEmail.setError("Field empty");
                } else if (mChangeCity.length() < 1) {
                    mChangeCity.requestFocus();
                    mChangeCity.setError("kathmandu only");
                } else if (mChangeZip.length() < 1) {
                    mChangeZip.requestFocus();
                    mChangeZip.setError("field empty");
                } else if (mChangePostal.length() < 1) {
                    mChangePostal.requestFocus();
                    mChangePostal.setError("field empty");
                } else if (mChangeAddress.length() < 1) {
                    mChangeAddress.requestFocus();
                    mChangeAddress.setError("field empty");
                } else if (mChangePhone.length() < 1) {
                    mChangePhone.requestFocus();
                    mChangePhone.setError("invalid number");
                } else if (mChangeMobile.length() < 10) {
                    mChangeMobile.requestFocus();
                    mChangeMobile.setError("invalid number");
                } else if (!isOnline()) {
                    Toast.makeText(ActivityUpdateAccount.this,
                            "No internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    // converting to string
                    sChangeEmail = mChangeEmail.getText().toString();
                    sChangeName = mChangeName.getText().toString();
                    sChangeCity = mChangeCity.getText().toString();
                    sChangeZip = mChangeZip.getText().toString();
                    sChangePostal = mChangePostal.getText().toString();
                    sChangeAddress = mChangeAddress.getText().toString();
                    sChangePhone = mChangePhone.getText().toString();
                    sChangeMobile = mChangeMobile.getText().toString();
                    new UpdateAsyncTask().execute();

                }
            }
        });
    }

    private boolean validate() {
        String emailPattern = "[a-zA-Z0-9._-]+@+[a-z]+.+[a-z]";
        String tempEmail = mChangeEmail.getText().toString().trim();
        if (tempEmail.matches(emailPattern)) return true;
        else return false;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private class UpdateAsyncTask extends AsyncTask<String, String, String> {
        GlobalState globalState = GlobalState.singleton;
        Gson gson = new Gson();
        loginDetail lD = gson.fromJson(globalState.getPREFS_VALID_USER_INFO(), loginDetail.class);
        String token = lD.token;
        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ActivityUpdateAccount.this, null, "saving changes");
        }

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> UpdateHm = new HashMap<>();
            UpdateHm.put("email", sChangeEmail);
            UpdateHm.put("fullname", sChangeName);
            UpdateHm.put("city", sChangeCity);
            UpdateHm.put("zip", sChangeZip);
            UpdateHm.put("postal", sChangePostal);
            UpdateHm.put("address", sChangeAddress);
            UpdateHm.put("phone1", sChangePhone);
            UpdateHm.put("mobile", sChangeMobile);

            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.performPostCI(Constant.BASE_URL + "myaccount/update?api_token=" + token, UpdateHm);

            try {
                if (jsonObject == null) {
                    flag = 1;

                } else if (jsonObject.getString("error").equals("false")) {
                    flag = 2;
                    loginDetail lDetail = new loginDetail();
                    lDetail.setsFullName(sChangeName);
                    lDetail.setsEmail(sChangeEmail);
                    lDetail.setsCIty(sChangeCity);
                    lDetail.setsMobile(sChangeMobile);
                    lDetail.setsAddress(sChangeAddress);
                    lDetail.setToken(token);
                    globalState.setPrefsValidUserInfo(gson.toJson(lDetail), 1);


                } else if (jsonObject.getString("error").equals("true")) {
                    flag = 4;

                } else {
                    flag = 3;
                }
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 1) {
                progressDialog.dismiss();
                Toast.makeText(ActivityUpdateAccount.this, "Server Error", Toast.LENGTH_SHORT).show();

            } else if (flag == 2) {
                progressDialog.dismiss();
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ActivityUpdateAccount.this);
                    builder.setMessage("Changes has been saved");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(ActivityUpdateAccount.this, ActivityMyAccount.class));
                            finish();
                        }
                    });
                    builder.show();

            } else if (flag == 3) {
                progressDialog.dismiss();
                Toast.makeText(ActivityUpdateAccount.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
            } else if (flag == 4) {
                progressDialog.dismiss();
                mChangeEmail.requestFocus();
                mChangeEmail.setError("Duplicate email");
            }
        }
    }

}
