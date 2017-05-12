package com.dev.app.playground.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.app.playground.JavaObject.PolicyObject;
import com.dev.app.playground.R;

import java.util.List;

/**
 * Created by Saugat Shrestha on 12/29/2016.
 */

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.MyPolicyHolder> {
    private List<PolicyObject> listPolicy;
    Context context;

    public PolicyAdapter(List<PolicyObject> listPolicy, Context applicationContext) {
        this.listPolicy = listPolicy;
        this.context = applicationContext;

    }

    @Override
    public PolicyAdapter.MyPolicyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.policy_view, parent, false);
        return new MyPolicyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyPolicyHolder holder, final int position) {
        holder.mTermOfUseView.setVisibility(View.GONE);
        holder.mPolicyName.setText(listPolicy.get(position).getTitle());
        holder.mTouchHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.mTermOfUseView.isShown()) {
                    holder.mTermOfUseView.setVisibility(View.GONE);
                    holder.mButton.setText("+");

                } else {
                    holder.mTermOfUseView.setText("\t"+listPolicy.get(position).getDescription());
                    holder.mTermOfUseView.setVisibility(View.VISIBLE);
                    holder.mButton.setText("-");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listPolicy.size();
    }

    public class MyPolicyHolder extends RecyclerView.ViewHolder {
        boolean value = true;
        Button mButton;
        LinearLayout mTermOfUse;
        TextView mPolicyName;
        TextView mTermOfUseView;
        RelativeLayout mTouchHere;

        public MyPolicyHolder(View itemView) {
            super(itemView);
            mTermOfUse = (LinearLayout) itemView.findViewById(R.id.termOfUse);
            mButton = (Button) itemView.findViewById(R.id.button);
            mTermOfUseView = (TextView) itemView.findViewById(R.id.termOfUseView);
            mPolicyName = (TextView) itemView.findViewById(R.id.policyName);
            mTouchHere = (RelativeLayout) itemView.findViewById(R.id.touchHere);
        }
    }
}