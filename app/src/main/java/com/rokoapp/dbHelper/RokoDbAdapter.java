package com.rokoapp.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.rokoapp.adapter.ContactAdapter;
import com.rokoapp.mapUtils.responseModel.SearchHistoryModel;
import com.rokoapp.model.ContactList;
import com.rokoapp.model.NotifyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RokoDbAdapter {

    private HomeDbHelper homeDbHelper;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "roko_db";
    private static final String CONTACT_NEW = "contact_new";
    private static final String NOTIFY_TABLE = "notification_tab";
    private static final String LOCATION_TABLE = "location_tab";

    private static final String ID = "id";
    private static final String  USER_NAME = "user";
    private static final String  PHONE_NUMBER = "mobile";
    private static final String  NOTIFY_TITLE = "notify_title";
    private static final String  NOTIFY_DESC = "notify_desc";

    private static final String LOCATION = "place_name";
    private static final String ADDRESS = "place_address";
    private static final String  LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";


    public RokoDbAdapter(Context context) {
        homeDbHelper = new HomeDbHelper(context);
    }


    public long insertNewHomeData(List<ContactList> favItems) {
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
//        db.execSQL("delete from "+ CONTACT_NEW);
        ContentValues contentValues = new ContentValues();
        long value = (long) -1;
        for (ContactList catModel: favItems) {
            contentValues.put(USER_NAME, catModel.getContactName());
            contentValues.put(PHONE_NUMBER, catModel.getContactNumber());

            String uId = catModel.getContactNumber();
            String [] column = {PHONE_NUMBER};
            Cursor cursor = db.query(CONTACT_NEW, column, PHONE_NUMBER+" = '"+uId+"'", null, null, null, null);

            if(cursor.getCount() <= 0){
                cursor.close();
//                return false;
                value = db.insert(CONTACT_NEW, null, contentValues);
            }else {
                cursor.close();
                value = (db.update(CONTACT_NEW, contentValues, PHONE_NUMBER+" = '"+catModel.getContactNumber()+"'", null));
            }
        }
        db.close();
        return value;
    }

    public long insertNewNotification(List<NotifyModel> favItems) {
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
//        db.execSQL("delete from "+ CONTACT_NEW);
        ContentValues contentValues = new ContentValues();
        long value = (long) -1;
        for (NotifyModel catModel: favItems) {
            contentValues.put(NOTIFY_TITLE, catModel.getNotifyTitle());
            contentValues.put(NOTIFY_DESC, catModel.getNotifyDesc());

            String notifyDesc = catModel.getNotifyDesc();
            String [] column = {NOTIFY_DESC};
            Cursor cursor = db.query(NOTIFY_TABLE, column, NOTIFY_DESC+" = '"+notifyDesc+"'", null, null, null, null);

            if(cursor.getCount() <= 0){
                cursor.close();
//                return false;
                value = db.insert(NOTIFY_TABLE, null, contentValues);
            }else {
                cursor.close();
                value = (db.update(NOTIFY_TABLE, contentValues, NOTIFY_DESC+" = '"+catModel.getNotifyDesc()+"'", null));
            }
        }
        db.close();
        return value;
    }

    public boolean deleteContact(String contactNumber) {
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
         boolean data = db.delete(CONTACT_NEW, PHONE_NUMBER +" = '"+contactNumber+"'", null) > 0;
         db.close();
         return data;
    }

    public boolean deleteNotification(int uId) {
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
         boolean data = db.delete(NOTIFY_TABLE, ID +" = '"+uId+"'", null) > 0;
         db.close();
         return data;
    }

    public void deleteAllNotification() {
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
        db.execSQL("delete from "+ NOTIFY_TABLE);
        db.close();
    }

    public void readLocalNewHomeDataOne(ContactAdapter adapter, List<ContactList> favItems, LinearLayout layoutAdd){
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
        String[] column = {ID, USER_NAME, PHONE_NUMBER};
        Cursor cursor = db.query(CONTACT_NEW, column, null, null, null, null, null);
        try {
            while (cursor.moveToNext()){

                int index2 = cursor.getColumnIndex(USER_NAME);
                int index3 = cursor.getColumnIndex(PHONE_NUMBER);


                String userName = cursor.getString(index2);
                String phoneNumber = cursor.getString(index3);


                favItems.add(new ContactList(userName, phoneNumber));
            }
            cursor.close();
//            adapter.notifyDataSetChanged();
        }finally {
            if (favItems.size()>=5){
                layoutAdd.setVisibility(View.GONE);
            }else
                layoutAdd.setVisibility(View.VISIBLE);
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }
    }

    public void readLocalNotifications(List<NotifyModel> favItems/*, LinearLayout layoutAdd*/){
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
        String[] column = {ID, NOTIFY_TITLE, NOTIFY_DESC};
        Cursor cursor = db.query(NOTIFY_TABLE, column, null, null, null, null, null);
        try {
            while (cursor.moveToNext()){
                int index1 = cursor.getColumnIndex(ID);
                int index2 = cursor.getColumnIndex(NOTIFY_TITLE);
                int index3 = cursor.getColumnIndex(NOTIFY_DESC);


                int id = cursor.getInt(index1);
                String title = cursor.getString(index2);
                String desc = cursor.getString(index3);


                favItems.add(new NotifyModel(id, title, desc, ""));
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        /*finally {
            if (favItems.size()>=5){
                layoutAdd.setVisibility(View.GONE);
            }else
                layoutAdd.setVisibility(View.VISIBLE);
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }*/
    }


    public long insertSearchHistory(SearchHistoryModel catModel) {
        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
//        db.execSQL("delete from "+ CONTACT_NEW);
        ContentValues contentValues = new ContentValues();
        long value = (long) -1;
//        for (SearchHistoryModel catModel: searchHistoryItems) {
            contentValues.put(LOCATION, catModel.getPlaceName());
            contentValues.put(ADDRESS, catModel.getFormattedAddress());
            contentValues.put(LATITUDE, catModel.getLatitude());
            contentValues.put(LONGITUDE, catModel.getLongitude());

            double lat = catModel.getLatitude();
            double lng = catModel.getLongitude();
            String [] column = {LATITUDE, LONGITUDE};
            Cursor cursor = db.query(LOCATION_TABLE, column, LATITUDE + "="+lat + " AND " + LONGITUDE + "="+lng,
                    null, null, null, null, null);

            if(cursor.getCount() <= 0){
                cursor.close();
                value = db.insert(LOCATION_TABLE, null, contentValues);
            }else {
                cursor.close();
                value = (db.update(LOCATION_TABLE, contentValues, LATITUDE+" = "+catModel.getLatitude()+" AND "+LONGITUDE+"="+catModel.getLongitude(), null));
            }
//        }
        db.close();
        return value;
    }

    public ArrayList<SearchHistoryModel> readSearchHistory() {
        ArrayList<SearchHistoryModel> searchModels = new ArrayList<>();
        ArrayList<SearchHistoryModel> mySearchHistory = new ArrayList<>();
        String countQuery = "SELECT  * FROM " + LOCATION_TABLE;
        SQLiteDatabase db1 = homeDbHelper.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(countQuery, null);
        int count = cursor1.getCount();
        cursor1.close();

            String selectQr = " Select * From "+LOCATION_TABLE+" order by "+ID+" desc limit 4";
            Cursor cursor2 = db1.rawQuery(selectQr, null);
            try {
                while (cursor2.moveToNext()) {
                    int index1 = cursor2.getColumnIndex(ID);
                    int index2 = cursor2.getColumnIndex(LOCATION);
                    int index3 = cursor2.getColumnIndex(ADDRESS);
                    int index4 = cursor2.getColumnIndex(LATITUDE);
                    int index5 = cursor2.getColumnIndex(LONGITUDE);

                    int id = cursor2.getInt(index1);
                    String location = cursor2.getString(index2);
                    String address = cursor2.getString(index3);
                    double latitude = cursor2.getDouble(index4);
                    double longitude = cursor2.getDouble(index5);

                    searchModels.add(new SearchHistoryModel(id, location, address, latitude, longitude));
                }
                cursor2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        /*if (count>4){
            String[] arr = new String[4];
            for (int i = 0; i<searchModels.size()-1;i++){
                arr[i] = (searchModels.get(i).getPlaceName());
                *//*if (i != searchModels.size()-1){
                   }else
                      arr[i] = (searchModels.get(i).getId());*//*
            }
//            db1.delete(LOCATION_TABLE, LOCATION +" NOT IN (?, ?, ?, ?)", arr );
        }*/

        SQLiteDatabase db = homeDbHelper.getWritableDatabase();
        String[] column = {ID, LOCATION, ADDRESS, LATITUDE, LONGITUDE};
        Cursor cursor = db.query(LOCATION_TABLE, column, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                int index1 = cursor.getColumnIndex(ID);
                int index2 = cursor.getColumnIndex(LOCATION);
                int index3 = cursor.getColumnIndex(ADDRESS);
                int index4 = cursor.getColumnIndex(LATITUDE);
                int index5 = cursor.getColumnIndex(LONGITUDE);

                int id = cursor.getInt(index1);
                String location = cursor.getString(index2);
                String address = cursor.getString(index3);
                double latitude = cursor.getDouble(index4);
                double longitude = cursor.getDouble(index5);

                mySearchHistory.add(new SearchHistoryModel(id, location, address, latitude, longitude));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "mySearchHistory: ============= "+mySearchHistory);
        for (SearchHistoryModel search : mySearchHistory){

            // TODO: 15-May-19 remove data query is not working
//            db.delete(LOCATION_TABLE, ID+"="+search.getId(), null);
            Log.d(TAG, "mySearchHistory: =================="+search.getPlaceName());
        }
        Log.d(TAG, "searchModels: ============= "+searchModels);
        for (SearchHistoryModel search : searchModels){

            // TODO: 15-May-19 remove data query is not working
//            db.delete(LOCATION_TABLE, ID+"="+search.getId(), null);
            Log.d(TAG, "searchModels: =================="+search.getPlaceName());
        }

        List<SearchHistoryModel> myModel;
//        myModel.removeAll(searchModels);
        mySearchHistory.removeAll(searchModels);

        mySearchHistory.addAll(searchModels);

//            Collections.sort( mySearchHistory);

        myModel = new ArrayList(mySearchHistory);

        for (SearchHistoryModel search : myModel){

            // TODO: 15-May-19 remove data query is not working
//            db.delete(LOCATION_TABLE, ID+"="+search.getId(), null);
            Log.d(TAG, "my model: =================="+search.getPlaceName());
        }
        db1.close();
        db.close();
        return searchModels;
    }




    class HomeDbHelper extends SQLiteOpenHelper {
        Context context;

        private static final String CREATE_NEW_HOME_TABLE = "create table "+CONTACT_NEW +"("+ ID+"  INTEGER primary key autoincrement,"+ USER_NAME+" text,"+ PHONE_NUMBER+" text"+");";
        private static final String DROP_NEW_HOME_TABLE = "DROP TABLE IF EXISTS "+CONTACT_NEW;

        private static final String CREATE_NOTIFICATION_TABLE = "create table "+NOTIFY_TABLE +"("+ ID+"  INTEGER primary key autoincrement,"+ NOTIFY_TITLE+" text,"+ NOTIFY_DESC+" text"+");";
        private static final String DROP_NOTIFICATION_TABLE = "DROP TABLE IF EXISTS "+NOTIFY_TABLE;

        private static final String CREATE_SEARCH_LOCATION_TABLE = "create table "+LOCATION_TABLE +"("+ ID+"  INTEGER primary key autoincrement,"+ LOCATION+" text,"+ ADDRESS+" text,"+ LATITUDE + " REAL,"+LONGITUDE+" REAL"+");";
        private static final String DROP_SEARCH_LOCATION_TABLE = "DROP TABLE IF EXISTS "+LOCATION_TABLE;



        private HomeDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
//            Message.message(context, "Constructor called.");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.d(TAG, "onCreate: onCreateCalled.");
//                Message.message(context, "onCreate: onCreateCalled.");
                db.execSQL(CREATE_NEW_HOME_TABLE);
                db.execSQL(CREATE_NOTIFICATION_TABLE);
                db.execSQL(CREATE_SEARCH_LOCATION_TABLE);

            } catch (SQLException e) {
//                Message.message(context, "onCreate: error=== " + e);
                Log.d(TAG, "onCreate: error Occurred === " + e);
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.d(TAG, "onUpgrade: " + "onUpgradeCalled.");
//                Message.message(context, "onUpgradeCalled.");

                db.execSQL(DROP_NEW_HOME_TABLE);
                db.execSQL(DROP_NOTIFICATION_TABLE);
                db.execSQL(DROP_SEARCH_LOCATION_TABLE);


                onCreate(db);
            } catch (SQLException e) {
//                Message.message(context,"onUpgrade: error=== " + e);
                Log.d(TAG, "onUpgrade: error Occurred === " + e);
                e.printStackTrace();
            }
        }
    }
}
