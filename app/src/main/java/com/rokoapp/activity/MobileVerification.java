package com.rokoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.listener.LoginListener;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.utils.InputValidation;
import com.rokoapp.utils.SaveCache;

import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS;
import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.EMAIL_USER;
import static com.rokoapp.utils.ParamUtils.ID_AUTH;
import static com.rokoapp.utils.ParamUtils.ID_USER;
import static com.rokoapp.utils.ParamUtils.MOBILE_USER;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;
import static com.rokoapp.utils.ParamUtils.TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_COMPLEMENT_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.USER_COMPLIMENT_PASS;
import static com.rokoapp.utils.ParamUtils.USER_TOTAL_PASS;

public class MobileVerification extends AppCompatActivity implements ResponseProgressListener, LoginListener {

    private RelativeLayout mobileEnterScreen, OtpVerifyScreen;

    private TextInputLayout mobileVerifyLayout, otpVerifyLayout;
    private TextInputEditText mobileVerifyEditText, otpVerifyEditText;
    private TextView otpVerifyButton, otpConfirmButton, resendOtp;
    private ImageView backButton;

    private InputValidation inputValidation;
    private String mNum= null, userName, userId;
    private int checkFlag = 0;

    private final AppCompatActivity activity = MobileVerification.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        initViews();

        ID_AUTH = SaveCache.getString(MobileVerification.this,"Authorization");
        ID_USER = SaveCache.getString(MobileVerification.this,"UserId");
        MOBILE_USER = SaveCache.getString(MobileVerification.this, "mobile");
        EMAIL_USER = SaveCache.getString(MobileVerification.this, "email");
        USER_COMPLIMENT_PASS = SaveCache.getString(MobileVerification.this, COMPLIMENTARY_PASS);
        USER_COMPLEMENT_PASS_VALIDITY = SaveCache.getString(MobileVerification.this, COMPLIMENTARY_PASS_VALIDITY);
        USER_TOTAL_PASS = SaveCache.getString(MobileVerification.this, TOTAL_PASS);


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        if(hasLoggedIn)
        {
            navigateToHomeActivity();
        }

        otpVerifyButton.setOnClickListener( view -> {
            verifyToSendOtp();
        });

        otpConfirmButton.setOnClickListener( view -> {
            confirmOtp();
        });

        resendOtp.setOnClickListener( view -> {

        });

        backButton.setOnClickListener( view -> {
            mobileEnterScreen.setVisibility(View.VISIBLE);
            OtpVerifyScreen.setVisibility(View.GONE);
            otpVerifyEditText.setText(null);
        });

        mobileVerifyEditText.setFocusableInTouchMode(false);
        mobileVerifyEditText.setFocusable(false);

        mobileVerifyEditText.setOnClickListener(v->{
            mobileVerifyEditText.setFocusableInTouchMode(true);
            mobileVerifyEditText.setFocusable(true);
        });
        
        sendSms();
    }



    public static String generateOTP() {
        int randomPin   =(int)(Math.random()*900000)+100000;
        return String.valueOf(randomPin);
    }

    private void sendSms() {
    }

    private void navigateToHomeActivity() {
//        Intent j = new Intent(Login.this, Home.class);
        Intent j = new Intent(MobileVerification.this, MapsActivity.class);
        startActivity(j);
        MobileVerification.this.finish();
    }

    private void confirmOtp() {
        if (!inputValidation.isInputEditTextFilled(otpVerifyEditText, otpVerifyLayout, getString(R.string.error_message_otp_empty))) {
            return;
        }
        if (otpVerifyEditText !=null){
            if (otpVerifyEditText.getText().toString().equals("123456")){
                Toast.makeText(MobileVerification.this,"Successfully Verified.", Toast.LENGTH_LONG).show();
                if (checkFlag==1){
                    Intent in = new Intent(MobileVerification.this, SignUpNew.class);
                    if (mNum != null) {
                        in.putExtra("mobile", mNum);
                    }
                    startActivity(in);
                    MobileVerification.this.finish();
                }else if (checkFlag==2) {
                    ApiManager.getParentLogin(MobileVerification.this, mNum, "roko@123", MobileVerification.this, MobileVerification.this);

                   /* Intent in = new Intent(MobileVerification.this, Login.class);
                    if (mNum != null) {
                        in.putExtra("mobile", mNum);
                    }
                    startActivity(in);*/
                }

            }
        }
    }

    private void verifyToSendOtp() {
        if (!inputValidation.isInputEditTextFilled(mobileVerifyEditText, mobileVerifyLayout, getString(R.string.error_message_mobile_empty))) {
            return;
        }
        if (!inputValidation.isValidPhoneNumber(mobileVerifyEditText, mobileVerifyLayout, getString(R.string.error_message_mobile_not_valid))) {
            return;
        }
        if (mobileVerifyEditText != null){
            mNum = mobileVerifyEditText.getText().toString().trim();
            ApiManager.getMobileVerified(MobileVerification.this, mNum,MobileVerification.this);
        }
    }

    private void initViews() {
        mobileEnterScreen = findViewById(R.id.mobileEnterScreen);
        OtpVerifyScreen = findViewById(R.id.OtpVerifyScreen);
        mobileVerifyLayout = findViewById(R.id.mobileVerifyLayout);
        mobileVerifyEditText = findViewById(R.id.mobileVerifyEditText);
        otpVerifyButton = findViewById(R.id.otpVerifyButton);

        otpVerifyLayout = findViewById(R.id.otpVerifyLayout);
        otpVerifyEditText = findViewById(R.id.otpVerifyEditText);
        otpConfirmButton = findViewById(R.id.otpConfirmButton);
        resendOtp = findViewById(R.id.resendOtp);

        backButton = findViewById(R.id.backButton);

        inputValidation = new InputValidation(activity);
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        mobileEnterScreen.setVisibility(View.GONE);
        OtpVerifyScreen.setVisibility(View.VISIBLE);
        switch (i){
            case 1:
                checkFlag = 1;
                
                break;
            case 2:
                checkFlag = 2;
                break;
            case 3:
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", true);
                editor.commit();
                navigateToHomeActivity();
                Toast.makeText(MobileVerification.this, "Welcome, "+userName, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {

    }

    @Override
    public void getUserId(String userId) {
        this.userId = userId;
        ID_USER = userId;
    }

    @Override
    public void getUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void getMobile(String mobile) {
        MOBILE_USER = mobile;
    }

    @Override
    public void getEmail(String email) {
        EMAIL_USER = email;
    }

}
