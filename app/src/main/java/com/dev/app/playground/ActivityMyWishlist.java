package com.dev.app.playground;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.app.playground.Adapter.WishAdapter;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.dev.app.playground.JavaObject.Add2wishList;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saugat Shrestha on 11/28/2016.
 */

public class ActivityMyWishlist extends AppCompatActivity {
    Button mReturnShop;
    RecyclerView mWishRV;
    WishAdapter mAdapter;
    LinearLayout mLinearLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    nBulletinDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        db = nBulletinDatabase.getInstace(getApplicationContext());
        mReturnShop = (Button) findViewById(R.id.btn_ReturnShop);
        mReturnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLinearLayout = (LinearLayout) findViewById(R.id.emptyWishListView);
        mLinearLayout.setVisibility(View.GONE);
        new AsyncWishList().execute();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                db.deleteAllWish();
                new AsyncWishList().execute();
            }
        });

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

    private class AsyncWishList extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(ActivityMyWishlist.this);
        Gson gson = new Gson();
        GlobalState globalState = GlobalState.singleton;
        int flag = 0;
        int setlayout = 0;
        public List<Add2wishList> wishList = new ArrayList<>();
        loginDetail lD = gson.fromJson(globalState.getPREFS_VALID_USER_INFO(), loginDetail.class);
        String token = lD.token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {

            if (!GlobalState.isNetworkOnline() || !GlobalState.isGoogleReachableWithInetAddress()) {
                flag = 1;
            } else {
                HashMap<String, String> wishHm = new HashMap<>();

                JsonParser jsonParser = new JsonParser();
                JSONObject jsonObject = jsonParser.getForJSONObject(Constant.BASE_URL + Constant.WISH_LIST + token, wishHm);

                try {
                    if (jsonObject == null) {
                        flag = 2;

                    } else if (jsonObject.getString("error").equals("false")) {
                        flag = 3;

                        JSONArray jArray = jsonObject.getJSONArray("model");

                        // Extract data from json and store into ArrayList as class objects
                        for (int i = 0; i < jArray.length(); i++) {
                            setlayout = 1;
                            db.deleteAllWish();
                            JSONObject json_data = jArray.getJSONObject(i);
                            String pID = json_data.getString("product_id");
                            String productName = json_data.getString("name");
                            String price = json_data.getString("price");
                            String image = Constant.IMAGE_BASE + json_data.getString("image");
                            String wishID = json_data.getString("wishlist_id");
                            db.addToWishlist(new Add2wishList(pID, productName, price, image, wishID));
                        }

                    } else {
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
            //this method will be running on UI thread
            progressDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            if (flag == 1) {
                Toast.makeText(ActivityMyWishlist.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            if (flag == 2) {
                Toast.makeText(ActivityMyWishlist.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
            if (flag == 3) {
                // Setup and Handover data to recyclerView
                wishList = db.getMyWishlist();
                mWishRV = (RecyclerView) findViewById(R.id.wishList_RecyclerView);
                mAdapter = new WishAdapter(wishList, getApplicationContext());
                mWishRV.setAdapter(mAdapter);
                GridLayoutManager llm = new GridLayoutManager(ActivityMyWishlist.this, 2);
                mWishRV.setLayoutManager(llm);
                if (setlayout == 0) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
                if (setlayout == 1) {
                    mLinearLayout.setVisibility(View.GONE);

                }
            }
            if (flag == 4) {
                Toast.makeText(ActivityMyWishlist.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
