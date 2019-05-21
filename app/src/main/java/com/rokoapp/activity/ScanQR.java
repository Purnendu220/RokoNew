package com.rokoapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.rokoapp.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class ScanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Toolbar toolbar;
    private ZXingScannerView qrCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        initViews();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Scan to Start Trip");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setScannerProperties();
    }

    private void setScannerProperties() {
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.yellow);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
//        if (Build.MANUFACTURER.equals(HUAWEI, ignoreCase = true))
            qrCodeScanner.setAspectTolerance(0.5f);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        qrCodeScanner = findViewById(R.id.qrCodeScanner);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 200);
                    return;
                }
            }
            qrCodeScanner.startCamera();
            qrCodeScanner.setResultHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult != null) {
            Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG).show();
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
        /*startActivity(new Intent(PassesList.this, MapsActivity.class));
        PassesList.this.finish();*/
        finish();
    }
}
