package com.dev.app.playground;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.dev.app.playground.JavaObject.Add2wishList;
import com.dev.app.playground.JavaObject.FeaturedProduct;
import com.dev.app.playground.JavaObject.MyCart;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dell on 11/17/2016.
 */
public class ActivityItemDetail extends AppCompatActivity {
    MyCart myCart;
    TextView item_DetailName, item_price;
    ImageView ivProduct;
    final Context context = this;
    String productName, image, fProduct, price, pID;
    FeaturedProduct singleProduct;
    Gson gson = new Gson();
    nBulletinDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        db = nBulletinDatabase.getInstace(getApplicationContext());
        //fetching data from intent
        fProduct = getIntent().getStringExtra("product");
        singleProduct = gson.fromJson(fProduct, FeaturedProduct.class);
        pID = singleProduct.getpID();
        productName = singleProduct.getProductName();
        price = singleProduct.getPrice();
        image = singleProduct.getImage();
        //finding view from layout
        item_DetailName = (TextView) findViewById(R.id.item_detailName);
        item_price = (TextView) findViewById(R.id.tv_price_detail);
        ivProduct = (ImageView) findViewById(R.id.itemDetail_image);
        CardView addCart = (CardView) findViewById(R.id.itemDetailAddCart);
        CardView orderNow = (CardView) findViewById(R.id.itemDetailOrder);
        CardView addWishlist = (CardView) findViewById(R.id.itemDetailAddWishList);
        //setting data into layout
        item_DetailName.setText(productName);
        item_price.setText("Rs."+price);
        Glide.with(ivProduct.getContext()).load(image)
                .placeholder(R.drawable.tulipfinal)
                .error(R.drawable.tulipfinal)
                .into(ivProduct);
        //listening for OrderNow Click
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderNowClicked();
            }
        });
        //Listening for AddToCart Click
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check internet
                if (!GlobalState.isNetworkOnline() || GlobalState.isGoogleReachableWithInetAddress()) {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    //check item in local database
                    boolean status = db.CheckIsDataAlreadyInDBorNot("cart_table","id",pID);
                    if (!status){
                        //add if not present
                        db.addToMyCart(new MyCart(image, productName, price, pID, "1"));
                        onAddClicked("AddedToCartMsg");
                    }else{
                        //display if already present
                        onAddClicked("ItemAlreadyInCart");
                    }
                }
            }
        });
        //Listening for AddToWishList Click
        addWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //check internet
                if (!GlobalState.isNetworkOnline() || GlobalState.isGoogleReachableWithInetAddress()) {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    //check item in local database
                    boolean status = db.CheckIsDataAlreadyInDBorNot("wishlist_table","id",pID);
                    if (!status)
                    {
                        //add if not present in local and server
                        new AddToWishListAsyncTask().execute();
                    } else
                    {
                        //display if already present
                        onAddClicked("ItemAlreadyInWishlist");
                    }
                }
            }
        });


    }

    private void onAddClicked(String flags) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(ActivityItemDetail.this);
        if (flags == "AddedToCartMsg") {
            //setting message for cart
            builder.setMessage("Item " + productName + " has been added to your cart");
            builder.setPositiveButton("OK", null);
            builder.show();
        }
        if (flags == "AddedToWishListMsg") {
            //setting message for wishList
            builder.setMessage("Item " + productName + " has been added to your Wishlist");
            builder.setPositiveButton("OK", null);
            builder.show();
        }
        if (flags == "ItemAlreadyInCart"){
            builder.setMessage("Item " + productName + " is already in your Cart");
            builder.setPositiveButton("OK", null);
            builder.show();
        }
        if (flags == "ItemAlreadyInWishlist"){
            builder.setMessage("Item " + productName + " is already in your Wishlist");
            builder.setPositiveButton("OK", null);
            builder.show();
        }
    }

    private void onOrderNowClicked() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.order_now);
        dialog.setTitle("Call us now");

        // set the custom dialog components - text, image and button
        TextView number = (TextView) dialog.findViewById(R.id.order_nowNumber);
        number.setText("9801083175");
        TextView number2 = (TextView) dialog.findViewById(R.id.order_nowNumber2);
        number2.setText("9849783175");

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
                my_callIntent.setData(Uri.parse("tel:" + "9801083175"));
                startActivity(my_callIntent);
            }
        });

        number2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
                my_callIntent.setData(Uri.parse("tel:" + "9849783175"));
                startActivity(my_callIntent);
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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

    private class AddToWishListAsyncTask extends AsyncTask<String, String, String> {
        GlobalState globalState = GlobalState.singleton;
        Gson gson = new Gson();
        int flag = 0;
        loginDetail lD = gson.fromJson(globalState.getPREFS_VALID_USER_INFO(), loginDetail.class);
        String token = lD.token;

        @Override
        protected String doInBackground(String[] params) {
            HashMap<String, String> addWishHm = new HashMap<>();
            addWishHm.put("product_id", pID);
            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.performPostCI(Constant.BASE_URL + Constant.ADD_LIST + token, addWishHm);

            try {
                if (jsonObject == null) {
                    flag = 1;

                } else if (jsonObject.getString("error").equals("false")) {
                    db.addToWishlist(new Add2wishList(pID, productName, price, image, null));
                    flag = 2;
                } else {
                    flag = 3;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 2) {
                onAddClicked("AddedToWishListMsg");
            }
        }

    }
}