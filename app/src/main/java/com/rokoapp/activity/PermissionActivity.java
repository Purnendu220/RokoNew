package com.rokoapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;

import com.rokoapp.R;
import com.rokoapp.utils.SaveCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionActivity extends AppCompatActivity {

    private Button buttonPermission;
    private WebView tvPermissionOne, tvPermissionTwo, tvPermissionThree, tvPermissionFour, tvPermissionFive;

    private static final int PERMISSION_REQUEST_CODE = 200;
    String[] appPermission = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        initViews();

        if (SaveCache.getBoolean(this, "first")){
            if (checkAndRequestPermission()){
                navigateToLoginScreen();
            }
        }

        tvPermissionOne.setVerticalScrollBarEnabled(false);
        tvPermissionOne.loadData(getString(R.string.manage_phone_call_permission), "text/html; charset=utf-8", "utf-8");

        tvPermissionTwo.setVerticalScrollBarEnabled(false);
        tvPermissionTwo.loadData(getString(R.string.access_contact_permission), "text/html; charset=utf-8", "utf-8");

        tvPermissionThree.setVerticalScrollBarEnabled(false);
        tvPermissionThree.loadData(getString(R.string.access_sms_permission), "text/html; charset=utf-8", "utf-8");

        tvPermissionFour.setVerticalScrollBarEnabled(false);
        tvPermissionFour.loadData(getString(R.string.access_location_permission), "text/html; charset=utf-8", "utf-8");

        tvPermissionFive.setVerticalScrollBarEnabled(false);
        tvPermissionFive.loadData(getString(R.string.camera_permission), "text/html; charset=utf-8", "utf-8");

        buttonPermission.setOnClickListener(v ->{
                SaveCache.save(this, "first", true);
                checkAndRequestPermission();
        });

    }

    private boolean checkAndRequestPermission() {
        List<String> listPermissionNeeded = new ArrayList<>();

        for (String perm : appPermission){
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionNeeded.add(perm);
            }
        }
        if (!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), PERMISSION_REQUEST_CODE);

            return false;
        }
        navigateToLoginScreen();
        return true;
    }

    private void navigateToLoginScreen() {
//        Intent in = new Intent(PermissionActivity.this, Login.class);
        Intent in = new Intent(PermissionActivity.this, MobileVerification.class);
        startActivity(in);
        PermissionActivity.this.finish();
    }

    private void initViews() {
        tvPermissionOne = findViewById(R.id.tvPermissionOne);
        tvPermissionTwo = findViewById(R.id.tvPermissionTwo);
        tvPermissionThree = findViewById(R.id.tvPermissionThree);
        tvPermissionFour = findViewById(R.id.tvPermissionFour);
        tvPermissionFive = findViewById(R.id.tvPermissionFive);
        buttonPermission = findViewById(R.id.buttonPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSION_REQUEST_CODE){
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i=0; i<grantResults.length; i++){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            if (deniedCount==0){
                navigateToLoginScreen();
            }
            else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();
//                    int permResult = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)){
                        showMessageOKCancel((dialog, which) -> checkAndRequestPermission());
                        return;
                    }
                    else {
                        new AlertDialog.Builder(PermissionActivity.this)
                                .setMessage("You have denied some permissions. Allow all permissions at [Settings] > [Permissions]")
                                .setPositiveButton("Go to Settings", (dialog, which) -> {
                                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                })
                                .setNegativeButton("No, Exit App", (dialog, id) -> {
                                    dialog.cancel();
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                })
                                .create()
                                .show();
                    }
                    break;

                }
            }
        }

    }



    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PermissionActivity.this)
                .setMessage("Roko App needs Call, Contacts, SMS, Location and Camera permissions to work without problem.")
                .setPositiveButton("Yes, Grant Permissions", okListener)
                .setNegativeButton("No, Exit App", (dialog, id) -> {
                    dialog.cancel();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .create()
                .show();
    }


    /*public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
            int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
            int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
            int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
            if (result == PackageManager.PERMISSION_GRANTED & result1 == PackageManager.PERMISSION_GRANTED & result2 == PackageManager.PERMISSION_GRANTED & result3 == PackageManager.PERMISSION_GRANTED & result4 == PackageManager.PERMISSION_GRANTED){
                Log.d("permissions","Permission is granted====================  already given button check");
            }
            else {
                Log.d("permissions","Permission is revoked===================== button check");
                ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, READ_CONTACTS, WRITE_CONTACTS, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d("permissions","Permission is granted================================ automatically <23");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean phoneState = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readContact = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeContacts = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean fineLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean coarseLocation = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (phoneState && readContact && writeContacts && fineLocation && coarseLocation) {
                        Log.d("", "onRequestPermissionsResult: " + "======================== override Permission Granted.");
//                        Toast.makeText(Login.this, "Permission Granted, Now you can access to Write and Read data.", Toast.LENGTH_LONG).show();
                        navigateToLoginScreen();
                    } else {
                        Log.d("", "onRequestPermissionsResult: " + "======================== override Permission revoked.");
//                        Toast.makeText(Login.this, "Permission Denied, You cannot access to Write and Read data.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                                showMessageOKCancel("You need to allow permissions to serve you BETTER.",
                                        (dialog, which) -> requestPermissions(new String[]{READ_PHONE_STATE, READ_CONTACTS, WRITE_CONTACTS, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE));
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.cancel();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .create()
                .show();
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
