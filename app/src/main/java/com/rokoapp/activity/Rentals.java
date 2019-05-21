package com.rokoapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.rokoapp.R;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.utils.ParamUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;

public class Rentals extends AppCompatActivity implements View.OnClickListener, ResponseProgressListener {

    private Toolbar toolbar;
    private Button submitEnquiry;
    private TextView startDate, endDate, totalDays, startPoint, EndPoint;
    private EditText seatRequired;
    private RadioGroup radioGrpBusType;

    private AutocompleteFilter typeFilter;
    int PLACE_AUTOCOMPLETE_REQUEST_START_POINT_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_END_POINT_CODE = 2;

    private String tripStartDate, tripEndDate;
    private Date startDates, endDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals);
        initViews();
        initListeners();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Rental");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        radioGrpBusType.clearCheck();
    }

    private void initListeners() {
        startDate.setOnClickListener(Rentals.this);
        endDate.setOnClickListener(Rentals.this);
        startPoint.setOnClickListener(Rentals.this);
        EndPoint.setOnClickListener(Rentals.this);
        submitEnquiry.setOnClickListener(Rentals.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                selectDate(startDate);
                break;
            case R.id.endDate:
                selectDate(endDate);
            break;
            case R.id.startPoint:
                findLocation(PLACE_AUTOCOMPLETE_REQUEST_START_POINT_CODE);
            break;
            case R.id.EndPoint:
                findLocation(PLACE_AUTOCOMPLETE_REQUEST_END_POINT_CODE);
            break;
            case R.id.submitEnquiry:
                submitEnquiryForm();
            break;

        }
    }

    private void findLocation(int requestCode){
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)!=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            finish();
            System.exit(0);
            return;
        }
        try {
            typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
//                typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setTypeFilter(3).build();
//                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(bounds).setFilter(typeFilter).build(this);
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d("", "findLocation: "+e.toString());
        }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_START_POINT_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                /*homeLatitude = latLng.latitude;
                homeLongitude = latLng.longitude;*/
                String sourceName = (String) place.getName()+", "+place.getAddress();
                startPoint.setText(sourceName);
//                Toast.makeText(MapsActivity.this, place.getLatLng().toString(), Toast.LENGTH_LONG).show();
                Log.d("", "Place: ================================================== home: " + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("", "onActivityResult: RESULT_CANCELED");
            }
        }
        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_END_POINT_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
//                Toast.makeText(MapsActivity.this,  place.getLatLng().toString(), Toast.LENGTH_LONG).show();

                String destName = (String) place.getName()+", "+place.getAddress();
                EndPoint.setText(destName);
                LatLng latLng = place.getLatLng();
                /*officeLatitude = latLng.latitude;
                officeLongitude = latLng.longitude;*/
                Log.d("", "Place: ================================================= office: " + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("", "onActivityResult: RESULT_CANCELED");
            }
        }
    }


    private void submitEnquiryForm() {
        if (startDate.getText() == null || startDate.getText().equals("")){
            startDate.setError("Please Select Journey Start Date.");
            startDate.requestFocus();
//            Toast.makeText(Rentals.this, "Please Select Journey Start Date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.getText() == null || endDate.getText().equals("")){
            endDate.setError("Please Select Journey End Date.");
            endDate.requestFocus();
//            Toast.makeText(Rentals.this, "Please Select Journey End Date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startPoint.getText() == null || startPoint.getText().equals("")){
            startPoint.setError("Please Select Journey Start Point.");
            startPoint.requestFocus();
//            Toast.makeText(Rentals.this, "Please Select Journey Start Point.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EndPoint.getText() == null || EndPoint.getText().equals("")){
            EndPoint.setError("Please Select Journey Destination.");
            EndPoint.requestFocus();
//            Toast.makeText(activity.getApplicationContext(), "Please Select Your DOB.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (seatRequired.getText().toString().trim().isEmpty() && seatRequired.getText().toString().trim().equals("")) {
            seatRequired.requestFocus();
            seatRequired.setError("Enter Required Seats.");
            return;
        }
        String busType = null;
        int selectedId = radioGrpBusType.getCheckedRadioButtonId();
        RadioButton radioPayButton = this.findViewById(selectedId);
        if (selectedId == -1) {
            Toast.makeText(this.getApplicationContext(), "Please select Bus Type.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (radioPayButton.getText().toString().equals("A.C.")) {
                busType = "ac";
            }
            else if (radioPayButton.getText().toString().equals("Non A.C.")) {
                busType = "non-ac";
            }
        }
        if (seatRequired != null){
            String journeyStartDate =   ParamUtils.changeBirthDateFormatForRental(startDate.getText().toString().trim());
            String journeyEndDate =     ParamUtils.changeBirthDateFormatForRental(endDate.getText().toString().trim());
            String journeyStartPoint =  startPoint.getText().toString().trim();
            String journeyEndPoint =    EndPoint.getText().toString().trim();
            String requiredSeatCount =  seatRequired.getText().toString().trim();

            ApiManager.sendRentalQuery(Rentals.this, journeyStartDate, journeyEndDate, journeyStartPoint, journeyEndPoint, requiredSeatCount, busType, Rentals.this);
        }
    }

    private void selectDate(TextView dateTextView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Rentals.this, R.style.MyDialogTheme,
                (view, year1, monthOfYear, dayOfMonth) -> {
//                    dobText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    String tripDates = (year1 +"-"+ (monthOfYear + 1) + "-"+dayOfMonth);
                    dateTextView.setText(ParamUtils.changeBirthDateFormat(tripDates));
                    if (!startDate.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){
                        totalDays.setText(ParamUtils.getDifferenceDays(startDate.getText().toString(), endDate.getText().toString()));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        totalDays = findViewById(R.id.totalDays);
        startPoint = findViewById(R.id.startPoint);
        EndPoint = findViewById(R.id.EndPoint);
        seatRequired = findViewById(R.id.seatRequired);
        radioGrpBusType = findViewById(R.id.radioGrpBusType);

        submitEnquiry = findViewById(R.id.submitEnquiry);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Rentals.this, MapsActivity.class));
        Rentals.this.finish();
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            Log.d("RentalResponseComplete", "onResponseCompleted: ");
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code){
            case NO_INTERNET:
                Toast.makeText(this, "No internet ", Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_NULL:
//                Toast.makeText(this, "Unable to Login; please Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case SIGNATURE_EXPIRE:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
//                startActivity(new Intent(MapsActivity.this, Login.class));
                startActivity(new Intent(Rentals.this, MobileVerification.class));
                Rentals.this.finish();
                break;
        }
    }
}
