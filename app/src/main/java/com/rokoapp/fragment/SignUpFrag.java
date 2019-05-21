package com.rokoapp.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.activity.Login;
import com.rokoapp.activity.SignUp;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.utils.ParamUtils;

import java.util.Calendar;

public class SignUpFrag extends Fragment implements View.OnClickListener {

    private EditText fullName, mobileSignUp, emailSignUp, passwordSignUp, confirmPasswordSignUp;
    private TextView dobText;
    private RelativeLayout dobSelect;
    private RadioGroup radioGrp;
    private TextView submitSignUp, linkToSignIn;

    private String dobSt;
    private FragmentActivity activity;
    private static SignUp signUp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initViews(view);
        initListener();
        activity = getActivity();
        signUp = new SignUp();

        radioGrp.clearCheck();

        return view;
    }

    private void initListener() {
        dobSelect.setOnClickListener(this);
        submitSignUp.setOnClickListener(this);
        linkToSignIn.setOnClickListener(this);
    }

    private void initViews(View view) {
        fullName = view.findViewById(R.id.fullName);
        mobileSignUp = view.findViewById(R.id.mobileSignUp);
        emailSignUp = view.findViewById(R.id.emailSignUp);
        passwordSignUp = view.findViewById(R.id.passwordSignUp);
        confirmPasswordSignUp = view.findViewById(R.id.confirmPasswordSignUp);
        dobSelect = view.findViewById(R.id.dobSelect);
        dobText = view.findViewById(R.id.dobText);
        radioGrp = view.findViewById(R.id.radioGrp);
        submitSignUp = view.findViewById(R.id.nextSignUp);
        linkToSignIn = view.findViewById(R.id.linkToSignIn);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dobSelect:
                selectDate();
                break;
            case R.id.nextSignUp:
                createNewUser();
                break;
            case R.id.linkToSignIn:
                navigateToLogin();
                break;
        }
    }

    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.MyDialogTheme,
                (view, year1, monthOfYear, dayOfMonth) -> {
//                    dobText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    dobSt = (year1 +"-"+ (monthOfYear + 1) + "-"+dayOfMonth);
                    dobText.setText(ParamUtils.changeBirthDateFormat(dobSt));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void createNewUser() {
        if (fullName.getText().toString().trim().isEmpty() && fullName.getText().toString().trim().equals("")) {
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
        }
        if (mobileSignUp.getText().toString().trim().isEmpty() && mobileSignUp.getText().toString().trim().equals("")) {
            mobileSignUp.requestFocus();
            mobileSignUp.setError("Mobile number shouldn't be empty.");
            return;
        }
        if (mobileSignUp.getText().toString().trim().length() < 10 || mobileSignUp.getText().toString().trim().length() > 10) {
            mobileSignUp.requestFocus();
            mobileSignUp.setError("Mobile should have valid 10 digit number.");
            return;
        }
        if (passwordSignUp.getText().toString().trim().isEmpty() && passwordSignUp.getText().toString().trim().equals("")) {
            passwordSignUp.requestFocus();
            passwordSignUp.setError("Password shouldn't be empty.");
            return;
        }
        if (confirmPasswordSignUp.getText().toString().trim().isEmpty() && confirmPasswordSignUp.getText().toString().equals("")) {
            confirmPasswordSignUp.requestFocus();
            confirmPasswordSignUp.setError("Confirm password shouldn't be empty.");
            return;
        }
        if (!confirmPasswordSignUp.getText().toString().trim().contentEquals(passwordSignUp.getText().toString().trim())) {
            confirmPasswordSignUp.requestFocus();
            confirmPasswordSignUp.setError("Password didn't match.");
            return;
        }
        if (dobText.getText() == null || dobText.getText().equals("Select Date of Birth")){
            Toast.makeText(activity.getApplicationContext(), "Please Select Your DOB.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fullName != null) {
            final String name = fullName.getText().toString().trim();
            final String email = emailSignUp.getText().toString().trim();
            final String mobile = mobileSignUp.getText().toString().trim();
            final String password = passwordSignUp.getText().toString().trim();
            final String dob = dobSt;
            String gender = null;

            int selectedId = radioGrp.getCheckedRadioButtonId();
            RadioButton radioPayButton = activity.findViewById(selectedId);

            if (selectedId == -1) {
                Toast.makeText(activity.getApplicationContext(), "Please select Your Gender.", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if (radioPayButton.getText().toString().equals("Male")) {
                    gender = "male";
                }
                else if (radioPayButton.getText().toString().equals("Female")) {
                    gender = "female";
                }
            }
            if (gender != null) {
                Intent i = new Intent(activity, SignUp.class);
                i.putExtra("act","2");
                i.putExtra("name", name);
                i.putExtra("mobile", mobile);
                i.putExtra("email", email);
                i.putExtra("password", password);
                i.putExtra("gender", gender);
                i.putExtra("dob", dob);
                startActivity(i);

                /*signUp.getAdbDetail(name, mobile, email, password, gender, dob);*/
            }


        }

    }

    private void emptyInputEditText() {
        fullName.setText(null);
        emailSignUp.setText(null);
        passwordSignUp.setText(null);
        confirmPasswordSignUp.setText(null);
        dobText.setText(R.string.select_date_of_birth);
        radioGrp.clearCheck();
//        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent in = new Intent(activity, Login.class);
        startActivity(in);
        ((SignUp)activity).finish();
    }
}
