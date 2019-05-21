package com.rokoapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.listener.LoginListener;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.utils.InputValidation;
import com.rokoapp.utils.ParamUtils;

import java.util.Calendar;

import static com.rokoapp.utils.ParamUtils.EMAIL_USER;
import static com.rokoapp.utils.ParamUtils.ID_USER;
import static com.rokoapp.utils.ParamUtils.MOBILE_USER;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;

public class SignUpNew extends AppCompatActivity implements ResponseProgressListener, LoginListener {

    private LinearLayout dobSelect;
    private TextInputLayout fullNameInputLayout, emailTextInputLayout;
    private TextInputEditText fullName, emailSignUp, referSignUp;
    private ImageView backButton;
    private TextView submitSignUp, dobText, welcomeTextSignUp;

    private String mobile, userName, dobSt;
    private InputValidation inputValidation;

    private final AppCompatActivity activity = SignUpNew.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_new);
        initViews();

        mobile = getIntent().getStringExtra("mobile");

        if (mobile != null && !mobile.isEmpty()){
            welcomeTextSignUp.setText("Enter more details to create your account with "+mobile);
        }

        submitSignUp.setOnClickListener( view -> verifySignUp());

        dobSelect.setOnClickListener( view -> selectDate());

        backButton.setOnClickListener(view -> onBackPressed());

    }

    private void verifySignUp() {
        if (!inputValidation.isInputEditTextFilled(fullName, fullNameInputLayout, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(emailSignUp, emailTextInputLayout, getString(R.string.error_message_email_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(emailSignUp, emailTextInputLayout, getString(R.string.error_message_email_valid))) {
            return;
        }

        /*if (fullName.getText().toString().trim().isEmpty() && fullName.getText().toString().trim().equals("")) {
            fullName.requestFocus();
            fullName.setError("Name shouldn't be empty.");
            return;
        }
        if (emailSignUp.getText().toString().trim().isEmpty() && emailSignUp.getText().toString().trim().equals("")) {
            emailSignUp.requestFocus();
            emailSignUp.setError("Email shouldn't be empty.");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailSignUp.getText().toString().trim()).matches()) {
            emailSignUp.requestFocus();
            emailSignUp.setError("Email should be valid.");
            return;
        }*/
        if (fullName.getText() != null & emailSignUp.getText() != null) {
            final String name = fullName.getText().toString().trim();
            final String email = emailSignUp.getText().toString().trim();

            if (referSignUp.getText() != null) {
                final String referCode = referSignUp.getText().toString().trim();
            }

            ApiManager.getParentSignUp(SignUpNew.this, name, mobile, email, "roko@123", "male", dobSt,"","","","","","", SignUpNew.this);
        }
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);

        fullNameInputLayout = findViewById(R.id.fullNameInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        fullName = findViewById(R.id.fullName);
        emailSignUp = findViewById(R.id.emailSignUp);
        referSignUp = findViewById(R.id.referSignUp);
        welcomeTextSignUp = findViewById(R.id.welcomeTextSignUp);
        submitSignUp = findViewById(R.id.submitSignUp);
        dobSelect = findViewById(R.id.dobSelect);
        dobText = findViewById(R.id.dobText);

        inputValidation = new InputValidation(activity);
    }

    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpNew.this, R.style.MyDialogTheme,
                (view, year1, monthOfYear, dayOfMonth) -> {
//                    dobText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    dobSt = (year1 +"-"+ (monthOfYear + 1) + "-"+dayOfMonth);
                    dobText.setText(ParamUtils.changeBirthDateFormat(dobSt));
                    dobText.setTextColor(getResources().getColor(R.color.black));
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        switch (i){
            case 1:
//                Toast.makeText(SignUpNew.this ,"You have Successfully Registered.", Toast.LENGTH_LONG).show();
                ApiManager.getParentLogin(SignUpNew.this, mobile, "roko@123", SignUpNew.this, SignUpNew.this);
                break;
            case 3:
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", true);
                editor.commit();
                navigateToHomeActivity();
                Toast.makeText(SignUpNew.this, "Welcome, "+userName, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void navigateToHomeActivity() {
//        Intent j = new Intent(Login.this, Home.class);
        Intent j = new Intent(SignUpNew.this, MapsActivity.class);
        startActivity(j);
        SignUpNew.this.finish();
    }

    @Override
    public void getUserId(String userId) {
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

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SERVER_ERROR:
                Toast.makeText(SignUpNew.this, "Server Busy, Try Again Later.", Toast.LENGTH_SHORT).show();
                break;
            case NO_INTERNET:
                Toast.makeText(SignUpNew.this, "No internet ", Toast.LENGTH_SHORT).show();
                break;
            case NETWORK_ERROR:
                Toast.makeText(SignUpNew.this, "Please Try Again!!! ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}