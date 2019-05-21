package com.rokoapp.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.fragment.SignUpAddressFrag;
import com.rokoapp.fragment.SignUpFrag;

public class SignUp extends AppCompatActivity /*implements View.OnClickListener*/ {

    private View basicView, adbView;
    private   TextView basicInfo, adbInfo;
    private RelativeLayout basicLayout, adbLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        String a = getIntent().getStringExtra("act");
        if (a.equals("1")){
            getBasicDetail();
        }else {
            final String name = getIntent().getStringExtra("name");
            final String mobile = getIntent().getStringExtra("mobile");
            final String email = getIntent().getStringExtra("email");
            final String password = getIntent().getStringExtra("password");
            final String dob = getIntent().getStringExtra("dob");
            String gender = getIntent().getStringExtra("gender");
            getAdbDetail(name, mobile, email, password, gender, dob);
        }



    }

    private void initViews() {
        basicLayout = findViewById(R.id.basicLayout);
        adbLayout = findViewById(R.id.adbLayout);
        basicInfo = findViewById(R.id.basicInfo);
        adbInfo = findViewById(R.id.adbInfo);
        basicView = findViewById(R.id.basicView);
        adbView = findViewById(R.id.adbView);
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basicLayout:
                getBasicDetail();
                break;
            case R.id.adbLayout:
                getAdbDetail();
                break;
        }
    }*/

    public void getBasicDetail() {
        basicView.setVisibility(View.VISIBLE);
        adbView.setVisibility(View.GONE);
        basicInfo.setTextColor(getResources().getColor(R.color.yellow));
        adbInfo.setTextColor(getResources().getColor(R.color.white));
        SignUpFrag signUpFrag = new SignUpFrag();
        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
        transactions.replace(R.id.signUpFrame, signUpFrag);
        transactions.commit();
    }

    private void getAdbDetail(String name, String mobile, String email, String password, String gender, String dob) {
        adbView.setVisibility(View.VISIBLE);
        basicView.setVisibility(View.GONE);
        adbInfo.setTextColor(getResources().getColor(R.color.yellow));
        basicInfo.setTextColor(getResources().getColor(R.color.white));
        SignUpAddressFrag signUpAddressFrag = new SignUpAddressFrag();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("mobile", mobile);
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("gender", gender);
        bundle.putString("dob", dob);
        signUpAddressFrag.setArguments(bundle);
        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
        transactions.replace(R.id.signUpFrame, signUpAddressFrag);
        transactions.commit();
    }
}
