package com.rokoapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.fragment.CurrentRide;
import com.rokoapp.fragment.PreviousTrips;


public class TripHistory extends AppCompatActivity implements View.OnClickListener /*ResponseProgressListener*/ {

    private Toolbar toolbar;
    private RelativeLayout rlUpcoming, rlCurrent, rlPrevious;
    private TextView tvUpcoming, tvCurrent, tvPrevious;
    private View upcomingView,currentView, previousView;
//    private RecyclerView recyclerTripHistory;

//    private List<TripHistorySubModel> tripList = new ArrayList<>();
//    private TripHistoryAdapter tripAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        initViews();
        initListeners();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Trips History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        selectUpcomingTrips();
        selectCurrentTrips();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarTrip);
        rlUpcoming = findViewById(R.id.rlUpcoming);
        rlCurrent = findViewById(R.id.rlCurrent);
        rlPrevious = findViewById(R.id.rlPrevious);
        tvUpcoming = findViewById(R.id.tvUpcoming);
        tvCurrent = findViewById(R.id.tvCurrent);
        tvPrevious = findViewById(R.id.tvPrevious);
        upcomingView = findViewById(R.id.upcomingView);
        currentView = findViewById(R.id.currentView);
        previousView = findViewById(R.id.previousView);
//        recyclerTripHistory = findViewById(R.id.recyclerTripHistory);
    }

    private void initListeners() {
        rlUpcoming.setOnClickListener(this);
        rlCurrent.setOnClickListener(this);
        rlPrevious.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.rlUpcoming:
                selectUpcomingTrips();
                break;*/
            case R.id.rlCurrent:
                selectCurrentTrips();
                break;
            case R.id.rlPrevious:
                selectPreviousTrips();
                break;
            default:
                selectCurrentTrips();
//                selectUpcomingTrips();
                break;
        }
    }

    /*private void selectUpcomingTrips() {
        upcomingView.setVisibility(View.VISIBLE);
        currentView.setVisibility(View.GONE);
        previousView.setVisibility(View.GONE);
        tvUpcoming.setTextColor(getResources().getColor(R.color.yellow));
        tvCurrent.setTextColor(getResources().getColor(R.color.white));
        tvPrevious.setTextColor(getResources().getColor(R.color.white));
        UpcomingRides upcomingRides = new UpcomingRides();
        *//*Bundle bundle = new Bundle();
        bundle.putString("id", id);
        upcomingRides.setArguments(bundle);*//*
        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
        transactions.replace(R.id.frameTrips, upcomingRides);
        transactions.commit();
    }*/

    private void selectCurrentTrips() {
        upcomingView.setVisibility(View.GONE);
        currentView.setVisibility(View.VISIBLE);
        previousView.setVisibility(View.GONE);
        tvUpcoming.setTextColor(getResources().getColor(R.color.white));
        tvCurrent.setTextColor(getResources().getColor(R.color.yellow));
        tvPrevious.setTextColor(getResources().getColor(R.color.white));
        CurrentRide currentRide = new CurrentRide();
        /*Bundle bundle = new Bundle();
        bundle.putString("id", id);
        currentRide.setArguments(bundle);*/
        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
        transactions.replace(R.id.frameTrips, currentRide);
        transactions.commit();
    }

    private void selectPreviousTrips() {
        upcomingView.setVisibility(View.GONE);
        currentView.setVisibility(View.GONE);
        previousView.setVisibility(View.VISIBLE);
        tvUpcoming.setTextColor(getResources().getColor(R.color.white));
        tvCurrent.setTextColor(getResources().getColor(R.color.white));
        tvPrevious.setTextColor(getResources().getColor(R.color.yellow));
        PreviousTrips previousTrips = new PreviousTrips();
        /*Bundle bundle = new Bundle();
        bundle.putString("id", id);
        previousTrips.setArguments(bundle);*/
        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
        transactions.replace(R.id.frameTrips, previousTrips);
        transactions.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(EditProfile.this, Home.class));
        startActivity(new Intent(TripHistory.this, MapsActivity.class));
        TripHistory.this.finish();
    }

    /*@Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            tripAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch(code){
             case SIGNATURE_EXPIRE:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
//                startActivity(new Intent(MapsActivity.this, Login.class));
                startActivity(new Intent(TripHistory.this, MobileVerification.class));
                TripHistory.this.finish();
                break;
        }
    }*/
}
