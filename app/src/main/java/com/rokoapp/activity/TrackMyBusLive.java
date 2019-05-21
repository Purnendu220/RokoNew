package com.rokoapp.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;
import com.rokoapp.R;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.mapUtils.MapUtils;
import com.rokoapp.model.FireBaseDbModel;
import com.rokoapp.model.response.MyRouteSubData;
import com.rokoapp.model.response.RouteStopData;
import com.rokoapp.model.response.TripHistorySubModel;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.rokoapp.utils.ParamUtils.ID_AUTH;
import static com.rokoapp.utils.ParamUtils.myLastDestLat;
import static com.rokoapp.utils.ParamUtils.myLastDestLong;
import static com.rokoapp.utils.ParamUtils.myLastDestName;
import static com.rokoapp.utils.ParamUtils.myLastOriginLat;
import static com.rokoapp.utils.ParamUtils.myLastOriginLong;
import static com.rokoapp.utils.ParamUtils.myLastOriginName;
import static com.rokoapp.utils.ParamUtils.myLastVirtualRouteId;
import static com.rokoapp.utils.ParamUtils.virtualRouteIdS;

public class TrackMyBusLive extends AppCompatActivity implements OnMapReadyCallback, ResponseProgressListener, View.OnClickListener {

    private Toolbar toolbar;
    private SupportMapFragment mapFragment;

    String virtualRouteId="315";
    int originId=5;
    int destinationId=8;
    TripHistorySubModel userRouteData;
    private List<MyRouteSubData> stopsList = new ArrayList<>();
    private Handler handler;
    private GoogleMap mMap;

    private LatLng origin = null;
    private LatLng destination = null;
    ArrayList<LatLng> pointsList = new ArrayList<>();
    List<LatLng> path = new ArrayList<>();

    private Marker carMarker;
    private float v;
    private double lat, lng;
    private LatLng startPosition;
    private LatLng endPosition;
    private int index;
    private int next;
    MarkerOptions options;
    private LinearLayout scanToBoardLayout, cancelRideLayout, helpLayout, cancelHelpLayout;
    private RelativeLayout scanToBoardRl;
    private TextView busNo, busName, busStartFromOrigin, busArrivalOnStop;
    private ImageView arrowMark;
    public BottomSheetBehavior mBottomSheetBehavior;
    private int sheetState;

    private FirebaseDatabase database;
    private DatabaseReference mFireBaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map_test);
        setContentView(R.layout.activity_track_my_bus_live);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();
        initListeners();

        virtualRouteIdS = SaveCache.getString(TrackMyBusLive.this, ParamUtils.lastVirtualRouteId);
        if (!virtualRouteIdS.equals("")){
            virtualRouteId = virtualRouteIdS;
        }else
            finish();

        /*if(getIntent()!=null){
            if(getIntent().getStringExtra("virtual_Route_id")!=null){
                virtualRouteId = getIntent().getStringExtra("virtual_Route_id");
            }
            if(getIntent().getSerializableExtra("userRouteData")!=null){
                userRouteData = (TripHistorySubModel) getIntent().getSerializableExtra("userRouteData");
            }
            else{
                finish();
            }
        }*/
        /*originId = userRouteData.getOrigin_station();
        destinationId = userRouteData.getDestination_station();*/

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Track My Route");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mapFragment.getMapAsync(TrackMyBusLive.this);

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();

        myLastVirtualRouteId = SaveCache.getString(TrackMyBusLive.this, ParamUtils.lastVirtualRouteId);
        myLastOriginLat = SaveCache.getFloat(TrackMyBusLive.this, ParamUtils.lastOriginLat);
        Log.d("myLastOriginLat", "onCreate: %%%%%%%%%"+myLastOriginLat);
        myLastOriginLong = SaveCache.getFloat(TrackMyBusLive.this, ParamUtils.lastOriginLong);
        Log.d("myLastOriginLong", "onCreate: %%%%%%%%%"+myLastOriginLat);
        myLastDestLat = SaveCache.getFloat(TrackMyBusLive.this, ParamUtils.lastDestLat);
        Log.d("myLastDestLat", "onCreate: %%%%%%%%%"+myLastOriginLat);
        myLastDestLong = SaveCache.getFloat(TrackMyBusLive.this, ParamUtils.lastDestLong);
        Log.d("myLastDestLong", "onCreate: %%%%%%%%%"+myLastOriginLat);
        myLastOriginName = SaveCache.getString(TrackMyBusLive.this, ParamUtils.lastOriginName);
        Log.d("myLastOriginName", "onCreate: %%%%%%%%%"+myLastOriginName);
        myLastDestName = SaveCache.getString(TrackMyBusLive.this, ParamUtils.lastDestName);
        Log.d("myLastDestName", "onCreate: %%%%%%%%%"+myLastDestName);

        ApiManager.getMyStopList(TrackMyBusLive.this, virtualRouteId, myLastDestLat, myLastDestLong, TrackMyBusLive.this, stopsList);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        scanToBoardLayout = findViewById(R.id.scanToBoardLayout);
        scanToBoardRl = findViewById(R.id.scanToBoardRl);
        busNo = findViewById(R.id.busNo);
        busName = findViewById(R.id.busName);
        busStartFromOrigin = findViewById(R.id.busStartFromOrigin);
        busArrivalOnStop = findViewById(R.id.busArrivalOnStop);
        cancelRideLayout = findViewById(R.id.cancelRideLayout);
        helpLayout = findViewById(R.id.helpLayout);
//        cancelHelpLayout = findViewById(R.id.cancelHelpLayout);
        arrowMark = findViewById(R.id.arrowMark);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapGoogle);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetFrag));

        handler = new Handler();
    }

    private void initListeners() {
        scanToBoardLayout.setOnClickListener(TrackMyBusLive.this);
        scanToBoardRl.setOnClickListener(TrackMyBusLive.this);
        cancelRideLayout.setOnClickListener(TrackMyBusLive.this);
        helpLayout.setOnClickListener(TrackMyBusLive.this);
        arrowMark.setOnClickListener(TrackMyBusLive.this);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetState = newState;
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    arrowMark.setImageResource(R.drawable.arrow_point_up);
                } else {
                    arrowMark.setImageResource(R.drawable.arrow_point_down);
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    /*case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;*/
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanToBoardRl:
                startActivity(new Intent(TrackMyBusLive.this, ScanQR.class));
            break;
            case R.id.cancelRideLayout:
                startActivity(new Intent(TrackMyBusLive.this, CancelReschedule.class));
            break;
            case R.id.helpLayout:

            break;
            case R.id.scanToBoardLayout:
                switch (sheetState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    default:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GoogleMapOptions gMapOptions = new GoogleMapOptions();
        gMapOptions.liteMode(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            if (!stopsList.isEmpty() && stopsList.size()>1) {
                setupStartAndEndPoint();
            }
        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ID_AUTH = SaveCache.getString(TrackMyBusLive.this,"Authorization");
    }

    private void setupStartAndEndPoint(){
        List<LatLng> locationPoints = getPoints(stopsList);
        if (locationPoints.size() > 1) {
            locationPoints.clear();
            mMap.clear();
        }

        for(int i=0;i<stopsList.size();i++){
            MyRouteSubData model=stopsList.get(i);
            LatLng position = new LatLng(model.getLatitude(), model.getLongitude());
            // Creating MarkerOptions
             options = new MarkerOptions();
            // Setting the position of the marker
            options.position(position);
            /*Log.d("tag", "position lat: ============================ "+stopsList.get(i).getBus_name());
            Log.d("tag", "position lat: ============================ "+position.latitude);
            Log.d("tag", "myLastOriginLat: ========================== "+Math.round(myLastOriginLat * 1000000d) / 1000000d);
            Log.d("tag", "myLastDestLat: ========================== "+Math.round(myLastDestLat * 1000000d) / 1000000d);
            Log.d("tag", "position.longitude: ============================ "+position.longitude);
            Log.d("tag", "myLastOriginLong: ========================== "+Math.round(myLastOriginLong * 100000d) / 100000d);
            Log.d("tag", "myLastDestLong: ========================== "+Math.round(myLastDestLong * 100000d) / 100000d);
            if ((position.latitude== Math.round(myLastOriginLat * 1000000d) / 1000000d) & (position.longitude == Math.round(myLastOriginLong * 100000d) / 100000d)){
                Log.d("tag", "setupStartAndEndPoint: ============================ myLastOriginLat");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else if (position.latitude== Math.round(myLastDestLat * 1000000d) / 1000000d & position.longitude== Math.round(myLastDestLong * 100000d) / 100000d){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                Log.d("tag", "setupStartAndEndPoint: ================= myLastDestLat");
            }*/
            if(i==0||i==stopsList.size()-1){
                locationPoints.add(position);

                if (locationPoints.size() == 1) {
                    origin = position;
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(model.getBus_name());
//                    sb.append(position.latitude).append(",").append(position.longitude).append("|");
                } else if (locationPoints.size() == 2) {
                    destination = position;
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(model.getBus_name());
//                    sb.append(position.latitude).append(",").append(position.longitude);
                }
                options.title(model.getBus_name());
                mMap.addMarker(options);
                pointsList.add(position);
            }
            else{
                mMap.addMarker(new MarkerOptions().position(position).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(model.getBus_name()));
                pointsList.add(position);
//                sb.append(position.latitude).append(",").append(position.longitude).append("|");
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pointsList.get(0), 15));
        if (origin != null & destination != null) {
            createNewChange();
        }
    }

    private List<LatLng> getPoints(List<MyRouteSubData> mLocations){
        List<LatLng> points = new ArrayList<>();
        for(MyRouteSubData mLocation : mLocations){
            points.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        }
        return points;
    }

    private void createNewChange() {
        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD794KKI3H8lpSPGufCEnRIPXNE9bf-IXw")
                .build();
        for (int m = 0; m< pointsList.size()-1;m++) {
            LatLng position= null;
            position = new LatLng(pointsList.get(m).latitude, pointsList.get(m).longitude);
            LatLng position1= null;
            position1 = new LatLng(pointsList.get(m+1).latitude, pointsList.get(m+1).longitude);
            DirectionsApiRequest req = DirectionsApi.getDirections(context, position.latitude + "," + position.longitude, position1.latitude + "," + position1.longitude);
            try {
                DirectionsResult res = req.mode(TravelMode.DRIVING)/*.waypoints(String.valueOf(sb)*//*"17.44027,78.39431", "17.43149,78.38817"*//*)*/.await();
                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j = 0; j < leg.steps.length; j++) {
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length > 0) {
                                        for (int k = 0; k < step.steps.length; k++) {
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
//                Log.e("tag", ex.getLocalizedMessage());
            }

            //Draw the polyline
            if (path.size() > 0) {
                PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(10);
                mMap.addPolyline(opts);
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pointsList.get(0), 15));

        startBusAnimation();

//        startCarAnimation(origin.latitude, origin.longitude);
    }

    private void startBusAnimation() {
        mFireBaseRef = database.getReference(virtualRouteId);

        mFireBaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    try {
                        FireBaseDbModel model = dataSnapshot.getValue(FireBaseDbModel.class);
                        if (model != null){
                            LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());
                            carMarker = mMap.addMarker(new MarkerOptions().position(latLng).
                                    flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));
                            /*LatLng newPos = new LatLng(model.getLatitude(), model.getLongitude());
                            carMarker.setPosition(newPos);*/
                            carMarker.setAnchor(0.5f, 0.5f);
                            carMarker.setRotation(MapUtils.getBearing(startPosition, latLng));
                            mMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition
                                            (new CameraPosition.Builder()
                                                    .target(latLng)
                                                    .zoom(15.5f)
                                                    .build()));
                        }

                    } catch (Exception ex) {
                        Log.e("Content Values: ", ex.getMessage());
                    }
                }

                if (dataSnapshot.getValue() != null) {
                    try {
                        FireBaseDbModel model = dataSnapshot.getValue(FireBaseDbModel.class);
                        if (model != null){
                            LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());
                            carMarker = mMap.addMarker(new MarkerOptions().position(latLng).
                                    flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));
                        }

                    } catch (Exception ex) {
                        Log.e("Content Values: ", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(@NotNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Content Values: ", databaseError.getMessage());
            }
        });
    }

  /*  public static void startCarAnimation(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        carMarker = mMap.addMarker(new MarkerOptions().position(latLng).
                flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));


        index = -1;
        next = 1;
        handler.postDelayed(staticCarRunnable, 2000);
    }*/

    Runnable staticCarRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("", "staticCarRunnable handler called...");
            if (index < (path.size() - 1)) {
                index++;
                next = index + 1;
            } else {
                index = -1;
                next = 1;
                stopRepeatingTask();
                return;
            }

            if (index < (path.size() - 1)) {
//                startPosition = polyLineList.get(index);
                startPosition = carMarker.getPosition();
                endPosition = path.get(next);
            }

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(2000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(valueAnimator1 -> {

//                    Log.i(TAG, "Car Animation Started...");

                v = valueAnimator1.getAnimatedFraction();
                lng = v * endPosition.longitude + (1 - v)
                        * startPosition.longitude;
                lat = v * endPosition.latitude + (1 - v)
                        * startPosition.latitude;
                LatLng newPos = new LatLng(lat, lng);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5f, 0.5f);
                carMarker.setRotation(MapUtils.getBearing(startPosition, newPos));
                mMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition
                                (new CameraPosition.Builder()
                                        .target(newPos)
                                        .zoom(15.5f)
                                        .build()));


            });
            valueAnimator.start();
            handler.postDelayed(this, 3000);

        }
    };

    void stopRepeatingTask() {

        if (staticCarRunnable != null) {
            handler.removeCallbacks(staticCarRunnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRepeatingTask();
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
        finish();
    }
}
