package com.rokoapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oneclickaway.opensource.placeautocomplete.api.bean.place_details.PlaceDetails;
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesStatusCodes;
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity;
import com.rokoapp.R;

import org.jetbrains.annotations.Nullable;

public class SearchPlace extends AppCompatActivity implements View.OnClickListener {

    private String codeType;
    private int HOME_CODE;
    private int OFFICE_CODE;
    private String API_KEY = "AIzaSyAsOByI5ByaQCyzDqNyKyNZ4tmuXaDxW2E";

    private CardView cardCurrentLocation;
    private LinearLayout homeLLayout, workLLayout, layoutExtra;
    private RecyclerView recyclerRecentSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        initViews();
        initListeners();

        codeType = getIntent().getStringExtra("codeType");
        if (codeType.equals("HOME")){
            HOME_CODE = getIntent().getIntExtra("reqCode", 0);
            searchPlace(HOME_CODE);
        }else {
            OFFICE_CODE = getIntent().getIntExtra("reqCode", 0);
            searchPlace(OFFICE_CODE);
        }
    }

    private void searchPlace(int CODE) {
        final Intent intent = new Intent(this, SearchPlaceActivity.class);
        String searchPlacesStatusCodes = SearchPlacesStatusCodes.INSTANCE.getCONFIG();
        String enclosingRadius = "500";
        String location = "12.9716,77.5946";
        String searchBarTitle = "Enter Source Location";
        intent.putExtra(searchPlacesStatusCodes, (Parcelable)(new SearchPlaceActivity.Config(this.API_KEY, location, enclosingRadius, searchBarTitle)));
        if (Build.VERSION.SDK_INT >= 21) {
//            Pair pair = Pair.create(, SearchPlacesStatusCodes.INSTANCE.getPLACEHOLDER_TRANSITION());
//            Bundle options = ActivityOptions.makeSceneTransitionAnimation((Activity) KotlinClassCode.this, new Pair[]{pair}).toBundle();
            startActivityForResult(intent, 700);
        } else {
            startActivityForResult(intent, 700);
            overridePendingTransition(-14, -13);
        }

        /*AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (CODE==HOME_CODE){
                    LatLng latLng = place.getLatLng();
                    ParamUtils.homeLatitude = latLng.latitude;
                    ParamUtils.homeLongitude = latLng.longitude;
                    ParamUtils.homeLocation = (String) place.getAddress();
                    ParamUtils.sourceName = (String) place.getName()+", "+place.getAddress();
                    finish();

                }else {
                    ParamUtils.officeLocation = (String) place.getAddress();
                    ParamUtils.destName = (String) place.getName()+", "+place.getAddress();
                    LatLng latLng = place.getLatLng();
                    ParamUtils.officeLatitude = latLng.latitude;
                    ParamUtils.officeLongitude = latLng.longitude;
                    finish();
                }
                Log.i("TAG", "Place: " + place.getName());//get place details here
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 700 && resultCode == -1) {
            PlaceDetails placeDetails = data != null ? (PlaceDetails)data.getParcelableExtra(SearchPlacesStatusCodes.INSTANCE.getPLACE_DATA()) : null;

            Toast.makeText(this, placeDetails.getName()+"", Toast.LENGTH_LONG).show();
            /*searchLocationET.setText((placeDetails != null ? placeDetails.getName() : null));
            TextView var5 = this.placeDetailsTV;
            if (var5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("placeDetailsTV");
            }

            var5.setText(String.valueOf(placeDetails));*/
            Log.i(this.getClass().getSimpleName(), "onActivityResult: " + placeDetails + "  ");
        }

    }

    private void initViews() {
//        mAutocomplete = findViewById(R.id.places_autocomplete);
        cardCurrentLocation = findViewById(R.id.cardCurrentLocation);
        homeLLayout = findViewById(R.id.homeLLayout);
        workLLayout = findViewById(R.id.workLLayout);
        recyclerRecentSearch = findViewById(R.id.recyclerRecentSearch);
        layoutExtra = findViewById(R.id.layoutExtra);
//        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
    }

    private void initListeners() {
        cardCurrentLocation.setOnClickListener(SearchPlace.this);
        homeLLayout.setOnClickListener(SearchPlace.this);
        workLLayout.setOnClickListener(SearchPlace.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardCurrentLocation:
                selectCurrentLocation();
            break;
            case R.id.homeLLayout:

            break;
            case R.id.workLLayout:

            break;
        }
    }

    private void selectCurrentLocation() {
        /*LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.asGoogleApiClient();

            *//*client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d("TAG", "location update " + location);
                        if (codeType.equals("HOME")){
//                            LatLng latLng = place.getLatLng();
                            ParamUtils.homeLatitude = location.getLatitude();
                            ParamUtils.homeLongitude = location.getLongitude();
                            ParamUtils.homeLocation = (String) location.ge();
                            ParamUtils.sourceName = (String) place.getName()+", "+place.getAddress();
                            finish();

                        }else {
                            ParamUtils.officeLocation = (String) place.getAddress();
                            ParamUtils.destName = (String) place.getName()+", "+place.getAddress();
                            LatLng latLng = place.getLatLng();
                            ParamUtils.officeLatitude = latLng.latitude;
                            ParamUtils.officeLongitude = latLng.longitude;
                            finish();
                        }


                    }
                }
            }, null);*//*
        }*/

        // Use fields to define the data types to return.
       /* List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

//        Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission();
        }*/
    }
}
