package com.dev.app.playground;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.JSONParser.JsonParser;


public class ActivitySignUp extends AppCompatActivity {

    EditText mEmail, mPassword, mFullName, mCity, mZip,
            mPostal, mAddress, mPhone, mMobile, mConfirmPassword;
    String sEmail, sPassword, sFullName, sCity,
             sZip, sPostal, sAddress, sPhone, sMobile;
    ProgressDialog progressDialog;
    CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //finding the editText and assigning
        mEmail = (EditText) findViewById(R.id.signUpEmail);
        mPassword = (EditText) findViewById(R.id.signUpPassword);
        mConfirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        mFullName = (EditText) findViewById(R.id.signUpName);
        mCity = (EditText) findViewById(R.id.signUpCity);
        mZip = (EditText) findViewById(R.id.signUpZip);
        mPostal = (EditText) findViewById(R.id.signUpPostal);
        mAddress = (EditText) findViewById(R.id.signUpAddress);
        mPhone = (EditText) findViewById(R.id.signUpPhone);
        mMobile = (EditText) findViewById(R.id.signUpMobile);
        mCheckBox = (CheckBox) findViewById(R.id.signUp_checkBox);

        //button click and validation
        findViewById(R.id.signUp_btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validating signUp
                if (mFullName.length() < 5) {
                    mFullName.requestFocus();
                    mFullName.setError("must be atleast 5 characters");
                }
                //email validation
                else if (!validate()) {
                    mEmail.requestFocus();
                    mEmail.setError("Invalid email");
                } else if (mPassword.length() < 6) {
                    mPassword.requestFocus();
                    mPassword.setError("must be atleast 6 characters");
                } else if (!matchPassword()) {
                    mConfirmPassword.requestFocus();
                    mConfirmPassword.setError("password mismatch");
                } else if (mEmail.length() < 1) {
                    mEmail.requestFocus();
                    mEmail.setError("Field empty");
                } else if (mCity.length() < 1) {
                    mCity.requestFocus();
                    mCity.setError("kathmandu only");
                } else if (mZip.length() < 1) {
                    mZip.requestFocus();
                    mZip.setError("field empty");
                } else if (mPostal.length() < 1) {
                    mPostal.requestFocus();
                    mPostal.setError("field empty");
                } else if (mAddress.length() < 1) {
                    mAddress.requestFocus();
                    mAddress.setError("field empty");
                } else if (mPhone.length() < 1) {
                    mPhone.requestFocus();
                    mPhone.setError("invalid number");
                } else if (mMobile.length() < 10) {
                    mMobile.requestFocus();
                    mMobile.setError("invalid number");

                } else if (!mCheckBox.isChecked()){
                    PrivacyMsg();
                }
                else if (!isOnline()) {
                    Toast.makeText(ActivitySignUp.this,
                            "No internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    // converting to string
                    sEmail = mEmail.getText().toString();
                    sPassword = mPassword.getText().toString();
                    sFullName = mFullName.getText().toString();
                    sCity = mCity.getText().toString();
                    sZip = mZip.getText().toString();
                    sPostal = mPostal.getText().toString();
                    sAddress = mAddress.getText().toString();
                    sPhone = mPhone.getText().toString();
                    sMobile = mMobile.getText().toString();
                    new SignupAsyncTask().execute();

                }
            }
        });


    }

    private void PrivacyMsg() {
        Toast.makeText(this, "Please accept Terms and Privacy", Toast.LENGTH_SHORT).show();
    }

    private boolean matchPassword() {
        String pass = mConfirmPassword.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (pass.matches(password)) return true;
        else return false;
    }

    private boolean validate() {
        String emailPattern = "[a-zA-Z0-9._-]+@+[a-z]+.+[a-z]";
        String tempEmail = mEmail.getText().toString().trim();
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


    class SignupAsyncTask extends AsyncTask<String, String, String> {

        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ActivitySignUp.this, "Wait", "Signing up");
        }

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> signUpHm = new HashMap<>();
            signUpHm.put("email", sEmail);
            signUpHm.put("password", sPassword);
            signUpHm.put("fullname", sFullName);
            signUpHm.put("city", sCity);
            signUpHm.put("zip", sZip);
            signUpHm.put("postal", sPostal);
            signUpHm.put("address", sAddress);
            signUpHm.put("phone1", sPhone);
            signUpHm.put("mobile", sMobile);

            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.performPostCI(Constant.BASE_URL+Constant.REGISTRATION, signUpHm);

            try {
                if (jsonObject == null) {
                    flag = 1;

                } else if (jsonObject.getString("error").equals("false")) {
                    flag = 2;

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
                Toast.makeText(ActivitySignUp.this, "Server Error", Toast.LENGTH_SHORT).show();

            } else if (flag == 2) {
                progressDialog.dismiss();
                Toast.makeText(ActivitySignUp.this, "Registration Successful", Toast.LENGTH_LONG).show();
                finish();

            } else if (flag == 3) {
                progressDialog.dismiss();
                Toast.makeText(ActivitySignUp.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
            } else if (flag == 4) {
                progressDialog.dismiss();
                mEmail.requestFocus();
                mEmail.setError("Duplicate email");
            }
        }
    }
}