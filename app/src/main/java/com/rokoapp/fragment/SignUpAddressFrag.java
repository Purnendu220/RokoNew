package com.rokoapp.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.rokoapp.R;
import com.rokoapp.activity.Login;
import com.rokoapp.activity.SignUp;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;

import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class SignUpAddressFrag extends Fragment implements ResponseProgressListener, View.OnClickListener {

    private FragmentActivity activity;

    private String name, mobile, email, password, gender, dob;
    private LinearLayout layoutHome, layoutTimeHome, layoutOffice, layoutTimeOffice;
    private TextView tvHome, tvTimeHome, tvOffice, tvTimeOffice;

    private Button submitSignUp;

    private AutocompleteFilter typeFilter;
    private AutocompleteFilter typeFilter1;

    int PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE = 2;

    private double homeLatitude = 0, homeLongitude = 0, officeLatitude = 0, officeLongitude = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        name = null;
        mobile = null;
        email = null;
        password = null;
        gender = null;
        dob = null;
        Bundle bundle = this.getArguments();
        if (bundle !=null) {
            name = bundle.getString("name");
            mobile = bundle.getString("mobile");
            email = bundle.getString("email");
            password = bundle.getString("password");
            gender = bundle.getString("gender");
            dob = bundle.getString("dob");
        }
        View view = inflater.inflate(R.layout.fragment_sign_up_address, container, false);
        initViews(view);
        activity = getActivity();

        layoutHome.setOnClickListener(this);
        layoutTimeHome.setOnClickListener(this);
        layoutOffice.setOnClickListener(this);
        layoutTimeOffice.setOnClickListener(this);
        submitSignUp.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutHome:
                try {
                    typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
//                typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setTypeFilter(3).build();
//                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(bounds).setFilter(typeFilter).build(this);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(activity);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.d("", "findLocation: "+e.toString());
                }
                break;
            case R.id.layoutTimeHome:
                selectTiming(tvTimeHome);
//                tvTimeHome.setText(selectTiming());
                break;
            case R.id.layoutOffice:
                try {
                    typeFilter1 = new AutocompleteFilter.Builder().setCountry("IN").build();
//                typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setTypeFilter(3).build();
//                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(bounds).setFilter(typeFilter).build(this);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter1).build(activity);
                    startActivityForResult(intent, 2);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.d("", "findLocation: "+e.toString());
                }
                break;
            case R.id.layoutTimeOffice:
                selectTiming(tvTimeOffice);
//                tvTimeOffice.setText(selectTiming(tvTimeOffice));
                break;
            case R.id.submitSignUp:
                callSignUp();
                break;
        }
    }

    private void selectTiming(TextView tvTime) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) -> {
            String am_pm = "";
            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
            datetime.set(Calendar.MINUTE, selectedMinute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
            String strMinToShow = (selectedMinute == 0) ?"00":selectedMinute+"";
            tvTime.setText( strHrsToShow+":"+strMinToShow+" "+am_pm);
        }, hour, minute, false);//true for 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(activity, data);
                LatLng latLng = place.getLatLng();
                homeLatitude = latLng.latitude;
                homeLongitude = latLng.longitude;
                tvHome.setText(place.getAddress());
//                Toast.makeText(MapsActivity.this, place.getLatLng().toString(), Toast.LENGTH_LONG).show();
                Log.d("", "Place: ================================================== home: " + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(activity, data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("", "onActivityResult: RESULT_CANCELED");
            }
        }
        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(activity, data);
//                Toast.makeText(MapsActivity.this,  place.getLatLng().toString(), Toast.LENGTH_LONG).show();
                tvOffice.setText(place.getAddress());
                LatLng latLng = place.getLatLng();
                officeLatitude = latLng.latitude;
                officeLongitude = latLng.longitude;
                Log.d("", "Place: ================================================= office: " + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(activity, data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("", "onActivityResult: RESULT_CANCELED");
            }
        }
    }

    private void callSignUp() {
//        if (homeLatitude!=0& officeLatitude!=0){
            String homeLatLong = homeLatitude+", "+homeLongitude;
            String officeLatLong = officeLatitude+", "+officeLongitude;
            String homeName = tvHome.getText().toString().trim();
            String officeName = tvOffice.getText().toString().trim();
            String homeTime = tvTimeHome.getText().toString().trim();
            String officeTime = tvTimeOffice.getText().toString().trim();
        ApiManager.getParentSignUp(activity, name, mobile, email, password, gender, dob, homeName, officeName, homeTime, officeTime, homeLatLong, officeLatLong, this);

        /*}else
        ApiManager.getParentSignUp(activity, name, mobile, email, password, gender, dob,"","","","","","", this);*/
    }

    private void initViews(View view) {
        layoutHome = view.findViewById(R.id.layoutHome);
        layoutTimeHome = view.findViewById(R.id.layoutTimeHome);
        layoutOffice = view.findViewById(R.id.layoutOffice);
        layoutTimeOffice = view.findViewById(R.id.layoutTimeOffice);
        tvHome = view.findViewById(R.id.tvHome);
        tvTimeHome = view.findViewById(R.id.tvTimeHome);
        tvOffice = view.findViewById(R.id.tvOffice);
        tvTimeOffice = view.findViewById(R.id.tvTimeOffice);
        submitSignUp = view.findViewById(R.id.submitSignUp);
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            Toast.makeText(activity ,"You have Successfully Registered.", Toast.LENGTH_LONG).show();
//            emptyInputEditText();
            navigateToLogin();
        }
    }
    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SERVER_ERROR:
                Toast.makeText(activity, "Server Busy, Try Again Later.", Toast.LENGTH_SHORT).show();
                break;
            case NO_INTERNET:
                Toast.makeText(activity, "No internet ", Toast.LENGTH_SHORT).show();
                break;
            case NETWORK_ERROR:
                Toast.makeText(activity, "Please Try Again!!! ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void navigateToLogin() {
        Intent in = new Intent(activity, Login.class);
        startActivity(in);
        ((SignUp)activity).finish();
    }
}
