package com.rokoapp.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rokoapp.R;
import com.rokoapp.adapter.RouteStopsAdapter;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.mapUtils.GPSTracker;
import com.rokoapp.model.DirectionsJSONParser;
import com.rokoapp.model.response.MyRouteSubData;
import com.rokoapp.model.response.RouteStopData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrackMyBus extends AppCompatActivity implements OnMapReadyCallback, ResponseProgressListener, View.OnClickListener {

    private Toolbar toolbar;
    public BottomSheetBehavior mBottomSheetBehavior;
    private ImageView listIcon;
    private RecyclerView locationRecycler;
    private TextView textRouteCount;
    private RelativeLayout routeListLayout;
    private SupportMapFragment mapFragment;

    private GoogleMap mMap;
    GPSTracker gpsTracker;
    List<MyRouteSubData> stopsList = new ArrayList<>();
    private String virtualRouteId = "315";
    private int sheetState;
    ArrayList markerPoints= new ArrayList();
    ArrayList<LatLng> pointsList = new ArrayList<>();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    int marketPosition=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_my_bus);
        initViews();
        gpsTracker = new GPSTracker(this);

        if(getIntent()!=null){
            if(getIntent().getStringExtra("virtual_Route_id")!=null){
                virtualRouteId = getIntent().getStringExtra("virtual_Route_id");
            }
        }
        mapFragment.getMapAsync(TrackMyBus.this);
        ApiManager.getRouteStopList(TrackMyBus.this, virtualRouteId,TrackMyBus.this, stopsList);
        if (toolbar != null) {
            //setActionBar(toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Route Detail");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        locationRecycler = findViewById(R.id.locationRecycler);
        listIcon = findViewById(R.id.listIcon);
        textRouteCount = findViewById(R.id.textRouteCount);
        routeListLayout = findViewById(R.id.routeListLayout);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        initListeners();
    }

    private void initListeners() {
        routeListLayout.setOnClickListener(TrackMyBus.this);

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

    private void showData() {
        locationRecycler.setHasFixedSize(true);
        RouteStopsAdapter adapterImage = new RouteStopsAdapter(TrackMyBus.this, stopsList);
        locationRecycler.setLayoutManager(new LinearLayoutManager(TrackMyBus.this, LinearLayoutManager.VERTICAL, false));
        locationRecycler.setItemAnimator(new DefaultItemAnimator());
        locationRecycler.setAdapter(adapterImage);

        int listCount = stopsList.size();
        if (listCount==0){
            textRouteCount.setText("No stops");
        }else if (listCount ==1){
            textRouteCount.setText(listCount+" Stop");
        }else
            textRouteCount.setText(listCount+" Stops");

        listIcon.setImageResource(R.drawable.up_arrow);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


            }
        });
    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            if (!stopsList.isEmpty() && stopsList.size()>1) {
                setupStartAndEndPoint();
                showData();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng) markerPoints.get(0), 14));
            }
        }
    }

    private void setupStartAndEndPoint(){

        if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }

        for(int i=0;i<stopsList.size();i++){
            MyRouteSubData model=stopsList.get(i);
            LatLng position = new LatLng(model.getLatitude(), model.getLongitude());

            if(i==0||i==stopsList.size()-1){
                markerPoints.add(position);
// Adding new item to the ArrayList

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(position);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                options.title(model.getBus_name());
                mMap.addMarker(options);
                pointsList.add(position);

            }
            else{
                mMap.addMarker(new MarkerOptions().position(position).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(model.getBus_name()));
                pointsList.add(position);
            }
        }
        if (markerPoints.size() >= 2) {
            LatLng origin = (LatLng) markerPoints.get(0);
            LatLng dest = (LatLng) markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            // getUpdateAfterInterval();

        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.routeListLayout:
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

    private class DownloadTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }


    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            Log.e("Path Result","Result Size "+result.size());

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.GREEN);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(lineOptions!=null){
                mMap.addPolyline(lineOptions);

            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key= "key="+getResources().getString(R.string.google_directions_key);

        String waypoint= "";

        for(int i=0;i<pointsList.size();i++){
            LatLng point=pointsList.get(i);
            if(i==0){ waypoint = "waypoints="; }
            waypoint += point.latitude + "," + point.longitude + "|";
        }


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + waypoint + "&" + key;


        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private void setMarker(MyRouteSubData model) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String key = "Tracking";
                double lat = model.getLatitude();
                double lng = model.getLongitude();
                LatLng location = new LatLng(lat, lng);
                if (!mMarkers.containsKey(key)) {

                    mMarkers.put(key, mMap.addMarker(new MarkerOptions().position(location).icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));


                } else {

                    mMarkers.get(key).setPosition(location);


                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkers.values()) {
                    builder.include(marker.getPosition());
                }
                //  mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
            }
        });

    }

    private void getUpdateAfterInterval(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(marketPosition<pointsList.size()){
                    setMarker (stopsList.get(marketPosition));
                    marketPosition +=1;
                    Log.e("Marker Update","Marker Position "+marketPosition);

                }
            }
        }, 0, 2000);
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
