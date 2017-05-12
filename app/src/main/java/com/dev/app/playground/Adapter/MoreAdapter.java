package com.dev.app.playground.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.app.playground.ActivityItemDetail;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.JavaObject.FeaturedProduct;
import com.dev.app.playground.JavaObject.MyCart;
import com.dev.app.playground.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Saugat Shrestha on 12/30/2016.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MyMoreHolder> {
    List<FeaturedProduct> product;
    Context context;
    nBulletinDatabase db;
    public MoreAdapter(List<FeaturedProduct> product, Context context) {
        this.product = product;
        this.context = context;
        db = nBulletinDatabase.getInstace(context);

    }

    @Override
    public MyMoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemVIew = LayoutInflater.from(parent.getContext()).
               inflate(R.layout.more_view,parent,false);
        return new MyMoreHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(final MyMoreHolder holder, final int position) {
        String pID = product.get(position).getpID();
        holder.tvName.setText(product.get(position).getProductName());
        holder.tvPrice.setText("Rs." + (product.get(position).getPrice()));
        final String image =(product.get(position).getImage());
        holder.iV.setImageDrawable(null);
        //load image into imageView using glide
        Glide.with(holder.iV.getContext()).load(image)
                .placeholder(R.drawable.tulipfinal)
                .error(R.drawable.tulipfinal)
                .into(holder.iV);

        holder.cV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), ActivityItemDetail.class);
                Gson gson = new Gson();
                in.putExtra("product", gson.toJson(product.get(position)));
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class MyMoreHolder extends RecyclerView.ViewHolder {
        CardView cV;
        ImageView iV;
        TextView tvName, tvPrice;
        public MyMoreHolder(View itemView) {
            super(itemView);iV = (ImageView) itemView.findViewById(R.id.more_img);
            tvName = (TextView) itemView.findViewById(R.id.more_product);
            tvPrice = (TextView) itemView.findViewById(R.id.more_price);
            cV = (CardView) itemView.findViewById(R.id.cv_row);

        }
    }
}
