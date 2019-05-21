package com.rokoapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rokoapp.R;
import com.rokoapp.activity.PassesList;
import com.rokoapp.activity.PaymentActivity;
import com.rokoapp.model.response.PassModel;

import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.rokoapp.utils.ParamUtils.EMAIL_USER;

public class PassesAdapter extends RecyclerView.Adapter<PassesAdapter.MyViewHolder> implements PaymentResultListener /*PaytmPaymentTransactionCallback, ResponseProgressListener, ChecksumListener*/ {

    private Context mContext;
    private List<PassModel> passModelList;
    private PassesList passesList;
    /*private String checksumHash;
    private String txnAmount;
    private ChecksumResponse response;*/

    public PassesAdapter(Context mContext, List<PassModel> passModelList, PassesList passesList) {
        this.mContext = mContext;
        this.passModelList = passModelList;
        this.passesList = passesList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        /*private ImageView passImg;
        private TextView passName, costPasses, descPass, totalRides, bonusRides, complimentaryValidity;
        private RelativeLayout passLayout;*/

        private LinearLayout pass_view;
        private TextView expireTag, passRidesCount, passRidesValidity, offeredRide, perRidePrize;

        MyViewHolder(View itemView) {
            super(itemView);
            pass_view = itemView.findViewById(R.id.pass_view);
            expireTag = itemView.findViewById(R.id.expireTag);
            passRidesCount = itemView.findViewById(R.id.passRidesCount);
            passRidesValidity = itemView.findViewById(R.id.passRidesValidity);
            offeredRide = itemView.findViewById(R.id.offeredRide);
            perRidePrize = itemView.findViewById(R.id.perRidePrize);
           /* passImg = itemView.findViewById(R.id.passImg);
            passName = itemView.findViewById(R.id.passName);
            costPasses = itemView.findViewById(R.id.costPasses);
            descPass = itemView.findViewById(R.id.descPass);
            totalRides = itemView.findViewById(R.id.totalRides);
            bonusRides = itemView.findViewById(R.id.bonusRides);
            complimentaryValidity = itemView.findViewById(R.id.complimentaryValidity);
            passLayout = itemView.findViewById(R.id.passLayout);*/

            expireTag.setVisibility(View.GONE);
            offeredRide.setVisibility(View.GONE);
        }

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passes_items, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.roko_pass_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PassModel passModel = passModelList.get(position);

        String cost = String.valueOf(passModel.getCost()).split("\\.")[0];
        holder.passRidesCount.setText(passModel.getNo_of_rides()+" Rides");
        holder.passRidesValidity.setText("Validity "+passModel.getValidUpTo()+" days");
        if (passModel.getComplimentary_ride() != null & !passModel.getComplimentary_ride().isEmpty() & !passModel.getComplimentary_ride().equals("0")){
           holder.offeredRide.setVisibility(View.VISIBLE);
           holder.offeredRide.setText("Extra +"+passModel.getComplimentary_ride()+" Rides");
        }else
            holder.offeredRide.setVisibility(View.GONE);

        holder.perRidePrize.setText("₹ "+cost+"/ Ride");

        holder.pass_view.setOnClickListener(v -> {
            if (passModel.getStatus()){
//                startPayment(passModel.getCost(), passModel.getName(), passModel.getDescription(), passModel.getImage_url());
//                ApiManager.getChecksumKey(mContext, passModel.getCost(), this, this);
                Intent i = new Intent(mContext, PaymentActivity.class);
                i.putExtra("id",passModel.getId());
                i.putExtra("name", passModel.getName());
                i.putExtra("description", passModel.getDescription());
                i.putExtra("image_url", passModel.getImage_url());
                i.putExtra("no_of_rides", passModel.getNo_of_rides());
                i.putExtra("complimentry_ride", passModel.getComplimentary_ride());
                i.putExtra("validity_of_complimentry_ride", passModel.getValidUpTo());
                i.putExtra("cost", passModel.getCost());
                i.putExtra("status", passModel.getStatus());
                mContext.startActivity(i);
                passesList.finish();
            }else {
                Log.d(TAG, "onBindViewHolder: doNothing");
                Toast.makeText(mContext,"You have already bought this.", Toast.LENGTH_LONG).show();
            }
        });


        /*holder.passName.setText(passModel.getName());
        holder.descPass.setText(passModel.getDescription());

        String cost = String.valueOf(passModel.getCost()).split("\\.")[0];
        holder.costPasses.setText("In just Rs. "+cost+" only");
        holder.totalRides.setText(passModel.getNo_of_rides()+" Rides ");
        holder.bonusRides.setText("+ "+passModel.getComplimentary_ride()+" Rides Extra");

        if (passModel.getImage_url() !=null){
            Glide.with(mContext).load(passModel.getImage_url()).into(holder.passImg);
        }

        if (passModel.getValidUpTo() != null){
            holder.complimentaryValidity.setText("*Extra rides valid upto "+passModel.getValidUpTo()+" days, till the buy date.");
        }


            To ensure faster loading of the Checkout form,
            call this method as early as possible in your checkout flow.

//        Checkout.preload(mContext);

        holder.passLayout.setOnClickListener( v ->{
            Log.d(TAG, "onBindViewHolder: ");
            if (passModel.getStatus()){
//                startPayment(passModel.getCost(), passModel.getName(), passModel.getDescription(), passModel.getImage_url());
//                ApiManager.getChecksumKey(mContext, passModel.getCost(), this, this);
                Intent i = new Intent(mContext, PaymentActivity.class);
                i.putExtra("id",passModel.getId());
                i.putExtra("name", passModel.getName());
                i.putExtra("description", passModel.getDescription());
                i.putExtra("image_url", passModel.getImage_url());
                i.putExtra("no_of_rides", passModel.getNo_of_rides());
                i.putExtra("complimentry_ride", passModel.getComplimentary_ride());
                i.putExtra("validity_of_complimentry_ride", passModel.getValidUpTo());
                i.putExtra("cost", passModel.getCost());
                i.putExtra("status", passModel.getStatus());
                mContext.startActivity(i);
                passesList.finish();
            }else {
                Log.d(TAG, "onBindViewHolder: doNothing");
                Toast.makeText(mContext,"You have already bought this.", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void startPayment(int cost, String name, String description, String image_url) {
        /* You need to pass current activity in order to let Razorpay create CheckoutActivity  */
        final Activity activity = ((PassesList)mContext);
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", description);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", image_url);
            options.put("currency", "INR");
            options.put("amount",  cost*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", EMAIL_USER);
            preFill.put("contact", /*MOBILE_USER*/"8377052474");

            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

   /* *//**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(mContext, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public int getItemCount() {
        if (passModelList != null) {
            return passModelList.size();
        } else return 0;
    }

    /*@Override
    public void getChecksumHash(String checksumHash) {
        this.checksumHash = checksumHash;
    }

    @Override
    public void getTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    @Override
    public void getCheckSumObject(ChecksumResponse response) {
        this.response = response;
    }

    @Override
    public void onResponseInProgress() { }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            initializePaytmPayment();
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {

    }

    private void initializePaytmPayment() {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
//        PaytmPGService Service = PaytmPGService.getProductionService();

        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , Constants.M_ID);
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , response.getOrderId());
        paramMap.put( "CUST_ID" , ID_USER);
        paramMap.put( "MOBILE_NO" , MOBILE_USER);
        paramMap.put( "EMAIL" , EMAIL_USER);
        paramMap.put( "CHANNEL_ID" , Constants.CHANNEL_ID);
        paramMap.put( "TXN_AMOUNT" , response.getTxnAmount());
        paramMap.put( "WEBSITE" , response.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , Constants.INDUSTRY_TYPE_ID);
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", Constants.CALLBACK_URL);
        paramMap.put( "CHECKSUMHASH" , response.getChecksumHash());
        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(mContext, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        navigateToResult(bundle.toString());
        Toast.makeText(mContext, bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        navigateToResult("Network error");
        Toast.makeText(mContext, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        navigateToResult(s);
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        navigateToResult(s);
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        navigateToResult(s);
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        navigateToResult("Back Pressed");
        Toast.makeText(mContext, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        navigateToResult(bundle.toString());
        Toast.makeText(mContext, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }*/

    private void navigateToResult(String result){
        Intent i = new Intent(mContext, PaymentActivity.class);
        i.putExtra("result", result);
        mContext.startActivity(i);
    }
}
