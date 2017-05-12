package com.dev.app.playground.Adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.app.playground.ActivityItemDetail;
import com.dev.app.playground.JavaObject.FeaturedProduct;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.R;
import com.google.gson.Gson;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.MyProductHolder> {
    private List<FeaturedProduct> featuredProductList;


    public FeaturedAdapter(List<FeaturedProduct> featuredProductList) {
        this.featuredProductList = featuredProductList;
    }

    public static class MyProductHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView ivProduct;
        public TextView product, price;

        public MyProductHolder(View itemView) {
            super(itemView);
            ivProduct = (ImageView) itemView.findViewById(R.id.product_img);
            product = (TextView) itemView.findViewById(R.id.tv_product);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            cardView = (CardView) itemView.findViewById(R.id.cv_row);

        }

    }

    @Override
    public FeaturedAdapter.MyProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_row, parent, false);

        return new MyProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyProductHolder holder, final int position) {
        String pID = featuredProductList.get(position).getpID();
        holder.product.setText(featuredProductList.get(position).getProductName());
        holder.price.setText("Rs." + (featuredProductList.get(position).getPrice()));
        String image =(featuredProductList.get(position).getImage());
        holder.ivProduct.setImageDrawable(null);
        //load image into imageView using glide
        Glide.with(holder.ivProduct.getContext()).load(image)
                .placeholder(R.drawable.tulipfinal)
                .error(R.drawable.tulipfinal)
                .into(holder.ivProduct);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), ActivityItemDetail.class);
                Gson gson = new Gson();
                in.putExtra("product", gson.toJson(featuredProductList.get(position)));
                v.getContext().startActivity(in);
            }
        });
    }


    @Override
    public int getItemCount() {
       // return 10;
        return featuredProductList.size();
    }
}
