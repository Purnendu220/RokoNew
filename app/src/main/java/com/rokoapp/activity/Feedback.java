package com.rokoapp.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.rokoapp.R;

public class Feedback extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private RatingBar rateBar;
    private EditText editTextFeedBack;
    private Button submitRating;
    private LinearLayout paramLayout, lateArrivalIssue, busNotClean, issueWithApp, routeIssue, notFriendlyStaff, rudeDriver, acIssue, otherIssue;
    private boolean flagLateArrival = false;
    private boolean flagBusNotClean = false;
    private boolean flagAppIssue = false;
    private boolean flagRouteIssue = false;
    private boolean flagFriendlyStaff = false;
    private boolean flagRudeDriver = false;
    private boolean flagAcIssue = false;
    private boolean flagOtherIssue = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
        initListeners();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Send Feedback");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rateBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> setCurrentRating(rating, rateBar));
    }

    private void submitRatingToServer() {
        if (String.valueOf(rateBar.getRating()).equals("0")){
            rateBar.requestFocus();
            Toast.makeText(Feedback.this, "Kindly select Star Rating", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setCurrentRating(float rating, RatingBar rateBar) {
        LayerDrawable drawable = (LayerDrawable) rateBar.getProgressDrawable();
        switch (Math.round(rating)) {
            case 1:
                setRatingStarColor(drawable.getDrawable(2), ContextCompat.getColor(Feedback.this, R.color.red));
                paramLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                setRatingStarColor(drawable.getDrawable(2), ContextCompat.getColor(Feedback.this, R.color.yellow));
                paramLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                setRatingStarColor(drawable.getDrawable(2), ContextCompat.getColor(Feedback.this, R.color.light_yellow));
                paramLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                setRatingStarColor(drawable.getDrawable(2), ContextCompat.getColor(Feedback.this, R.color.light_green));
                paramLayout.setVisibility(View.GONE);
                break;
            case 5:
                setRatingStarColor(drawable.getDrawable(2), ContextCompat.getColor(Feedback.this, R.color.dark_green));
                paramLayout.setVisibility(View.GONE);
                break;
        }
        setRatingStarColor(drawable.getDrawable(1), ContextCompat.getColor(Feedback.this, android.R.color.transparent));
        setRatingStarColor(drawable.getDrawable(0), ContextCompat.getColor(Feedback.this, R.color.light_gray));
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rateBar = findViewById(R.id.rateBar);
        editTextFeedBack = findViewById(R.id.editTextFeedBack);
        submitRating = findViewById(R.id.submitRating);
        paramLayout = findViewById(R.id.paramLayout);
        lateArrivalIssue = findViewById(R.id.lateArrivalIssue);
        busNotClean = findViewById(R.id.busNotClean);
        issueWithApp = findViewById(R.id.issueWithApp);
        routeIssue = findViewById(R.id.routeIssue);
        notFriendlyStaff = findViewById(R.id.notFriendlyStaff);
        rudeDriver = findViewById(R.id.rudeDriver);
        acIssue = findViewById(R.id.acIssue);
        otherIssue = findViewById(R.id.otherIssue);
    }

    private void initListeners() {
        submitRating.setOnClickListener(Feedback.this);
        lateArrivalIssue.setOnClickListener(Feedback.this);
        busNotClean.setOnClickListener(Feedback.this);
        issueWithApp.setOnClickListener(Feedback.this);
        routeIssue.setOnClickListener(Feedback.this);
        notFriendlyStaff.setOnClickListener(Feedback.this);
        rudeDriver.setOnClickListener(Feedback.this);
        acIssue.setOnClickListener(Feedback.this);
        otherIssue.setOnClickListener(Feedback.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitRating:
                submitRatingToServer();
            break;
            case R.id.lateArrivalIssue:
                if (!flagLateArrival){
                    flagLateArrival = true;
                    lateArrivalIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagLateArrival = false;
                    lateArrivalIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
                break;
            case R.id.busNotClean:
                if (!flagBusNotClean){
                    flagBusNotClean = true;
                    busNotClean.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagBusNotClean = false;
                    busNotClean.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
            case R.id.issueWithApp:
                if (!flagAppIssue){
                    flagAppIssue = true;
                    issueWithApp.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagAppIssue = false;
                    issueWithApp.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
            case R.id.routeIssue:
                if (!flagRouteIssue){
                    flagRouteIssue = true;
                    routeIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagRouteIssue = false;
                    routeIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
            case R.id.notFriendlyStaff:
                if (!flagFriendlyStaff){
                    flagFriendlyStaff = true;
                    notFriendlyStaff.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagFriendlyStaff = false;
                    notFriendlyStaff.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
            case R.id.rudeDriver:
                if (!flagRudeDriver){
                    flagRudeDriver = true;
                    rudeDriver.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagRudeDriver = false;
                    rudeDriver.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
            case R.id.acIssue:
                if (!flagAcIssue){
                    flagAcIssue = true;
                    acIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagAcIssue = false;
                    acIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
            case R.id.otherIssue:
                if (!flagOtherIssue){
                    flagOtherIssue = true;
                    otherIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_pay));
                }else {
                    flagOtherIssue = false;
                    otherIssue.setBackground(getResources().getDrawable(R.drawable.button_rectangle_yellow_outline));
                }
            break;
        }
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
//        startActivity(new Intent(EditProfile.this, Home.class));
        startActivity(new Intent(Feedback.this, MapsActivity.class));
        Feedback.this.finish();
    }
}
