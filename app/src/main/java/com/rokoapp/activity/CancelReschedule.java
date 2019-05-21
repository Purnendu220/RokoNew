package com.rokoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import static com.rokoapp.utils.ParamUtils.PREFS_NAME;

public class CancelReschedule extends AppCompatActivity implements View.OnClickListener, ResponseProgressListener {

    private Toolbar toolbar;
    private RelativeLayout busRunLate, mightLateForBus, otherModeCommute;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reschedule);
        initViews();
        initListeners();

        bookingId = SaveCache.getString(CancelReschedule.this, ParamUtils.bookingId);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Cancel My Ride");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        busRunLate = findViewById(R.id.busRunLate);
        mightLateForBus = findViewById(R.id.mightLateForBus);
        otherModeCommute = findViewById(R.id.otherModeCommute);
    }

    private void initListeners() {
        busRunLate.setOnClickListener(CancelReschedule.this);
        mightLateForBus.setOnClickListener(CancelReschedule.this);
        otherModeCommute.setOnClickListener(CancelReschedule.this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.busRunLate:
               if (bookingId != null & !bookingId.isEmpty() & !bookingId.equals("")){
                   ApiManager.getBookingCancel(CancelReschedule.this, bookingId, "Bus is running late.", CancelReschedule.this);
               }
           break;
           case R.id.mightLateForBus:
               if (bookingId != null & !bookingId.isEmpty() & !bookingId.equals("")){
                   ApiManager.getBookingCancel(CancelReschedule.this, bookingId, "I might be late for the bus.", CancelReschedule.this);
               }
           break;
           case R.id.otherModeCommute:
               if (bookingId != null & !bookingId.isEmpty() & !bookingId.equals("")){
                   ApiManager.getBookingCancel(CancelReschedule.this, bookingId, "Using some other mode of commute.", CancelReschedule.this);
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
//        startActivity(new Intent(CancelReschedule.this, MapsActivity.class));
        CancelReschedule.this.finish();
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            Log.d("", "onResponseCompleted: booking cancelled.");
            startActivity(new Intent(CancelReschedule.this, MapsActivity.class));
            CancelReschedule.this.finish();
            SaveCache.save(CancelReschedule.this, ParamUtils.lastVirtualRouteId, null);
            SaveCache.save(CancelReschedule.this, ParamUtils.lastOriginLat, (float) 0);
            SaveCache.save(CancelReschedule.this, ParamUtils.lastOriginLong, (float) 0);
            SaveCache.save(CancelReschedule.this, ParamUtils.lastDestLat, (float) 0);
            SaveCache.save(CancelReschedule.this, ParamUtils.lastDestLong, (float) 0);
            SaveCache.save(CancelReschedule.this, ParamUtils.lastOriginName, null);
            SaveCache.save(CancelReschedule.this, ParamUtils.lastDestName, null);
            SaveCache.save(CancelReschedule.this, ParamUtils.bookingId, null);
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SERVER_ERROR:
//                Toast.makeText(this, "Server Busy, Try Again Later.", Toast.LENGTH_SHORT).show();
                break;
            case NO_INTERNET:
                Toast.makeText(this, "No internet ", Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_NULL:
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                Toast.makeText(this, "Unable to connect; please Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_0:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case SIGNATURE_EXPIRE:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
//                startActivity(new Intent(MapsActivity.this, Login.class));
                startActivity(new Intent(CancelReschedule.this, MobileVerification.class));
                CancelReschedule.this.finish();
                break;
        }
    }
}
