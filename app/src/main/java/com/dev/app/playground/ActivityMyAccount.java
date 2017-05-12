package com.dev.app.playground;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saugat Shrestha on 11/29/2016.
 */

public class ActivityMyAccount extends AppCompatActivity {
    private static  int RESULT_LOAD_IMAGE = 1;
    CircleImageView account_img;
    EditText mAccountName, mAccountMobile, mAccountEmail, mAccountAddress;
    TextView mCartCount, mWishlistCount;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        account_img = (CircleImageView) findViewById(R.id.accountImage);
        mAccountName = (EditText) findViewById(R.id.accountName);
        mAccountName.setFocusable(false);
        mAccountMobile = (EditText) findViewById(R.id.accountMobile);
        mAccountMobile.setFocusable(false);
        mAccountEmail = (EditText) findViewById(R.id.accountEmail);
        mAccountEmail.setFocusable(false);
        mAccountAddress = (EditText) findViewById(R.id.accountAddress);
        mAccountAddress.setFocusable(false);
        mCartCount = (TextView) findViewById(R.id.cartCount);
        mWishlistCount = (TextView) findViewById(R.id.wishlistCount);
        new AccountAsyncTask().execute();


        account_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
        findViewById(R.id.accountEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMyAccount.this, ActivityUpdateAccount.class));

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImage = data.getData();
                Glide.with(getApplicationContext())
                        .load(selectedImage)
                        .placeholder(R.drawable.tulipfinal)
                        .into(account_img);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return  image;

    }

    class AccountAsyncTask extends AsyncTask<String, String, String> {

        int flag = 0;
        String wishListCount;
        loginDetail lDetail;
        Gson gson = new Gson();
        GlobalState globalState = GlobalState.singleton;
        loginDetail lD = gson.fromJson(globalState.getPREFS_VALID_USER_INFO(), loginDetail.class);
        String token = lD.token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ActivityMyAccount.this, "", "Loading");
        }

        @Override
        protected String doInBackground(String... params) {
            if (!GlobalState.isNetworkOnline() || !GlobalState.isGoogleReachableWithInetAddress()) {
                flag = 5;
            } else {
                HashMap<String, String> loginHm = new HashMap<>();
                JsonParser jsonParser = new JsonParser();
                JSONObject jsonObject = jsonParser.getForJSONObject(Constant.BASE_URL + "myaccount?api_token=" + token, loginHm);

                try {
                    if (jsonObject == null) {
                        flag = 1;

                    } else if (jsonObject.getString("error").equals("false")) {
                        flag = 2;

                        JSONObject jObject = jsonObject.getJSONObject("model");

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

                        wishListCount = jsonObject.getString("wishListCount");

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
            if (flag == 2) {
                mAccountName.setText(lDetail.sFullName);
                mAccountMobile.setText(lDetail.sMobile);
                mAccountEmail.setText(lDetail.sEmail);
                mAccountAddress.setText(lDetail.sAddress + "," + lDetail.sCIty);
                ImageView iV = (ImageView) findViewById(R.id.accountImage);
                Glide.with(getApplicationContext()).load("http://159.203.186.37/laravel/ecommerce/public/users/" + lDetail.sImage)
                        .dontAnimate().into(iV);
                mCartCount.setText(String.valueOf(GlobalState.gCartCount));
                mWishlistCount.setText(String.valueOf(wishListCount));
            }
        }

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
}
