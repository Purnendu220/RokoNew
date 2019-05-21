package com.rokoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.iid.FirebaseInstanceId;
import com.oneclickaway.opensource.placeautocomplete.api.bean.place_details.PlaceDetails;
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesStatusCodes;
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity;
import com.rokoapp.R;
import com.rokoapp.dbHelper.RokoDbAdapter;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.mapUtils.TrackerService;
import com.rokoapp.mapUtils.mapFragment.MyRouteSearch;
import com.rokoapp.mapUtils.responseModel.SearchHistoryModel;
import com.rokoapp.model.response.RouteFinderModel;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.rokoapp.utils.ParamUtils.ANDROID_TOKEN;
import static com.rokoapp.utils.ParamUtils.ID_AUTH;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;
import static com.rokoapp.utils.ParamUtils.changeDateFormat;
import static com.rokoapp.utils.ParamUtils.changeDateFormatForPass;
import static com.rokoapp.utils.ParamUtils.getTimeForPass;
import static com.rokoapp.utils.ParamUtils.homeLatitude;
import static com.rokoapp.utils.ParamUtils.homeLocation;
import static com.rokoapp.utils.ParamUtils.homeLongitude;
import static com.rokoapp.utils.ParamUtils.myLastDestLat;
import static com.rokoapp.utils.ParamUtils.myLastDestLong;
import static com.rokoapp.utils.ParamUtils.myLastDestName;
import static com.rokoapp.utils.ParamUtils.myLastRouteDate;
import static com.rokoapp.utils.ParamUtils.myLastOriginLat;
import static com.rokoapp.utils.ParamUtils.myLastOriginLong;
import static com.rokoapp.utils.ParamUtils.myLastOriginName;
import static com.rokoapp.utils.ParamUtils.myLastVirtualRouteId;
import static com.rokoapp.utils.ParamUtils.officeLatitude;
import static com.rokoapp.utils.ParamUtils.officeLocation;
import static com.rokoapp.utils.ParamUtils.officeLongitude;

@SuppressWarnings("deprecation")
public class MapsActivity extends FragmentActivity implements View.OnClickListener, ResponseProgressListener {

    int PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE = 2;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView textHomeLocation, textOfficeLocation;
    private Button findRouteButton;

    private String distanceCircle = "1";

    private RelativeLayout headerUser;
    private TextView textViewUserName, welcomeText, trackBus;
    private ImageView userImage;
    private CardView cardTracking;

    private TextView timeSlot, pickupPoint, tripDate;

    private Boolean exit = false;
    private String androidToken = null;
    private String fireBaseId = null;
    private String API_KEY = "AIzaSyAsOByI5ByaQCyzDqNyKyNZ4tmuXaDxW2E";

    private RokoDbAdapter dbAdapter;
    
    ArrayList<RouteFinderModel> routeFinderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initViews();
        initListeners();

        ID_AUTH = SaveCache.getString(MapsActivity.this,"Authorization");
        String token = SaveCache.getString(MapsActivity.this, ANDROID_TOKEN);
        if (token != null && !token.isEmpty()){
            androidToken = token;
        }else {
            androidToken = FirebaseInstanceId.getInstance().getToken();
        }

        ApiManager.getDeviceDetail(MapsActivity.this, androidToken, fireBaseId, MapsActivity.this);

        showData();
        showTrackingCard();
    }

    private void showTrackingCard() {
        myLastVirtualRouteId = SaveCache.getString(MapsActivity.this, ParamUtils.lastVirtualRouteId);
        myLastOriginLat = SaveCache.getFloat(MapsActivity.this, ParamUtils.lastOriginLat);
        myLastOriginLong = SaveCache.getFloat(MapsActivity.this, ParamUtils.lastOriginLong);
        myLastDestLat = SaveCache.getFloat(MapsActivity.this, ParamUtils.lastDestLat);
        myLastDestLong = SaveCache.getFloat(MapsActivity.this, ParamUtils.lastDestLong);
        myLastOriginName = SaveCache.getString(MapsActivity.this, ParamUtils.lastOriginName);
        myLastDestName = SaveCache.getString(MapsActivity.this, ParamUtils.lastDestName);
        myLastRouteDate = SaveCache.getString(MapsActivity.this, ParamUtils.bookingReachTime);


        pickupPoint.setText(myLastOriginName);
        if(myLastRouteDate != null & !myLastRouteDate.isEmpty() & !myLastRouteDate.equals("")) {
//            tripDate.setText(myLastRouteDate);
            tripDate.setText(changeDateFormatForPass(myLastRouteDate));
            timeSlot.setText(getTimeForPass(myLastRouteDate));
        }

        if (myLastVirtualRouteId != null & myLastOriginLat !=0 & myLastOriginLong != 0 & myLastDestLat !=0 & myLastDestLong !=0) {
            cardTracking.setVisibility(View.VISIBLE);
        }else
            cardTracking.setVisibility(View.GONE);
    }

    private void showData() {
        int width = getResources().getDisplayMetrics().widthPixels*2 / 3;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
        params.width = width;
        mNavigationView.setLayoutParams(params);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            if (menuItem.getItemId() == R.id.my_trips) {
                startActivity(new Intent(MapsActivity.this, TripHistory.class));
                MapsActivity.this.finish();
            }
            /*if (menuItem.getItemId() == R.id.pass_list) {
//                    startActivity(new Intent(Home.this, Notifications.class));
                startActivity(new Intent(MapsActivity.this, PassesList.class));
                MapsActivity.this.finish();
            }*/
            if (menuItem.getItemId() == R.id.my_passes) {
                startActivity(new Intent(MapsActivity.this, WhatPass.class));
                MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.notification) {
                startActivity(new Intent(MapsActivity.this, Notifications.class));
                MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.route_map) {
                startActivity(new Intent(MapsActivity.this, RouteMap.class));
                MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.drawer_sos) {
                startActivity(new Intent(MapsActivity.this, MySos.class));
//                    MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.drawer_rental) {
                startActivity(new Intent(MapsActivity.this, Rentals.class));
                MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.drawer_suggest_route) {
                startActivity(new Intent(MapsActivity.this, SuggestNewRoute.class));
                MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.drawer_feedback) {
                startActivity(new Intent(MapsActivity.this, Feedback.class));
                MapsActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.drawer_coupon) {
                Intent i = new Intent(MapsActivity.this, ApplyCoupons.class);
                i.putExtra("from", "Home");
                startActivity(i);
            }
            if (menuItem.getItemId() == R.id.drawer_share) {
                startActivity(new Intent(MapsActivity.this, ReferAndEarn.class));
                MapsActivity.this.finish();
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Roko App at: https://play.google.com/store/apps/details?id="+getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/
            }
            /*if (menuItem.getItemId() == R.id.drawer_exit) {

            }
            if (menuItem.getItemId() == R.id.drawer_costumer_service) {
                startActivity(new Intent(MainActivity.this, CostumerService.class));
                MainActivity.this.finish();
            }
            if (menuItem.getItemId() == R.id.drawer_faq) {
                startActivity(new Intent(MainActivity.this, Faq.class));
                MainActivity.this.finish();
            }*/
            return false;
        });
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        setupDrawerHeader();
    }

    /*private void findLocation(int requestCode){
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)!=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            finish();
            System.exit(0);
            return;
        }
            try {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this);
                startActivityForResult(intent, requestCode);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                Log.d("", "findLocation: "+e.toString());
            }
    }*/

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                LatLng latLng = place.getLatLng();
//                homeLatitude = latLng.latitude;
//                homeLongitude = latLng.longitude;
//                textHomeLocation.setText(place.getAddress());
//                sourceName = (String) place.getName()+", "+place.getAddress();
////                Toast.makeText(MapsActivity.this, place.getLatLng().toString(), Toast.LENGTH_LONG).show();
//                Log.d("", "Place: ================================================== home: " + place.toString());
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.i("", status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//                Log.d("", "onActivityResult: RESULT_CANCELED");
//            }
//        }
//        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
////                Toast.makeText(MapsActivity.this,  place.getLatLng().toString(), Toast.LENGTH_LONG).show();
//                textOfficeLocation.setText(place.getAddress());
//                destName = (String) place.getName()+", "+place.getAddress();
//                LatLng latLng = place.getLatLng();
//                officeLatitude = latLng.latitude;
//                officeLongitude = latLng.longitude;
//                Log.d("", "Place: ================================================= office: " + place.toString());
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.i("", status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//                Log.d("", "onActivityResult: RESULT_CANCELED");
//            }
//        }
//    }

    private void setupDrawerHeader() {
        String userName = SaveCache.getString(MapsActivity.this, "userName");

        welcomeText.setText("Welcome");
        textViewUserName.setText(userName);

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
            userImage.setImageDrawable(drawable);
        }

        headerUser.setOnClickListener( view -> {
            Intent i = new Intent(MapsActivity.this, EditProfile.class);
            startActivity(i);
        });
    }

    private void initViews() {
        dbAdapter = new RokoDbAdapter(MapsActivity.this);

        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.main_drawer);
        textHomeLocation = findViewById(R.id.textHomeLocation);
        textOfficeLocation = findViewById(R.id.textOfficeLocation);
        findRouteButton = findViewById(R.id.findRouteButton);
        trackBus = findViewById(R.id.trackBus);
        cardTracking = findViewById(R.id.cardTracking);
        timeSlot = findViewById(R.id.timeSlot);
        pickupPoint = findViewById(R.id.pickupPoint);
        tripDate = findViewById(R.id.tripDate);

        View header = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        headerUser = header.findViewById(R.id.headerUser);
        textViewUserName = header.findViewById(R.id.userName);
        welcomeText = header.findViewById(R.id.welcomeText);
        userImage = header.findViewById(R.id.userImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (exit) {
            System.exit(0);
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);

        }
    }

    private void initListeners() {
        textHomeLocation.setOnClickListener(this);
        textOfficeLocation.setOnClickListener(this);
        findRouteButton.setOnClickListener(this);
        trackBus.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeLocation != null){
            textHomeLocation.setText(homeLocation);
        }
        if (officeLocation != null){
            textOfficeLocation.setText(officeLocation);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.searchBar:
                if(!frameIsVisible){
                    frameIsVisible = true;
                    searchBar.setVisibility(View.GONE);
                    frame_contain.setVisibility(View.VISIBLE);

                }
                break;*/
            case R.id.trackBus:

                startActivity(new Intent(MapsActivity.this, TrackMyBusLive.class));
                break;
            case R.id.textHomeLocation:
                Intent i = new Intent(MapsActivity.this, MyRouteSearch.class);
                String searchPlacesStatusCodes = SearchPlacesStatusCodes.INSTANCE.getCONFIG();
                String enclosingRadius = "500";
                String location = "12.9716,77.5946";
                String searchBarTitle = "Enter Source Location";
                i.putExtra(searchPlacesStatusCodes, (Parcelable)(new SearchPlaceActivity.Config(this.API_KEY, location, enclosingRadius, searchBarTitle)));
                i.putExtra("codeType", "Home");
                i.putExtra("from", "MapsActivity");
                startActivityForResult(i, PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE);
                break;
            case R.id.textOfficeLocation:
                Intent j = new Intent(MapsActivity.this, MyRouteSearch.class);
                String searchPlacesStatusCodes1 = SearchPlacesStatusCodes.INSTANCE.getCONFIG();
                String enclosingRadius1 = "500";
                String location1 = "12.9716,77.5946";
                String searchBarTitle1 = "Enter Destination Location";
                j.putExtra(searchPlacesStatusCodes1, (Parcelable)(new SearchPlaceActivity.Config(this.API_KEY, location1, enclosingRadius1, searchBarTitle1)));
                j.putExtra("codeType", "Office");
                j.putExtra("from", "MapsActivity");
                startActivityForResult(j, PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE);
                break;
            case R.id.findRouteButton:
                    findTheRoute();
                break;
            /*case R.id.closeLocationLayout:
                if (frameIsVisible){
                   frameIsVisible = false;
                   searchBar.setVisibility(View.VISIBLE);
                   frame_contain.setVisibility(View.GONE);
                   textHomeLocation.setText(R.string.select_home_location);
                   textOfficeLocation.setText(R.string.select_office_location);
                    homeLatitude = 0;
                    homeLongitude = 0;
                    officeLongitude = 0;
                    officeLatitude = 0;
                }
                break;*/
        }
    }

    private void findTheRoute() {
        if (homeLatitude==0 && homeLongitude ==0){
            textHomeLocation.requestFocus();
            textHomeLocation.setError("Select home location first.");
            return;
        }if (officeLatitude==0 && officeLongitude==0){
            textOfficeLocation.requestFocus();
            textOfficeLocation.setError("Select office location");
            return;
        }
        if (homeLatitude !=0 && officeLatitude !=0){
//            ApiManager.getRouteForUser(MapsActivity.this, 28.63220811970849, 77.3095102197085, 28.5072885197085, 77.3778969, MapsActivity.this, routeFinderList);
            routeFinderList.clear();
            ApiManager.getRouteForUser(MapsActivity.this, homeLatitude, homeLongitude, officeLatitude, officeLongitude, distanceCircle,MapsActivity.this, routeFinderList);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_HOME_CODE && resultCode == -1) {
            PlaceDetails placeDetails = data != null ? (PlaceDetails)data.getParcelableExtra(SearchPlacesStatusCodes.INSTANCE.getPLACE_DATA()) : null;

//            searchLocationET.setText((placeDetails != null ? placeDetails.getName() : null));

                homeLatitude = placeDetails.getGeometry().getLocation().getLat();
                homeLongitude = placeDetails.getGeometry().getLocation().getLng();
                homeLocation = placeDetails.getFormattedAddress();
                textHomeLocation.setText(homeLocation);
                ParamUtils.sourceName = (String) placeDetails.getName()+", "+placeDetails.getFormattedAddress();
            SearchHistoryModel searchHistoryModel = new SearchHistoryModel(placeDetails.getName(),homeLocation, homeLatitude, homeLongitude);
            long homeId = dbAdapter.insertSearchHistory(searchHistoryModel);
            if (homeId < 0) {
                Log.d(TAG, "onResponse: ===========" + "Something Went Wrong");
            } else {
                Log.d(TAG, "onResponse: ===========" + "Everything is ok");
            }

            Log.i(this.getClass().getSimpleName(), "onActivityResult: " + placeDetails + "  ");
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_OFFICE_CODE && resultCode == -1){
            PlaceDetails placeDetails = data != null ? (PlaceDetails)data.getParcelableExtra(SearchPlacesStatusCodes.INSTANCE.getPLACE_DATA()) : null;
            officeLatitude = placeDetails.getGeometry().getLocation().getLat();
            officeLongitude = placeDetails.getGeometry().getLocation().getLng();
            officeLocation = placeDetails.getFormattedAddress();
            textOfficeLocation.setText(officeLocation);
            ParamUtils.destName = (String) placeDetails.getName()+", "+placeDetails.getFormattedAddress();

            SearchHistoryModel searchHistoryModel = new SearchHistoryModel(placeDetails.getName(),officeLocation, officeLatitude, officeLongitude);
            long homeId = dbAdapter.insertSearchHistory(searchHistoryModel);
            if (homeId < 0) {
                Log.d(TAG, "onResponse: ===========" + "Something Went Wrong");
            } else {
                Log.d(TAG, "onResponse: ===========" + "Everything is ok");
            }

            Log.i(this.getClass().getSimpleName(), "onActivityResult: " + placeDetails + "  ");
        }
        else {
            Log.d(TAG, "onActivityResult: ");
        }

    }

    @Override
    public void onResponseInProgress() { }

    @Override
    public void onResponseCompleted(int i) {
        switch (i){
            case 1:
                Intent m = new Intent(MapsActivity.this, SelectShortestRoute.class);
                m.putExtra("source", ParamUtils.sourceName);
                m.putExtra("dest", ParamUtils.destName);
                m.putExtra("routeList", routeFinderList);
                this.startActivity(m);
                MapsActivity.this.finish();
            break;
            case 2:
                Log.d("", "onResponseCompleted: Device details are saved.");
            break;
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
                startActivity(new Intent(MapsActivity.this, MobileVerification.class));
                MapsActivity.this.finish();
                break;
            case LIST_EMPTY:
                routeFinderList.clear();
                switch (distanceCircle){
                    case "1":
                        distanceCircle = "2";
                        ApiManager.getRouteForUser(MapsActivity.this, homeLatitude, homeLongitude, officeLatitude, officeLongitude, distanceCircle,MapsActivity.this, routeFinderList);
                        break;
                    case "2":
                        distanceCircle = "3";
                        ApiManager.getRouteForUser(MapsActivity.this, homeLatitude, homeLongitude, officeLatitude, officeLongitude, distanceCircle,MapsActivity.this, routeFinderList);
                        break;
                    case "3":
                        distanceCircle = "5";
                        ApiManager.getRouteForUser(MapsActivity.this, homeLatitude, homeLongitude, officeLatitude, officeLongitude, distanceCircle,MapsActivity.this, routeFinderList);
                        break;
                    case "5":
                        Toast.makeText(MapsActivity.this, "Sorry !!! Area is not serviceable right now.", Toast.LENGTH_LONG).show();
                        break;
                }
                break;

        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        //finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParamUtils.sourceName = null;
        ParamUtils.destName = null;
        ParamUtils.homeLocation = null;
        ParamUtils.officeLocation = null;
        ParamUtils.homeLatitude = 0;
        ParamUtils.homeLongitude = 0;
        ParamUtils.officeLatitude = 0;
        ParamUtils.officeLongitude = 0;
    }
}
