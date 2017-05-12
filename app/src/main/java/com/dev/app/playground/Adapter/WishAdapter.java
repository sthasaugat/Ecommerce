package com.dev.app.playground.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.app.playground.ActivityItemDetail;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.JavaObject.Add2wishList;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JSONParser.JsonParser;
import com.dev.app.playground.JavaObject.MyCart;
import com.dev.app.playground.R;
import com.dev.app.playground.loginDetail;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Saugat Shrestha on 12/7/2016.
 */
public class WishAdapter extends RecyclerView.Adapter<WishAdapter.MyWishHolder> {
    private List<Add2wishList> wishList;
    public String wishID;
    Context context;
    nBulletinDatabase db;
    int tempPos;

    public WishAdapter(List<Add2wishList> wishList, Context context) {
        this.wishList = wishList;
        this.context = context;
        db = nBulletinDatabase.getInstace(context);

    }

    @Override
    public WishAdapter.MyWishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.wishlist_view, parent, false);
        return new MyWishHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyWishHolder holder, final int position) {
        holder.tvName.setText(wishList.get(position).getProductName());
        holder.tvPrice.setText("Rs." + wishList.get(position).getPrice());
        wishID = wishList.get(position).getWishID();
        final String image = (wishList.get(position).getImage());
        //load image into imageView using glide
        Glide.with(WishAdapter.MyWishHolder.iV.getContext()).load(image)
                .placeholder(R.drawable.tulipfinal)
                .error(R.drawable.tulipfinal)
                .dontAnimate()
                .into(MyWishHolder.iV);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // remove your item from data base
                tempPos = position;
                new AsyncDeleteItem().execute();
                //remove item from list and notify adapter

            }
        });
        holder.mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addToMyCart(new MyCart(image, wishList.get(position).getProductName(),
                        wishList.get(position).getPrice(), wishList.get(position).getpID(), "1"));

            }
        });
        holder.cV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), ActivityItemDetail.class);
                Gson gson = new Gson();
                in.putExtra("product", gson.toJson(wishList.get(position)));
                v.getContext().startActivity(in);
            }
        });
    }


    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public static class MyWishHolder extends RecyclerView.ViewHolder {
        CardView cV;
        static ImageView iV, remove, mAddToCart;
        TextView tvName, tvPrice;

        public MyWishHolder(View itemView) {
            super(itemView);
            iV = (ImageView) itemView.findViewById(R.id.wishList_img);
            tvName = (TextView) itemView.findViewById(R.id.wishList_product);
            tvPrice = (TextView) itemView.findViewById(R.id.wishList_price);
            cV = (CardView) itemView.findViewById(R.id.cv_row);
            remove = (ImageView) itemView.findViewById(R.id.deleteProduct);
            mAddToCart = (ImageView) itemView.findViewById(R.id.addToCart);
        }
    }

    private class AsyncDeleteItem extends AsyncTask<String, String, String> {
        GlobalState globalState = GlobalState.singleton;
        Gson gson = new Gson();
        int flag = 0;
        loginDetail lD = gson.fromJson(globalState.getPREFS_VALID_USER_INFO(), loginDetail.class);
        String token = lD.token;


        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> DeleteHm = new HashMap<>();
            DeleteHm.put("id", wishID);
            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.performPostCI(Constant.BASE_URL + Constant.REMOVE_LIST + token, DeleteHm);

            try {
                if (jsonObject == null) {
                    flag = 1;
                } else if (jsonObject.getString("error").equals("false")) {
                    flag = 2;
                } else if (jsonObject.getString("error").equals("true")) {
                    flag = 3;
                } else {
                    flag = 4;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            //this method will be running on UI thread
            if (flag == 1 || flag == 3 || flag == 4) {
                Toast.makeText(context, "Error removing item", Toast.LENGTH_SHORT).show();
            }
            if (flag == 2) {
                int pID = Integer.parseInt(wishList.get(tempPos).getpID());
                db.deleteWishlistItem(pID);
                wishList.remove(tempPos);
                notifyItemRemoved(tempPos);
                notifyItemRangeChanged(tempPos, getItemCount());
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
