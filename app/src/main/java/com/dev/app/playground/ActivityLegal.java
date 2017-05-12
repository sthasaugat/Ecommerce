package com.dev.app.playground;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.app.playground.Adapter.PolicyAdapter;
import com.dev.app.playground.Adapter.WishAdapter;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.dev.app.playground.JavaObject.PolicyObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saugat Shrestha on 12/29/2016.
 */

public class ActivityLegal extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        new policyGetter().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private class policyGetter extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(ActivityLegal.this);
        Gson gson = new Gson();
        int flag = 0;
        public List<PolicyObject> listPolicy = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("\tLoading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            if (!GlobalState.isNetworkOnline() || !GlobalState.isGoogleReachableWithInetAddress()) {
                flag = 1;
            } else {
                JsonParser jsonParser = new JsonParser();
                JSONObject jsonObject = jsonParser.getForJSONObject(Constant.BASE_URL + "page", null);
                try {
                    if (jsonObject == null) {
                        flag = 2;
                    } else if (jsonObject.getString("error").equals("false")) {
                        flag = 3;
                        JSONArray jsonArray = jsonObject.getJSONArray("page");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                            String sTitle = jsonObjectData.getString("title");
                            String sDescription = jsonObjectData.getString("description");
                            listPolicy.add(new PolicyObject(sTitle, sDescription));
                        }

                    } else if (jsonObject.getString("error").equals("true")) {
                        flag = 4;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            progressDialog.dismiss();
            if (flag == 1) {
                Toast.makeText(ActivityLegal.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            if (flag == 2) {
                Toast.makeText(ActivityLegal.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
            if (flag == 3) {
                // Setup and Handover data to recyclerView
                RecyclerView recyclerView;
                PolicyAdapter mAdapter;
                recyclerView = (RecyclerView) findViewById(R.id.recyclerPolicy_view);
                LinearLayoutManager llm = new LinearLayoutManager(ActivityLegal.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(llm);
                mAdapter = new PolicyAdapter(listPolicy, getApplicationContext());
                recyclerView.setAdapter(mAdapter);

            }
            if (flag == 4) {
                Toast.makeText(ActivityLegal.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
