package com.dev.app.playground;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.dev.app.playground.Adapter.CustomerAdapter;

/**
 * Created by Saugat Shrestha on 12/30/2016.
 */

public class ActivityCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.customerRecyclerView);
        CustomerAdapter customerAdapter = new CustomerAdapter();
        GridLayoutManager glm = new GridLayoutManager(ActivityCustomer.this, 3);
        recyclerView.setAdapter(customerAdapter);
        recyclerView.setLayoutManager(glm);

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
