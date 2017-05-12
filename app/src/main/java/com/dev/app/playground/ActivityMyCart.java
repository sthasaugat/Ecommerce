package com.dev.app.playground;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.app.playground.Adapter.CartAdapter;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JavaObject.MyCart;

import java.util.ArrayList;
import java.util.List;


public class ActivityMyCart extends AppCompatActivity {

    Button mReturnShop;
    List<MyCart> myCart = new ArrayList<>();
    LinearLayout mLinearLayout;
    RecyclerView mCartRV;
    CartAdapter mAdapter;
    nBulletinDatabase db;
    LinearLayoutManager llm;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = nBulletinDatabase.getInstace(getApplicationContext());
        mReturnShop = (Button) findViewById(R.id.btn_ReturnShop);
        mLinearLayout = (LinearLayout) findViewById(R.id.emptyCartView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_cart_swipeRefreshLayout);
        llm = new LinearLayoutManager(ActivityMyCart.this, LinearLayoutManager.VERTICAL, false);
        mCartRV = (RecyclerView) findViewById(R.id.cartView);
        mReturnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLinearLayout.setVisibility(View.GONE);
        myCart = db.getMyCart();

        refreshingCode();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshingCode();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void refreshingCode() {
        GlobalState.gCartCount = myCart.size();
        int tempInt = 0;
        String temp;
        for (int i=0;i< myCart.size();i++){
            temp = myCart.get(i).getPrice();
            tempInt =tempInt + Integer.parseInt(temp);
        }
        total = Integer.toString(tempInt);
        if (GlobalState.gCartCount==0) {
            mLinearLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mLinearLayout.setVisibility(View.GONE);
            mAdapter = new CartAdapter(myCart, getApplicationContext());
            mCartRV.setLayoutManager(llm);
            mCartRV.setAdapter(mAdapter);
            Snackbar snackbar;
            snackbar = Snackbar.make(findViewById(R.id.activity_cart_swipeRefreshLayout), "Rs." + total, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("CONTINUE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ActivityMyCart.this,ActivityHome.class));
                }
            });

            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.ButtonTextColor));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            textView.setTextSize( 20 );
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

            snackbar.show();        }
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

    private class MyUndoListener {
    }
}
