package com.dev.app.playground;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dev.app.playground.Adapter.FeaturedAdapter;
import com.dev.app.playground.Adapter.MoreAdapter;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;

/**
 * Created by Saugat Shrestha on 12/30/2016.
 */

public class ActivityMore extends AppCompatActivity {

    nBulletinDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        db = nBulletinDatabase.getInstace(getApplicationContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.moreRecyclerView);
        MoreAdapter mAdapter = new MoreAdapter(db.getProduct(12),getApplicationContext());
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager glm = new GridLayoutManager(ActivityMore.this,2);
        recyclerView.setLayoutManager(glm);

    }
}
