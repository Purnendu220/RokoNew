package com.rokoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.adapter.RecyclerAdapterRoute;
import com.rokoapp.listener.BookingListener;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.mapUtils.MapTest;
import com.rokoapp.model.response.BookingData;
import com.rokoapp.model.response.RouteFinderModel;
import com.rokoapp.model.response.TripHistorySubModel;
import com.rokoapp.utils.ParamUtils;
import com.rokoapp.utils.SaveCache;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS;
import static com.rokoapp.utils.ParamUtils.COMPLIMENTARY_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.ID_AUTH;
import static com.rokoapp.utils.ParamUtils.PREFS_NAME;
import static com.rokoapp.utils.ParamUtils.TOTAL_PASS;
import static com.rokoapp.utils.ParamUtils.USER_COMPLEMENT_PASS_VALIDITY;
import static com.rokoapp.utils.ParamUtils.USER_COMPLIMENT_PASS;
import static com.rokoapp.utils.ParamUtils.USER_TOTAL_PASS;

public class SelectShortestRoute extends AppCompatActivity implements ResponseProgressListener, BookingListener, View.OnClickListener {

    private Toolbar toolbar;
    private Button viewRoute, buyRoutePass, bookRide;
    public RecyclerView locationRecycler;
    private TextView pickupLocation, pickupPointDistance, dropLocation, dropPointDistance, errorMessage;
    private TextView textSource, textDest, textRouteCount, totalSeats, availableSeats, dateBusStart, originTime, destTime;
    private ImageView listIcon;
    private LinearLayout lLTimeSchedule, errorLayout;
    private RelativeLayout routeListLayout;

    private int sheetState;
//    private TripHistorySubModel data = null;
    private BookingData data = null;
    private ArrayList<RouteFinderModel> routeFinderList = new ArrayList<>();
    public BottomSheetBehavior mBottomSheetBehavior;
    private String source, dest, virtualRouteId = null, originStationId = null, destStationId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shortest_route);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ID_AUTH = SaveCache.getString(SelectShortestRoute.this,"Authorization");
        source = getIntent().getStringExtra("source");
        dest = getIntent().getStringExtra("dest");
        routeFinderList = (ArrayList<RouteFinderModel>) getIntent().getSerializableExtra("routeList");

        initViews();
        initListeners();
        errorLayout.setVisibility(View.GONE);
        showData();
    }

    private void initViews() {
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        locationRecycler = findViewById(R.id.locationRecycler);
        lLTimeSchedule = findViewById(R.id.lLTimeSchedule);
        errorLayout = findViewById(R.id.errorLayout);
        viewRoute = findViewById(R.id.viewRoute);
        toolbar = findViewById(R.id.toolbarShort);
        textSource = findViewById(R.id.textSource);
        textDest = findViewById(R.id.textDest);
        textRouteCount = findViewById(R.id.textRouteCount);
        totalSeats = findViewById(R.id.totalSeats);
        availableSeats = findViewById(R.id.availableSeats);
        dateBusStart = findViewById(R.id.dateBusStart);
        originTime = findViewById(R.id.originTime);
        destTime = findViewById(R.id.destTime);
        pickupLocation = findViewById(R.id.pickupLocation);
        pickupPointDistance = findViewById(R.id.pickupPointDistance);
        dropLocation = findViewById(R.id.dropLocation);
        dropPointDistance = findViewById(R.id.dropPointDistance);
        errorMessage = findViewById(R.id.errorMessage);
        listIcon = findViewById(R.id.listIcon);
        buyRoutePass = findViewById(R.id.buyRoutePass);
        bookRide = findViewById(R.id.bookRide);
        routeListLayout = findViewById(R.id.routeListLayout);
    }

    private void initListeners() {
        routeListLayout.setOnClickListener(SelectShortestRoute.this);
        viewRoute.setOnClickListener(SelectShortestRoute.this);
        lLTimeSchedule.setOnClickListener(SelectShortestRoute.this);
        buyRoutePass.setOnClickListener(SelectShortestRoute.this);
        bookRide.setOnClickListener(SelectShortestRoute.this);
        // Capturing the callbacks for bottom sheet
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetState = newState;
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    listIcon.setImageResource(R.drawable.up_arrow);
                } else {
                    listIcon.setImageResource(R.drawable.down_arrow);
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
                        routeListLayout.setVisibility(View.VISIBLE);
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.routeListLayout:
                switch (sheetState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        routeListLayout.setVisibility(View.VISIBLE);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    /*case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;*/
                    default:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
                break;
            case R.id.viewRoute:
//                Toast.makeText(SelectShortestRoute.this,"Map will implement.", Toast.LENGTH_LONG).show();
                Intent j = new Intent(SelectShortestRoute.this, MapTest.class);
                // j.putExtra("userRouteData",passModel);
                j.putExtra("virtual_Route_id",virtualRouteId);

                startActivity(j);
                break;
            /*case R.id.lLTimeSchedule:
                getBookingConfirm();
                break;*/
            case R.id.bookRide:
                getBookingConfirm();
                break;
            case R.id.buyRoutePass:
                if (virtualRouteId != null & originStationId != null & destStationId != null){
                    Intent i = new Intent(SelectShortestRoute.this, PassesList.class);
                    i.putExtra("virtualRouteId", virtualRouteId);
                    i.putExtra("originStationId", originStationId);
                    i.putExtra("destStationId", destStationId);
                    startActivity(i);
                    SelectShortestRoute.this.finish();
                }
                break;
        }
    }

    private void showData() {
        textSource.setText(source);
        textDest.setText(dest);

        RouteFinderModel routeModel = routeFinderList.get(0);
        selectMyRoute(routeModel.getTotalSeats(), routeModel.getAvailableSeats(), routeModel.getOriginStation(), routeModel.getDistanceOrigin(), routeModel.getDestinationStation(), routeModel.getDistanceDestination(), routeModel.getOriginTime(), routeModel.getDestinationTime(), routeModel.getRouteDateAndTime(), routeModel.getVirtualRouteId(), routeModel.getOrigin_station_id(), routeModel.getDestinationStationId());


        locationRecycler.setHasFixedSize(true);
        RecyclerAdapterRoute adapterImage = new RecyclerAdapterRoute(SelectShortestRoute.this, routeFinderList, this);
        locationRecycler.setLayoutManager(new LinearLayoutManager(SelectShortestRoute.this, LinearLayoutManager.VERTICAL, false));
        locationRecycler.setItemAnimator(new DefaultItemAnimator());
        locationRecycler.setAdapter(adapterImage);

        int listCount = routeFinderList.size();
        if (listCount==0){
            textRouteCount.setText("No Route Found.");
        }else if (listCount ==1){
            textRouteCount.setText(listCount+" Route Found.");
        }else
            textRouteCount.setText(listCount+" Routes Found.");

        listIcon.setImageResource(R.drawable.up_arrow);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void selectMyRoute(String totalSeat, String availableSeat, String originStation, String distanceOrigin, String destinationStation, String distanceDestination, String timeOrigin, String timeDestination, String routeDateAndTime, String virtualRouteId, String originStationId, String destinationStationId) {

        this.virtualRouteId = virtualRouteId;
        this.originStationId = originStationId;
        this.destStationId = destinationStationId;

        totalSeats.setText(totalSeat);
        availableSeats.setText(availableSeat);
        pickupLocation.setText(originStation);

        Float number=Float.parseFloat(distanceOrigin);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String sourceDistance = df.format(number);

        pickupPointDistance.setText(sourceDistance+" Kms.");
        dropLocation.setText(destinationStation);

        Float numberDest = Float.parseFloat(distanceDestination);
        DecimalFormat dfN = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String destDistance = dfN.format(numberDest);
        dropPointDistance.setText(destDistance+" Kms.");

        dateBusStart.setText(ParamUtils.changeDateFormat(routeDateAndTime));
        if (timeOrigin != null) {
            originTime.setText("Bus will arrive\nat: " + ParamUtils.getOriginTime(routeDateAndTime, timeOrigin) + " ");
        }
        if (timeDestination != null) {
            destTime.setText("You will reach on Destination\nby: " + ParamUtils.getOriginTime(routeDateAndTime, timeDestination) + " ");
        }
    }



    private void getBookingConfirm() {
        if (virtualRouteId !=null & originStationId != null & destStationId != null) {
            ApiManager.getBookingOfUser(SelectShortestRoute.this, virtualRouteId, originStationId, destStationId, ParamUtils.getCurrentDate(),  this, this);
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
        if (sheetState == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (sheetState == BottomSheetBehavior.STATE_COLLAPSED) {
            startActivity(new Intent(SelectShortestRoute.this, MapsActivity.class));
            SelectShortestRoute.this.finish();
        }
    }



    @Override
    public void getBookingData(BookingData data) {
        this.data = data;
    }

    @Override
    public void onResponseInProgress() {
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            USER_COMPLIMENT_PASS = SaveCache.getString(SelectShortestRoute.this, COMPLIMENTARY_PASS);
            USER_COMPLEMENT_PASS_VALIDITY = SaveCache.getString(SelectShortestRoute.this, COMPLIMENTARY_PASS_VALIDITY);
            USER_TOTAL_PASS = SaveCache.getString(SelectShortestRoute.this, TOTAL_PASS);

            Intent j = new Intent(SelectShortestRoute.this, BookingConfirm.class);
//            j.putExtra("userRouteData", data);
            j.putExtra("originStation", data.getOrigin_station_name());
            startActivity(j);
            SelectShortestRoute.this.finish();


            /*Intent j = new Intent(SelectShortestRoute.this, TrackMyBusLive.class);
            j.putExtra("userRouteData", data);
            j.putExtra("virtual_Route_id", data.getVirtualRouteId());
            startActivity(j);
            SelectShortestRoute.this.finish();*/
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
                errorLayout.setVisibility(View.VISIBLE);
                routeListLayout.setVisibility(View.GONE);
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                Toast.makeText(this, "Unable to connect; please Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_0:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case SIGNATURE_EXPIRE:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
//                startActivity(new Intent(MapsActivity.this, Login.class));
                startActivity(new Intent(SelectShortestRoute.this, MobileVerification.class));
                SelectShortestRoute.this.finish();
                break;
        }
    }
    /*private void launchBottomSheet() {
        SourceToDestination sourceToDestination = new SourceToDestination();
        Bundle bundle = new Bundle();
        bundle.putSerializable("routeList", routeFinderList);
        sourceToDestination.setArguments(bundle);
        sourceToDestination.show(getSupportFragmentManager(), "add_dialog_fragment");

    }*/
}
