package com.rokoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.rokoapp.R;
import com.rokoapp.utils.ParamUtils;

public class ReferAndEarn extends AppCompatActivity {

    private Toolbar toolbar;
    private Button inviteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);
        initViews();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.refer_earn);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inviteButton.setOnClickListener( v ->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Roko App at: https://play.google.com/store/apps/details?id="+getPackageName()+" Use my refer code:"+ ParamUtils.MOBILE_USER+" and earn 2 rides free on Registration.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        inviteButton = findViewById(R.id.inviteButton);
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
        startActivity(new Intent(ReferAndEarn.this, MapsActivity.class));
        ReferAndEarn.this.finish();
    }
}
