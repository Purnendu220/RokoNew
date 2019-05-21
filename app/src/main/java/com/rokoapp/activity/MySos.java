package com.rokoapp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.rokoapp.R;
import com.rokoapp.adapter.ContactAdapter;
import com.rokoapp.dbHelper.RokoDbAdapter;
import com.rokoapp.model.ContactList;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.ContentValues.TAG;

public class MySos extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView contactRecycler;
    public LinearLayout layoutAdd;

    static final int PICK_CONTACT=1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String contactNumber, name;

    private RokoDbAdapter dbAdapter;
    public List<ContactList> contactLists = new ArrayList<>();
    public ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sos);
        initViews();
        isStoragePermissionGranted();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("My SOS List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbAdapter = new RokoDbAdapter(MySos.this);

        contactRecycler.setHasFixedSize(true);
        contactRecycler.setLayoutManager(new LinearLayoutManager(MySos.this, LinearLayoutManager.VERTICAL, false));
        contactRecycler.setItemAnimator(new DefaultItemAnimator());

        dbAdapter.readLocalNewHomeDataOne(adapter, contactLists, layoutAdd);
        adapter = new ContactAdapter(MySos.this, contactLists, dbAdapter, this);
        contactRecycler.setAdapter(adapter);

        layoutAdd.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        });

    }


    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {

                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            contactNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:"+contactNumber);
                        }
                        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        List<ContactList> listData = new ArrayList<>();
                        listData.add(new ContactList(name,contactNumber));
                        long homeId = dbAdapter.insertNewHomeData(listData);
                        if (homeId < 0) {
                            Log.d(TAG, "onResponse: ===========" + "Something Went Wrong");
                        } else {
                            Log.d(TAG, "onResponse: ===========" + "Everything is ok");
                        }
                    }
                }
                contactLists.clear();
                dbAdapter.readLocalNewHomeDataOne(adapter, contactLists, layoutAdd);
                adapter = new ContactAdapter(MySos.this, contactLists, dbAdapter, this);
                contactRecycler.setAdapter(adapter);
                break;
        }
    }
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        contactRecycler =  findViewById(R.id.contactRecycler);
        layoutAdd =  findViewById(R.id.layoutAdd);
    }


    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
            if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ){
                Log.v("permissions","Permission is granted");
            }
            else {
                Log.v("permissions","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS, WRITE_CONTACTS}, PERMISSION_REQUEST_CODE);
//                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permissions","Permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readContacts = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeContacts = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (readContacts && writeContacts)
                        Log.d("", "onRequestPermissionsResult: "+"Permission Granted.");
//                        Toast.makeText(Login.this, "Permission Granted, Now you can access to Write and Read data.", Toast.LENGTH_LONG).show();
                    else {

//                        Toast.makeText(Login.this, "Permission Denied, You cannot access to Write and Read data.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_CONTACTS)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        (dialog, which) -> requestPermissions(new String[]{READ_CONTACTS, WRITE_CONTACTS}, PERMISSION_REQUEST_CODE));
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MySos.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .create()
                .show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                MySos.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
