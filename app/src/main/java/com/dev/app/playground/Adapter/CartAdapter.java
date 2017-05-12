package com.dev.app.playground.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.JavaObject.Add2wishList;
import com.dev.app.playground.JavaObject.MyCart;
import com.dev.app.playground.R;

import java.util.List;

/**
 * Created by Saugat Shrestha on 12/16/2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyCartHolder> {
    private List<MyCart> myCart;
    nBulletinDatabase db;
    Context context;
    int quantity = 1;

    public CartAdapter(List<MyCart> myCart, Context context) {
        this.context = context;
        this.myCart = myCart;
        db = nBulletinDatabase.getInstace(context);

    }

    @Override
    public CartAdapter.MyCartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_cart_view, parent, false);
        return new MyCartHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyCartHolder holder, final int position) {
        holder.pName.setText(myCart.get(position).getProductName());
        holder.pPrice.setText("Rs. " + myCart.get(position).getPrice());
        final String image = myCart.get(position).getImage();
        Glide.with(holder.pImage.getContext()).load(image)
                .placeholder(R.drawable.tulipfinal)
                .into(holder.pImage);
        holder.bRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removing item from TABLE_MY_CART
                int id = Integer.parseInt(myCart.get(position).getpID());
                db.deleteCartItem(id);
                myCart.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();

            }
        });

        holder.bIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                holder.pQuantity.setText(Integer.toString(quantity));
            }
        });
        holder.bDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    holder.pQuantity.setText(Integer.toString(quantity));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return myCart.size();
    }

    public class MyCartHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView pName, pPrice, pQuantity;
        ImageView pImage;
        Button bRemove, bIncrement, bDecrement;

        public MyCartHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardCartView);
            pName = (TextView) itemView.findViewById(R.id.pName);
            pPrice = (TextView) itemView.findViewById(R.id.pPrice);
            pImage = (ImageView) itemView.findViewById(R.id.pImage);
            bRemove = (Button) itemView.findViewById(R.id.bRemove);
            bIncrement = (Button) itemView.findViewById(R.id.increment);
            pQuantity = (TextView) itemView.findViewById(R.id.quantity);
            bDecrement = (Button) itemView.findViewById(R.id.decrement);
        }
    }
}
