package com.rokoapp.activity;

import android.content.Intent;
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

import com.rokoapp.R;
import com.rokoapp.adapter.NotifyAdapter;
import com.rokoapp.dbHelper.RokoDbAdapter;
import com.rokoapp.model.NotifyModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Notifications extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView notifyRecycler;
    private Button clearNotify;
    public NotifyAdapter notifyAdapter;
    public List<NotifyModel> notificationList = new ArrayList<>();
    private RokoDbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initViews();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("My Notifications");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbAdapter = new RokoDbAdapter(Notifications.this);

        insertData();

        notifyRecycler.setHasFixedSize(true);
        notifyRecycler.setLayoutManager(new LinearLayoutManager(Notifications.this, LinearLayoutManager.VERTICAL, false));
        notifyRecycler.setItemAnimator(new DefaultItemAnimator());

        dbAdapter.readLocalNotifications(notificationList);
        notifyAdapter = new NotifyAdapter(Notifications.this, notificationList, dbAdapter, this);
        notifyRecycler  .setAdapter(notifyAdapter);

        if (notificationList.size() != 0 && notificationList.size()>1){
            clearNotify.setVisibility(View.VISIBLE);
        }else {
            clearNotify.setVisibility(View.GONE);
        }
        clearNotify.setOnClickListener(v -> {
            dbAdapter.deleteAllNotification();
            notificationList.clear();
            dbAdapter.readLocalNotifications(notificationList);
            notifyAdapter.notifyDataSetChanged();
            clearNotify.setVisibility(View.GONE);
        });
    }

    private void insertData() {
        List<NotifyModel> notifyModelList = new ArrayList<>();
        notifyModelList.add(new NotifyModel(0,"New Notification 1","This is the notification description. which is used to define dummy data 1.",""));
        notifyModelList.add(new NotifyModel(0,"New Notification 2","This is the notification description. which is used to define dummy data 2.",""));
        notifyModelList.add(new NotifyModel(0,"New Notification 3","This is the notification description. which is used to define dummy data 3.",""));
        notifyModelList.add(new NotifyModel(0,"New Notification 4","This is the notification description. which is used to define dummy data 4.",""));
        notifyModelList.add(new NotifyModel(0,"New Notification 5","This is the notification description. which is used to define dummy data 5.",""));
        notifyModelList.add(new NotifyModel(0,"New Notification 6","This is the notification description. which is used to define dummy data 6.",""));
        notifyModelList.add(new NotifyModel(0,"New Notification 7","This is the notification description. which is used to define dummy data 7.",""));
        long homeId = dbAdapter.insertNewNotification(notifyModelList);
        if (homeId < 0) {
            Log.d(TAG, "onResponse: ===========" + "Something Went Wrong");
        } else {
            Log.d(TAG, "onResponse: ===========" + "Everything is ok");
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        clearNotify = findViewById(R.id.clearNotify);
        notifyRecycler = findViewById(R.id.notifyRecycler);
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
//        startActivity(new Intent(Notifications.this, Home.class));
        startActivity(new Intent(Notifications.this, MapsActivity.class));
        Notifications.this.finish();
    }
}
