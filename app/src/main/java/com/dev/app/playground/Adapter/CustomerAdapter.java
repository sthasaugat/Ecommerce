package com.dev.app.playground.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.app.playground.R;

/**
 * Created by Saugat Shrestha on 12/30/2016.
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyServiceHolder> {

    public CustomerAdapter() {

    }

    @Override
    public MyServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customer_view,parent,false);
        return new MyServiceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyServiceHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class MyServiceHolder extends RecyclerView.ViewHolder {
        public MyServiceHolder(View itemView) {
            super(itemView);
        }
    }
}
