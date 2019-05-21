package com.rokoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.listener.LoginListener;
import com.rokoapp.R;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS;
import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.ID_AUTH;
import static com.rokoapp.utils.ParamUtils.ID_USER;
import static com.rokoapp.utils.ParamUtils.MOBILE_USER;
import static com.rokoapp.utils.ParamUtils.EMAIL_USER;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;
import static com.rokoapp.utils.ParamUtils.TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_COMPLEMENT_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.USER_COMPLIMENT_PASS;
import static com.rokoapp.utils.ParamUtils.USER_TOTAL_PASS;

public class Login extends AppCompatActivity implements View.OnClickListener, ResponseProgressListener, LoginListener {

    private EditText emailSignIn, passwordSignIn;
    private TextView loginButton, linkSignUp, forgotPassword;

    private String userId, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListener();

        ID_AUTH = SaveCache.getString(Login.this,"Authorization");
        ID_USER = SaveCache.getString(Login.this,"UserId");
        MOBILE_USER = SaveCache.getString(Login.this, "mobile");
        EMAIL_USER = SaveCache.getString(Login.this, "email");
        USER_COMPLIMENT_PASS = SaveCache.getString(Login.this, COMPLIMENTARY_PASS);
        USER_COMPLEMENT_PASS_VALIDITY = SaveCache.getString(Login.this, COMPLIMENTARY_PASS_VALIDITY);
        USER_TOTAL_PASS = SaveCache.getString(Login.this, TOTAL_PASS);


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        if(hasLoggedIn)
        {
            navigateToHomeActivity();
        }
    }

    private void initViews() {
        emailSignIn = findViewById(R.id.emailSignIn);
        passwordSignIn=findViewById(R.id.passwordSignIn);
        loginButton =findViewById(R.id.loginButton);
        linkSignUp=findViewById(R.id.linkSignUp);
        forgotPassword=findViewById(R.id.forgotPassword);
    }

    private void initListener() {
        loginButton.setOnClickListener(Login.this);
        linkSignUp.setOnClickListener(Login.this);
        forgotPassword.setOnClickListener(Login.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                verifyUser();
                break;
            case R.id.linkSignUp:
                Intent j = new Intent(Login.this, SignUp.class);
                j.putExtra("act","1");
                startActivity(j);
                Login.this.finish();
                break;
            case R.id.forgotPassword:
                /*Intent i = new Intent(Login.this, ForgotPassword.class);
                i.putExtra("email", emailSignIn.getText().toString().trim());
                startActivity(i);
                Login.this.finish();*/
                break;
        }
    }

    private void verifyUser() {
        if (emailSignIn.getText().toString().trim().equals("") && emailSignIn.getText().toString().trim().isEmpty()){
            emailSignIn.requestFocus();
            emailSignIn.setError("Email/Mobile shouldn't be empty.");
            return;
        }
        if (ParamUtils.isDigits(emailSignIn.getText().toString().trim())){
            if (emailSignIn.getText().toString().trim().length() < 10 || emailSignIn.getText().toString().trim().length() > 10) {
                emailSignIn.requestFocus();
                emailSignIn.setError("Mobile should have valid 10 digit number.");
                return;
            }
        }else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailSignIn.getText().toString().trim()).matches()){
                emailSignIn.requestFocus();
                emailSignIn.setError("Email should be valid.");
                return;
            }
        }
        if (passwordSignIn.getText().toString().trim().isEmpty() && passwordSignIn.getText().toString().trim().equals("")){
            passwordSignIn.requestFocus();
            passwordSignIn.setError("Password shouldn't be empty.");
            return;
        }
        if (passwordSignIn.getText().toString().trim() != null) {
            final String email = emailSignIn.getText().toString().trim();
            final String password = passwordSignIn.getText().toString();

            ApiManager.getParentLogin(Login.this, email, password, Login.this, Login.this);
        }
    }

    private void emptyInputEditText() {
//        emailSignIn.setText(null);
        passwordSignIn.setText(null);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();
        navigateToHomeActivity();
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            emptyInputEditText();
//            ID_USER = SaveCache.getString(Login.this,"UserId");
            Toast.makeText(Login.this,"Welcome, "+userName, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SERVER_ERROR:
                Toast.makeText(this, "Server Busy, Try Again Later.", Toast.LENGTH_SHORT).show();
                break;
            case NO_INTERNET:
                Toast.makeText(this, "No internet ", Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_NULL:
                Toast.makeText(this, "Unable to Login; please Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_0:
                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                break;
        }
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


    private void navigateToHomeActivity() {
//        Intent j = new Intent(Login.this, Home.class);
        Intent j = new Intent(Login.this, MapsActivity.class);
        startActivity(j);
        Login.this.finish();
    }
}
