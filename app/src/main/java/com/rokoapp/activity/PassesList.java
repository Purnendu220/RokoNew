package com.rokoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.adapter.PassesAdapter;
import com.rokoapp.manageApi.ApiManager;
import com.rokoapp.manageApi.FailureCodes;
import com.rokoapp.manageApi.ResponseProgressListener;
import com.rokoapp.model.response.PassModel;
import com.rokoapp.utils.ParamUtils;

import java.util.ArrayList;
import java.util.List;

import static com.rokoapp.utils.ParamUtils.PREFS_NAME;

public class PassesList extends AppCompatActivity implements ResponseProgressListener {

    private Toolbar toolbar;
    private RecyclerView passesRecycler;
    private List<PassModel> passListing = new ArrayList<>();
    private PassesAdapter passesAdapter;
    private BottomSheetBehavior mBottomSheetBehavior;

    private int sheetState;
    private ImageView titleT, bannerPassDesc;
    private TextView passDescText, selectPassByRouteDesc, noOfRideInPassDesc, usageOfRideDesc;
    private RelativeLayout passDescHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passes_list);
        initViews();

        Intent j = getIntent();
        ParamUtils.virtualRouteIdS = j.getStringExtra("virtualRouteId");
        ParamUtils.originStationIdS = j.getStringExtra("originStationId");
        ParamUtils.destStationIdS = j.getStringExtra("destStationId");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Buy passes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        passesRecycler.setHasFixedSize(true);
        passesAdapter = new PassesAdapter(PassesList.this, passListing, PassesList.this);
        passesRecycler.setLayoutManager(new LinearLayoutManager(PassesList.this, LinearLayoutManager.VERTICAL, false));
        passesRecycler.setItemAnimator(new DefaultItemAnimator());
        passesRecycler  .setAdapter(passesAdapter);

        ApiManager.getPassesData(PassesList.this, passListing, PassesList.this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        loadData();
    }

    private void loadData() {
//        whatPassDesc.setText(Html.fromHtml(getResources().getString(R.string.purchase_pass_desc)));
        passDescText.setText(Html.fromHtml(getResources().getString(R.string.what_is_pass_desc)));
        selectPassByRouteDesc.setText(Html.fromHtml(getResources().getString(R.string.select_pass_for_route_desc)));
        noOfRideInPassDesc.setText(Html.fromHtml(getResources().getString(R.string.number_of_rides_in_each_pass)));
        usageOfRideDesc.setText(Html.fromHtml(getResources().getString(R.string.usage_of_rides)));

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        passesRecycler = findViewById(R.id.passesRecycler);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.passDescLayout));

        passDescHeader = findViewById(R.id.passDescHeader);
        titleT = findViewById(R.id.titleT);
        passDescText = findViewById(R.id.passDescText);
        selectPassByRouteDesc = findViewById(R.id.selectPassByRouteDesc);
        noOfRideInPassDesc = findViewById(R.id.noOfRideInPassDesc);
        usageOfRideDesc = findViewById(R.id.usageOfRideDesc);
        bannerPassDesc = findViewById(R.id.bannerPassDesc);

    }

    @Override
    public void onResponseInProgress() {

    }

    @Override
    public void onResponseCompleted(int i) {
        if (i==1){
            passesAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onResponseFailed(FailureCodes code) {
        switch (code) {
            case SERVER_ERROR:
                Toast.makeText(this, "Server Busy, Try Again Later.", Toast.LENGTH_SHORT).show();
                break;
            case NO_INTERNET:
                Toast.makeText(this, "No internet ", Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_NULL:
                Toast.makeText(this, "Unable to connect; please Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_0:
                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                break;
            case SIGNATURE_EXPIRE:
//                Toast.makeText(this, "Kindly Try Again.", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
//                startActivity(new Intent(MapsActivity.this, Login.class));
                startActivity(new Intent(PassesList.this, MobileVerification.class));
                PassesList.this.finish();
                break;
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
        startActivity(new Intent(PassesList.this, MapsActivity.class));
        PassesList.this.finish();
    }
}
