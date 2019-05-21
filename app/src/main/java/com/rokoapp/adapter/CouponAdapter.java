package com.rokoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.activity.ApplyCoupons;
import com.rokoapp.model.MyCouponModel;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyCouponModel> couponList;
    private ApplyCoupons applyCoupons;

    public CouponAdapter(Context mContext, List<MyCouponModel> couponList, ApplyCoupons applyCoupons) {
        this.mContext = mContext;
        this.couponList = couponList;
        this.applyCoupons = applyCoupons;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView couponName, validityCoupon, couponDesc, copyCouponCode;

        MyViewHolder(View itemView) {
            super(itemView);
            couponName = itemView.findViewById(R.id.couponName);
            validityCoupon = itemView.findViewById(R.id.validityCoupon);
            couponDesc = itemView.findViewById(R.id.couponDesc);
            copyCouponCode = itemView.findViewById(R.id.copyCouponCode);

            if (applyCoupons.from.equals("payment")){
                copyCouponCode.setVisibility(View.VISIBLE);
            }else
                copyCouponCode.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_coupon_itens, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MyCouponModel couponModel = couponList.get(position);

        holder.couponName.setText(couponModel.getCouponName());
        holder.couponDesc.setText(couponModel.getCouponDesc());
        holder.validityCoupon.setText("Valid till "+couponModel.getCouponValidity());

        holder.copyCouponCode.setOnClickListener(v -> {
            if (applyCoupons.couponEditText.getText().toString().trim().isEmpty() || applyCoupons.couponEditText.getText().toString().trim().equals("")) {
                applyCoupons.couponEditText.setText(couponModel.getCouponName());
            }else {
                applyCoupons.couponEditText.setText(null);
                applyCoupons.couponEditText.setText(couponModel.getCouponName());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (couponList != null) {
            return couponList.size();
        } else return 0;
    }
}