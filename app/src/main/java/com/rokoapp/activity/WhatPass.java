package com.rokoapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS;
import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_COMPLEMENT_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.USER_COMPLIMENT_PASS;
import static com.rokoapp.utils.ParamUtils.USER_TOTAL_PASS;

public class WhatPass extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private LinearLayout whatIsPassLayout, myPassLayout;
    private TextView whatPassDesc;
    private Button whatIsPassButton;
    private TextView totalPassCount, comPassCount, comPassValidity, passPrice;

    private int sheetState;
    private ImageView titleT, bannerPassDesc;
    private TextView passDescText, selectPassByRouteDesc, noOfRideInPassDesc, usageOfRideDesc;
    private RelativeLayout passDescHeader;
    private CoordinatorLayout coLayoutWhatPass;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_pass);
        initViews();
        initListeners();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("My Roko Passes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        showData();

        whatIsPassButton.setOnClickListener(v -> {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    private void showData() {
        if (USER_COMPLEMENT_PASS_VALIDITY != null && !USER_COMPLEMENT_PASS_VALIDITY.isEmpty() && !USER_TOTAL_PASS.equals("0")) {
            myPassLayout.setVisibility(View.VISIBLE);
            coLayoutWhatPass.setVisibility(View.GONE);
//            because pass price is not available that's why we are hiding pass price TextView
            passPrice.setVisibility(View.GONE);

            if (!USER_TOTAL_PASS.equals("") & Integer.parseInt(USER_TOTAL_PASS)>1) {
                totalPassCount.setText(USER_TOTAL_PASS + " Rides Left");
            } else if (!USER_TOTAL_PASS.equals("") & Integer.parseInt(USER_TOTAL_PASS)== 1){
                totalPassCount.setText(USER_TOTAL_PASS + " Ride Left");
            }
            if (USER_COMPLIMENT_PASS != null & !USER_COMPLIMENT_PASS.isEmpty() & !USER_COMPLIMENT_PASS.equals("")) {
                if (Integer.parseInt(USER_COMPLIMENT_PASS) > 1) {
                    comPassCount.setText("+ " + USER_COMPLIMENT_PASS + " Rides Extra");
                } else if (Integer.parseInt(USER_COMPLIMENT_PASS) == 1) {
                    comPassCount.setText("+ " + USER_COMPLIMENT_PASS + " Rides Extra");
                }
            }else
                comPassCount.setVisibility(View.GONE);

            if (USER_COMPLEMENT_PASS_VALIDITY !=null) {
                comPassValidity.setText(ParamUtils.changeBookingDateFormat(USER_COMPLEMENT_PASS_VALIDITY));
            }
        }
        else{
            myPassLayout.setVisibility(View.GONE);
            coLayoutWhatPass.setVisibility(View.VISIBLE);
            whatPassDesc.setText(Html.fromHtml(getResources().getString(R.string.purchase_pass_desc)));
            passDescText.setText(Html.fromHtml(getResources().getString(R.string.what_is_pass_desc)));
            selectPassByRouteDesc.setText(Html.fromHtml(getResources().getString(R.string.select_pass_for_route_desc)));
            noOfRideInPassDesc.setText(Html.fromHtml(getResources().getString(R.string.number_of_rides_in_each_pass)));
            usageOfRideDesc.setText(Html.fromHtml(getResources().getString(R.string.usage_of_rides)));

        }

    }

    private void initViews() {
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.passDescLayout));
        coLayoutWhatPass = findViewById(R.id.coLayoutWhatPass);
        passDescHeader = findViewById(R.id.passDescHeader);
        titleT = findViewById(R.id.titleT);
        passDescText = findViewById(R.id.passDescText);
        selectPassByRouteDesc = findViewById(R.id.selectPassByRouteDesc);
        noOfRideInPassDesc = findViewById(R.id.noOfRideInPassDesc);
        usageOfRideDesc = findViewById(R.id.usageOfRideDesc);
        bannerPassDesc = findViewById(R.id.bannerPassDesc);

        toolbar = findViewById(R.id.toolbar);
        whatIsPassLayout = findViewById(R.id.whatIsPassLayout);
        whatPassDesc = findViewById(R.id.whatPassDesc);
        whatIsPassButton = findViewById(R.id.whatIsPassButton);

        myPassLayout = findViewById(R.id.myPassLayout);
        totalPassCount = findViewById(R.id.totalPassCount);
        comPassCount = findViewById(R.id.comPassCount);
        comPassValidity = findViewById(R.id.comPassValidity);
        passPrice = findViewById(R.id.passPrice);

        USER_COMPLIMENT_PASS = SaveCache.getString(WhatPass.this, COMPLIMENTARY_PASS);
        USER_COMPLEMENT_PASS_VALIDITY = SaveCache.getString(WhatPass.this, COMPLIMENTARY_PASS_VALIDITY);
        USER_TOTAL_PASS = SaveCache.getString(WhatPass.this, TOTAL_PASS);
    }

    private void initListeners() {
        passDescHeader.setOnClickListener(WhatPass.this);

        // Capturing the callbacks for bottom sheet
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetState = newState;
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    titleT.setVisibility(View.VISIBLE);
//                    listIcon.setImageResource(R.drawable.up_arrow);
                } else {
                    titleT.setVisibility(View.GONE);
//                    listIcon.setImageResource(R.drawable.down_arrow);
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    /*case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;*/
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.passDescHeader:
                switch (sheetState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    default:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;
                }
            break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                WhatPass.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sheetState == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (sheetState == BottomSheetBehavior.STATE_COLLAPSED) {
//        startActivity(new Intent(EditProfile.this, Home.class));
            startActivity(new Intent(WhatPass.this, MapsActivity.class));
            WhatPass.this.finish();
        }
    }
}
