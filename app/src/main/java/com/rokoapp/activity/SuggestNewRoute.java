package com.rokoapp.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneclickaway.opensource.placeautocomplete.api.bean.place_details.PlaceDetails;
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesStatusCodes;
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity;
import com.rokoapp.R;
import com.rokoapp.mapUtils.mapFragment.MyRouteSearch;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class SuggestNewRoute extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private LinearLayout rlOrigin, rlDest;
    private TextView tvNewOrigin, tvNewRouteDest, morningHomeLeave, eveningOfficeLeave;
    private Button buttonSearchNewRoute;

    int PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE = 2;
    private double homeLatitude = 0;
    private double homeLongitude = 0;
    private double officeLatitude = 0;
    private double officeLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_new_route);
        initViews();
        initListeners();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Suggest New Route");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rlOrigin = findViewById(R.id.rlOrigin);
        rlDest = findViewById(R.id.rlDest);
        tvNewOrigin = findViewById(R.id.tvNewOrigin);
        tvNewRouteDest = findViewById(R.id.tvNewRouteDest);
        morningHomeLeave = findViewById(R.id.morningHomeLeave);
        eveningOfficeLeave = findViewById(R.id.eveningOfficeLeave);
        buttonSearchNewRoute = findViewById(R.id.buttonSearchNewRoute);
    }

    private void initListeners() {
        rlOrigin.setOnClickListener(SuggestNewRoute.this);
        rlDest.setOnClickListener(SuggestNewRoute.this);
        morningHomeLeave.setOnClickListener(SuggestNewRoute.this);
        eveningOfficeLeave.setOnClickListener(SuggestNewRoute.this);
        buttonSearchNewRoute.setOnClickListener(SuggestNewRoute.this);
    }

    @Override
    public void onClick(View v) {
        String API_KEY = "AIzaSyAsOByI5ByaQCyzDqNyKyNZ4tmuXaDxW2E";
        switch (v.getId()){
            case R.id.rlOrigin:
                Intent i = new Intent(SuggestNewRoute.this, MyRouteSearch.class);
                String searchPlacesStatusCodes = SearchPlacesStatusCodes.INSTANCE.getCONFIG();
                String enclosingRadius = "500";
                String location = "12.9716,77.5946";
                String searchBarTitle = "Enter Origin Location";
                i.putExtra(searchPlacesStatusCodes, (Parcelable)(new SearchPlaceActivity.Config(API_KEY, location, enclosingRadius, searchBarTitle)));
                i.putExtra("codeType", "Home");
                i.putExtra("from", "SuggestNew");
                startActivityForResult(i, PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE);
            break;
            case R.id.rlDest:
                Intent j = new Intent(SuggestNewRoute.this, MyRouteSearch.class);
                String searchPlacesStatusCodes1 = SearchPlacesStatusCodes.INSTANCE.getCONFIG();
                String enclosingRadius1 = "500";
                String location1 = "12.9716,77.5946";
                String searchBarTitle1 = "Enter Destination Location";
                j.putExtra(searchPlacesStatusCodes1, (Parcelable)(new SearchPlaceActivity.Config(API_KEY, location1, enclosingRadius1, searchBarTitle1)));
                j.putExtra("codeType", "Office");
                j.putExtra("from", "SuggestNew");
                startActivityForResult(j, PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE);
            break;
            case R.id.morningHomeLeave:
                selectTiming(morningHomeLeave);
            break;
            case R.id.eveningOfficeLeave:
                selectTiming(eveningOfficeLeave);
            break;
            case R.id.buttonSearchNewRoute:
                requestForNewRoute();
            break;
        }
    }

    private void requestForNewRoute() {
        if (homeLatitude==0 && homeLongitude ==0){
            tvNewOrigin.requestFocus();
            tvNewOrigin.setError("Select home location first.");
            return;
        }if (officeLatitude==0 && officeLongitude==0){
            tvNewRouteDest.requestFocus();
            tvNewRouteDest.setError("Select office location");
            return;
        }
        if (morningHomeLeave.getText().toString().trim().isEmpty() && morningHomeLeave.getText().toString().trim().equals("")) {
            morningHomeLeave.requestFocus();
            morningHomeLeave.setError("Time to leave home shouldn't be empty.");
            return;
        }
        if (eveningOfficeLeave.getText().toString().trim().isEmpty() && eveningOfficeLeave.getText().toString().trim().equals("")) {
            eveningOfficeLeave.requestFocus();
            eveningOfficeLeave.setError("Time to leave office shouldn't be empty.");
            return;
        }
        if (homeLatitude !=0 && officeLatitude !=0){
//            ApiManager.getRouteForUser(MapsActivity.this, homeLatitude, homeLongitude, officeLatitude, officeLongitude, distanceCircle,MapsActivity.this, routeFinderList);
        }
    }

    private void selectTiming(TextView tvTime) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE && resultCode == -1) {
            PlaceDetails placeDetails = data != null ? (PlaceDetails)data.getParcelableExtra(SearchPlacesStatusCodes.INSTANCE.getPLACE_DATA()) : null;
            homeLatitude = placeDetails.getGeometry().getLocation().getLat();
            homeLongitude = placeDetails.getGeometry().getLocation().getLng();
            tvNewOrigin.setText(placeDetails.getName()+", "+placeDetails.getFormattedAddress());
            Log.i(this.getClass().getSimpleName(), "onActivityResult: " + placeDetails + "  ");
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE && resultCode == -1){
            PlaceDetails placeDetails = data != null ? (PlaceDetails)data.getParcelableExtra(SearchPlacesStatusCodes.INSTANCE.getPLACE_DATA()) : null;
            officeLatitude = placeDetails.getGeometry().getLocation().getLat();
            officeLongitude = placeDetails.getGeometry().getLocation().getLng();
            tvNewRouteDest.setText( placeDetails.getName()+", "+placeDetails.getFormattedAddress());

            Log.i(this.getClass().getSimpleName(), "onActivityResult: " + placeDetails + "  ");
        }
        else {
            Log.d(TAG, "onActivityResult: ");
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
        startActivity(new Intent(SuggestNewRoute.this, MapsActivity.class));
        SuggestNewRoute.this.finish();
    }
}
