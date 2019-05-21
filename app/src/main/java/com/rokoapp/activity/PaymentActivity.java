package com.rokoapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rokoapp.R;
import com.rokoapp.listener.BookingListener;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.model.response.BookingData;
import com.rokoapp.model.response.TripHistorySubModel;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import org.json.JSONObject;

import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS;
import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.EMAIL_USER;
import static com.rokoapp.utils.ParamUtils.MOBILE_USER;
import static com.rokoapp.utils.ParamUtils.MY_COUPON_CODE;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;
import static com.rokoapp.utils.ParamUtils.TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_COMPLEMENT_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.USER_COMPLIMENT_PASS;
import static com.rokoapp.utils.ParamUtils.USER_TOTAL_PASS;

public class PaymentActivity extends Activity implements AppCompatCallback, PaymentResultListener, BookingListener, ResponseProgressListener, View.OnClickListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();

    private int passCost, cost;
    private String id, passName, description, imageUrl, noOfRides, complimentaryRide, validityOfComplimentaryRide, status;
    private TextView emailUserPass, passesCount, passesCountExtra, costOfPasses, validity, summaryPassPrice, totalPassPrice, termOfUse, refundPolicy;
    private TextView textCoupon;
    private Button buyButton;
    private LinearLayout layoutCoupon;
    private BookingData data = null;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);
//        setUpActionBar();
        delegate.setContentView(R.layout.activity_payment);

        initViews();

        if (toolbar != null) {
            delegate.setSupportActionBar(toolbar);
            delegate.getSupportActionBar().setTitle("Payment Summary");
            delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initListeners();

        Intent i = getIntent();
        id = i.getStringExtra("id");
        passName = i.getStringExtra("name");
        description = i.getStringExtra("description");
        imageUrl = i.getStringExtra("image_url");
        noOfRides = i.getStringExtra("no_of_rides");
        complimentaryRide = i.getStringExtra("complimentry_ride");
        validityOfComplimentaryRide = i.getStringExtra("validity_of_complimentry_ride");
        passCost = i.getIntExtra("cost",0);
        status = i.getStringExtra("status");

        showData();

        Checkout.preload(getApplicationContext());

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", passName);
            options.put("description", description);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.roko);
            options.put("currency", "INR");
            options.put("amount", cost*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", EMAIL_USER);
            preFill.put("contact", MOBILE_USER);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Log.e(TAG, "Payment Successful:============================================ " + razorpayPaymentID );
            ApiManager.getPostPayStatus(PaymentActivity.this, id, "SUCCESS", razorpayPaymentID, cost, PaymentActivity.this);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @Override
    public void onPaymentError(int code, String response) {
        try {
//            Toast.makeText(this, "Payment failed:" + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Payment failed: ===============================================" + code + " " + response );
            if (response.equals("Payment Cancelled")) {
                ApiManager.getPostPayStatus(PaymentActivity.this, id, "LAPSE", "", cost, PaymentActivity.this);
            }else {
                ApiManager.getPostPayStatus(PaymentActivity.this, id,  "FAILURE","", cost, PaymentActivity.this);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void showData() {
        emailUserPass.setText(EMAIL_USER);
        passesCount.setText(noOfRides+" Rides ");
        if (complimentaryRide == null || complimentaryRide.isEmpty() || complimentaryRide.equals("0")){
            passesCountExtra.setVisibility(View.GONE);
        }else {
           passesCountExtra.setVisibility(View.VISIBLE);
           passesCountExtra.setText("+ "+complimentaryRide+" Rides Extra");
        }
        cost = passCost*Integer.parseInt(noOfRides);
        costOfPasses.setText("₹ "+cost);
        if (validityOfComplimentaryRide != null){
            validity.setVisibility(View.VISIBLE);
            validity.setText("Valid for "+validityOfComplimentaryRide+" days");
        }else
            validity.setVisibility(View.GONE);

        summaryPassPrice.setText(cost+"");
        totalPassPrice.setText(cost+"");
        buyButton.setText("Pay ₹ "+cost);
    }

    private void initViews() {
        MY_COUPON_CODE = null;
        toolbar=  findViewById(R.id.toolbar);
        layoutCoupon=  findViewById(R.id.layoutCoupon);
        emailUserPass = findViewById(R.id.emailUserPass);
        passesCount = findViewById(R.id.passesCount);
        passesCountExtra = findViewById(R.id.passesCountExtra);
        costOfPasses = findViewById(R.id.costOfPasses);
        validity = findViewById(R.id.validity);
        summaryPassPrice = findViewById(R.id.summaryPassPrice);
        totalPassPrice = findViewById(R.id.totalPassPrice);
        buyButton = findViewById(R.id.buyButton);
        termOfUse = findViewById(R.id.termOfUse);
        refundPolicy = findViewById(R.id.refundPolicy);
        textCoupon = findViewById(R.id.textCoupon);
    }


    private void initListeners() {
        buyButton.setOnClickListener(PaymentActivity.this);
        termOfUse.setOnClickListener(PaymentActivity.this);
        refundPolicy.setOnClickListener(PaymentActivity.this);
        layoutCoupon.setOnClickListener(PaymentActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buyButton:
                startPayment();
                break;
            case R.id.layoutCoupon:
                Intent i = new Intent(PaymentActivity.this, ApplyCoupons.class);
                i.putExtra("from", "payment");
                startActivity(i);
//                    startActivity(new Intent(PaymentActivity.this, ));
                break;
            case R.id.termOfUse:

                break;
            case R.id.refundPolicy:

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MY_COUPON_CODE != null){
            textCoupon.setText(MY_COUPON_CODE);
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
//        startActivity(new Intent(Notifications.this, Home.class));
        startActivity(new Intent(PaymentActivity.this, MapsActivity.class));
        PaymentActivity.this.finish();
    }

    @Override
    public void getBookingData(BookingData data) {
        this.data = data;
    }

    @Override
    public void onResponseInProgress() { }

    @Override
    public void onResponseCompleted(int i) {
        switch (i){
            case 1:
                USER_COMPLIMENT_PASS = SaveCache.getString(PaymentActivity.this, COMPLIMENTARY_PASS);
                USER_COMPLEMENT_PASS_VALIDITY = SaveCache.getString(PaymentActivity.this, COMPLIMENTARY_PASS_VALIDITY);
                USER_TOTAL_PASS = SaveCache.getString(PaymentActivity.this, TOTAL_PASS);



                Intent j = new Intent(PaymentActivity.this, BookingConfirm.class);
                j.putExtra("originStation", data.getOrigin_station_name());
                startActivity(j);
                PaymentActivity.this.finish();

                /*Intent j = new Intent(PaymentActivity.this, TrackMyBusLive.class);
                j.putExtra("userRouteData", data);
                j.putExtra("virtual_Route_id",data.getVirtualRouteId());
                startActivity(j);*/
                break;
            case 2:
                if (ParamUtils.virtualRouteIdS !=null & ParamUtils.originStationIdS != null & ParamUtils.destStationIdS != null) {
                    ApiManager.getBookingOfUser(PaymentActivity.this, ParamUtils.virtualRouteIdS, ParamUtils.originStationIdS, ParamUtils.destStationIdS, ParamUtils.getCurrentDate(),  this, this);
                }
                break;
        }
        /*if (i==2){
            if (ParamUtils.virtualRouteIdS !=null & ParamUtils.originStationIdS != null & ParamUtils.destStationIdS != null) {
                ApiManager.getBookingOfUser(PaymentActivity.this, ParamUtils.virtualRouteIdS, ParamUtils.originStationIdS, ParamUtils.destStationIdS, ParamUtils.getCurrentDate(),  this, this);
            }
        }
        if (i==1){
            USER_COMPLIMENT_PASS = SaveCache.getString(PaymentActivity.this, COMPLIMENTARY_PASS);
            USER_COMPLEMENT_PASS_VALIDITY = SaveCache.getString(PaymentActivity.this, COMPLIMENTARY_PASS_VALIDITY);
            USER_TOTAL_PASS = SaveCache.getString(PaymentActivity.this, TOTAL_PASS);

            Intent j = new Intent(PaymentActivity.this, TrackMyBusLive.class);
            j.putExtra("userRouteData", data);
            j.putExtra("virtual_Route_id",data.getVirtualRouteId());
            startActivity(j);
        }*/
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SIGNATURE_EXPIRE:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
//                startActivity(new Intent(MapsActivity.this, Login.class));
                startActivity(new Intent(PaymentActivity.this, MobileVerification.class));
                PaymentActivity.this.finish();
                break;
        }
    }

    @Override
    public void onSupportActionModeStarted(ActionMode actionMode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode actionMode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
