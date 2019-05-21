package com.rokoapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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
import com.rokoapp.utils.SaveCache;

import java.util.Calendar;

import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS;
import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.DOB;
import static com.rokoapp.utils.ParamUtils.EMAIL;
import static com.rokoapp.utils.ParamUtils.GENDER;
import static com.rokoapp.utils.ParamUtils.HOME_ADDRESS;
import static com.rokoapp.utils.ParamUtils.HOME_LAT_LONG;
import static com.rokoapp.utils.ParamUtils.MOBILE_NUM;
import static com.rokoapp.utils.ParamUtils.MORNING_HOME_LEAVE;
import static com.rokoapp.utils.ParamUtils.OFFICE_ADDRESS;
import static com.rokoapp.utils.ParamUtils.OFFICE_END_TIME;
import static com.rokoapp.utils.ParamUtils.OFFICE_LAT_LONG;
import static com.rokoapp.utils.ParamUtils.PASS_EARNED;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;
import static com.rokoapp.utils.ParamUtils.REFER_CODE;
import static com.rokoapp.utils.ParamUtils.TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_COMPLEMENT_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.USER_COMPLIMENT_PASS;
import static com.rokoapp.utils.ParamUtils.USER_NAME;
import static com.rokoapp.utils.ParamUtils.USER_TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_TYPE;

public class EditProfile extends AppCompatActivity implements ResponseProgressListener, View.OnClickListener {

    private Toolbar toolbar;

    private EditText nameUser, emailIdUser;
    private TextView birthdayUser, tvHomeEdit, tvTimeHomeEdit, tvOfficeEdit, tvTimeOfficeEdit;
    private TextView totalPassCount, comPassCount, comPassValidity;
    private RelativeLayout rlBDaySelect;
    private RadioGroup radioGrp;
    private ImageView imgUser;
    private LinearLayout layoutHomeEdit, layoutTimeHomeEdit, layoutOfficeEdit, layoutTimeOfficeEdit, passSection;

    private AppCompatButton btnSubmit;


    private String userName, email, dobSt, gender, homeLatLong, officeLatLong;
    private AutocompleteFilter typeFilter, typeFilter1;

    int PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE = 2;

    private double homeLatitude = 0, homeLongitude = 0, officeLatitude = 0, officeLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
        initListeners();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Edit Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        showData();
    }

    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this, R.style.MyDialogTheme,
                (view, year1, monthOfYear, dayOfMonth) -> {
//                    birthdayUser.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    dobSt = (year1 +"-"+ (monthOfYear + 1) + "-"+dayOfMonth);
                    birthdayUser.setText(ParamUtils.changeBirthDateFormat(dobSt));
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showData() {
        userName = SaveCache.getString(EditProfile.this, USER_NAME);
        SaveCache.getString(EditProfile.this, MOBILE_NUM);
        email = SaveCache.getString(EditProfile.this, EMAIL);

        dobSt = SaveCache.getString(EditProfile.this, DOB);
        String genderSt = SaveCache.getString(EditProfile.this, GENDER);
        SaveCache.getString(EditProfile.this, USER_TYPE);
        String homeAddress = SaveCache.getString(EditProfile.this, HOME_ADDRESS);
        String officeAddress = SaveCache.getString(EditProfile.this, OFFICE_ADDRESS);
        String homeLeaveTime = SaveCache.getString(EditProfile.this, MORNING_HOME_LEAVE);
        String officeLeaveTime = SaveCache.getString(EditProfile.this, OFFICE_END_TIME);
        homeLatLong = SaveCache.getString(EditProfile.this, HOME_LAT_LONG);
        officeLatLong = SaveCache.getString(EditProfile.this, OFFICE_LAT_LONG);
        SaveCache.getString(EditProfile.this, REFER_CODE);
        SaveCache.getBoolean(EditProfile.this, PASS_EARNED);

        if (!userName.equals("") && !userName.isEmpty()) {
            String[] old = userName.split(" ");
            StringBuilder add = new StringBuilder();
            for (String s : old)
                add.append(s.charAt(0));

            String text = add.toString();
            text = text.length() < 2 ? text : text.substring(0, 2);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder().buildRound(text, color); // radius in px
            imgUser.setImageDrawable(drawable);
            nameUser.setText(userName);
            emailIdUser.setText(email);
            birthdayUser.setText(ParamUtils.changeBirthDateFormat(dobSt));
            if (genderSt.equals("male")) {
                radioGrp.clearCheck();
                ((RadioButton)radioGrp.getChildAt(0)).setChecked(true);
            }
            else if (genderSt.equals("female")) {
                radioGrp.clearCheck();
                ((RadioButton)radioGrp.getChildAt(1)).setChecked(true);
            }
            tvHomeEdit.setText(homeAddress);
            tvOfficeEdit.setText(officeAddress);
            tvTimeHomeEdit.setText(homeLeaveTime);
            tvTimeOfficeEdit.setText(officeLeaveTime);

            passSection.setVisibility(View.GONE);

        }
    }

    private void applyChanges() {
        if (nameUser.getText().toString().trim().isEmpty() && nameUser.getText().toString().trim().equals("")) {
            nameUser.requestFocus();
            nameUser.setError("Name shouldn't be empty.");
            return;
        }
        if (emailIdUser.getText().toString().trim().isEmpty() && emailIdUser.getText().toString().trim().equals("")) {
            emailIdUser.requestFocus();
            emailIdUser.setError("Email shouldn't be empty.");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailIdUser.getText().toString().trim()).matches()) {
            emailIdUser.requestFocus();
            emailIdUser.setError("Email should be valid.");
            return;
        }
        if (birthdayUser.getText().toString().trim().isEmpty() && birthdayUser.getText().toString().trim().equals("")) {
            birthdayUser.requestFocus();
            birthdayUser.setError("Birthday shouldn't be empty.");
            return;
        }
        int selectedId = radioGrp.getCheckedRadioButtonId();
        RadioButton radioPayButton = findViewById(selectedId);

        if (selectedId == -1) {
            Toast.makeText(EditProfile.this, "Please select Your Gender.", Toast.LENGTH_SHORT).show();
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
        if (nameUser !=null){
            String name = nameUser.getText().toString().trim();
            email = emailIdUser.getText().toString().trim();
//            String birthday = birthdayUser.getText().toString().trim();
            if (homeLatitude !=0 & homeLongitude != 0) {
                homeLatLong = homeLatitude + ", " + homeLongitude;
            }else
                homeLatLong = SaveCache.getString(EditProfile.this, HOME_LAT_LONG);
            if (officeLatitude !=0 & officeLongitude !=0) {
                officeLatLong = officeLatitude + ", " + officeLongitude;
            }else
                officeLatLong = SaveCache.getString(EditProfile.this, OFFICE_LAT_LONG);
            String homeName = tvHomeEdit.getText().toString().trim();
            String officeName = tvOfficeEdit.getText().toString().trim();
            String homeTime = tvTimeHomeEdit.getText().toString().trim();
            String officeTime = tvTimeOfficeEdit.getText().toString().trim();
            ApiManager.getUserEditProfile(EditProfile.this, name, email, dobSt, gender, homeName, homeTime, homeLatLong, officeName, officeTime, officeLatLong, EditProfile.this);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nameUser = findViewById(R.id.nameUser);
        emailIdUser = findViewById(R.id.emailIdUser);
        birthdayUser = findViewById(R.id.birthdayUser);
        rlBDaySelect = findViewById(R.id.rlBDaySelect);
        radioGrp = findViewById(R.id.radioGrp);
        imgUser = findViewById(R.id.imgUser);
        layoutHomeEdit = findViewById(R.id.layoutHomeEdit);
        tvHomeEdit = findViewById(R.id.tvHomeEdit);
        layoutTimeHomeEdit = findViewById(R.id.layoutTimeHomeEdit);
        tvTimeHomeEdit = findViewById(R.id.tvTimeHomeEdit);
        layoutOfficeEdit = findViewById(R.id.layoutOfficeEdit);
        tvOfficeEdit = findViewById(R.id.tvOfficeEdit);
        layoutTimeOfficeEdit = findViewById(R.id.layoutTimeOfficeEdit);
        passSection = findViewById(R.id.passSection);
        tvTimeOfficeEdit = findViewById(R.id.tvTimeOfficeEdit);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void initListeners(){
        layoutHomeEdit.setOnClickListener(EditProfile.this);
        layoutTimeHomeEdit.setOnClickListener(EditProfile.this);
        layoutOfficeEdit.setOnClickListener(EditProfile.this);
        layoutTimeOfficeEdit.setOnClickListener(EditProfile.this);
        btnSubmit.setOnClickListener(EditProfile.this);
        rlBDaySelect.setOnClickListener(EditProfile.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutHomeEdit:
                try {
                    typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
//                typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setTypeFilter(3).build();
//                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(bounds).setFilter(typeFilter).build(this);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.d("", "findLocation: "+e.toString());
                }
                break;
            case R.id.layoutTimeHomeEdit:
                selectTiming(tvTimeHomeEdit);
                break;
            case R.id.layoutOfficeEdit:
                try {
                    typeFilter1 = new AutocompleteFilter.Builder().setCountry("IN").build();
//                typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setTypeFilter(3).build();
//                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(bounds).setFilter(typeFilter).build(this);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter1).build(this);
                    startActivityForResult(intent, 2);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.d("", "findLocation: "+e.toString());
                }
                break;
            case R.id.layoutTimeOfficeEdit:
                selectTiming(tvTimeOfficeEdit);
                break;
            case R.id.btnSubmit:
                applyChanges();
                break;
            case R.id.rlBDaySelect:
                selectDate();
                break;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                homeLatitude = latLng.latitude;
                homeLongitude = latLng.longitude;
                tvHomeEdit.setText(place.getAddress());
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
        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
//                Toast.makeText(MapsActivity.this,  place.getLatLng().toString(), Toast.LENGTH_LONG).show();
                tvOfficeEdit.setText(place.getAddress());
                LatLng latLng = place.getLatLng();
                officeLatitude = latLng.latitude;
                officeLongitude = latLng.longitude;
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
//        startActivity(new Intent(EditProfile.this, Home.class));
        startActivity(new Intent(EditProfile.this, MapsActivity.class));
        EditProfile.this.finish();
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            Toast.makeText(EditProfile.this, "Profile Updated Successfully.", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SERVER_ERROR:
//                Toast.makeText(this, "Server Busy, Try Again Later.", Toast.LENGTH_SHORT).show();
                break;
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
                startActivity(new Intent(EditProfile.this, MobileVerification.class));
                EditProfile.this.finish();
                break;
        }
    }
}
