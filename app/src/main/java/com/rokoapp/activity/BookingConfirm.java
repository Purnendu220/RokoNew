package com.rokoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rokoapp.R;

public class BookingConfirm extends AppCompatActivity {

    private TextView textDesc, buttonGotIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);
        initViews();

        String originStation = getIntent().getStringExtra("originStation");
        textDesc.setText("Your Roko ride will arrive at "+originStation+". Please reach there 5 min before your boarding time.");

//        Your Roko ride will arrive at Incedo / Intellect T Point. Please reach there 5 min before your boarding time.

        buttonGotIt.setOnClickListener(v -> {
            startActivity(new Intent(BookingConfirm.this, MapsActivity.class));
            finish();
        });

    }

    private void initViews() {
        textDesc = findViewById(R.id.textDesc);
        buttonGotIt = findViewById(R.id.buttonGotIt);
    }

}
