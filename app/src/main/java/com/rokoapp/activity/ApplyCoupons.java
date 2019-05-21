package com.rokoapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.adapter.CouponAdapter;
import com.rokoapp.model.MyCouponModel;

import java.util.ArrayList;
import java.util.List;

import static com.rokoapp.utils.ParamUtils.MY_COUPON_CODE;

public class ApplyCoupons extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cardApplyCoupon;
    public EditText couponEditText;
    private TextView applyButton;
    private RecyclerView couponRecycler;
    private CouponAdapter couponAdapter;

    public String from;
    private List<MyCouponModel> couponList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupons);
        initViews();

        from = getIntent().getStringExtra("from");
        if (from.equals("payment")){
            cardApplyCoupon.setVisibility(View.VISIBLE);
        }else
            cardApplyCoupon.setVisibility(View.GONE);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("My Coupons");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        couponRecycler.setHasFixedSize(true);
        couponAdapter = new CouponAdapter(ApplyCoupons.this, couponList, ApplyCoupons.this);
        couponRecycler.setLayoutManager(new LinearLayoutManager(ApplyCoupons.this, LinearLayoutManager.VERTICAL, false));
        couponRecycler.setItemAnimator(new DefaultItemAnimator());
        couponRecycler  .setAdapter(couponAdapter);

        showCouponData();

        applyButton.setOnClickListener( v-> applyCouponToUser());
    }

    private void applyCouponToUser() {
        if (couponEditText.getText().toString().trim().isEmpty() && couponEditText.getText().toString().trim().equals("")) {
            couponEditText.requestFocus();
            couponEditText.setError("Coupon Code shouldn't be empty.");
            return;
        }
        if (couponEditText.getText().toString().trim().length()>3) {
            MY_COUPON_CODE = couponEditText.getText().toString().trim();
            finish();
        }
    }

    private void showCouponData() {
        couponList.add(new MyCouponModel("1","FIRSTRIDEFREE","1 promotional ride for new users.","18-jun-2019"));
        couponList.add(new MyCouponModel("2","HAPPYSUMMER","Roko offers 1 promotional ride to make your summer more happy.","21-jun-2019"));
        couponList.add(new MyCouponModel("3","MYBIRTHDAYBASH","Roko wishes you a very Happy Birthday with 1 ride free from our side.","25-jun-2019"));
        couponList.add(new MyCouponModel("5","ROKOLUCKYGO","1 free ride from us, for the lucky users",""));
        couponList.add(new MyCouponModel("6","MYROKOMYRIDE","1 free ride to refer Roko app to your friend","3-jul-2019"));
        couponAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        cardApplyCoupon = findViewById(R.id.cardApplyCoupon);
        couponEditText = findViewById(R.id.couponEditText);
        applyButton = findViewById(R.id.applyButton);
        couponRecycler = findViewById(R.id.couponRecycler);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ApplyCoupons.this.finish();
    }
}
