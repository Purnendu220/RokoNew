package com.rokoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.activity.MySos;
import com.rokoapp.dbHelper.RokoDbAdapter;
import com.rokoapp.mapUtils.mapFragment.MyRouteSearch;
import com.rokoapp.mapUtils.responseModel.SearchHistoryModel;
import com.rokoapp.model.ContactList;
import com.rokoapp.utils.ParamUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchHistoryModel> locationList;
    /*private RokoDbAdapter dbAdapter;*/
    private MyRouteSearch routeSearch;

    public SearchHistoryAdapter(Context mContext, List<SearchHistoryModel> locationList, /*RokoDbAdapter dbAdapter,*/ MyRouteSearch routeSearch) {
        this.mContext = mContext;
        this.locationList = locationList;
//        this.dbAdapter = dbAdapter;
        this.routeSearch = routeSearch;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView placeTitleTV, placeFormattedAddressTV;
        private LinearLayout layoutSearch;

        MyViewHolder(View itemView) {
            super(itemView);
            placeTitleTV = itemView.findViewById(R.id.placeTitleTV);
            placeFormattedAddressTV = itemView.findViewById(R.id.placeFormattedAddressTV);
            layoutSearch = itemView.findViewById(R.id.layoutSearch);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SearchHistoryModel searchModel = locationList.get(position);

        holder.placeTitleTV.setText(searchModel.getPlaceName());
        holder.placeFormattedAddressTV.setText(searchModel.getFormattedAddress());

        holder.layoutSearch.setOnClickListener(v -> {
            if (routeSearch.getCodeType() != null){
                if (routeSearch.getCodeType().equals("Home")){
                    ParamUtils.homeLatitude = searchModel.getLatitude();
                    ParamUtils.homeLongitude = searchModel.getLongitude();
                    ParamUtils.homeLocation = searchModel.getFormattedAddress();
                    ParamUtils.sourceName = searchModel.getPlaceName()+", "+searchModel.getFormattedAddress();
                    routeSearch.finish();
                }else {
                    ParamUtils.officeLatitude = searchModel.getLatitude();
                    ParamUtils.officeLongitude = searchModel.getLongitude();
                    ParamUtils.officeLocation = searchModel.getFormattedAddress();
                    ParamUtils.destName = searchModel.getPlaceName()+", "+searchModel.getFormattedAddress();
                    routeSearch.finish();                }
//                    ((MyRouteSearch)mContext).finish();                }
            }
        });

        /*holder.deleteButton.setOnClickListener(v -> {
            boolean contact = dbAdapter.deleteContact(passModel.getContactNumber());
            if (contact){
                Toast.makeText(mContext,"Contact removed from list.", Toast.LENGTH_LONG).show();
                mySos.contactLists.remove(position);
                mySos.adapter.notifyDataSetChanged();
                if (mySos.contactLists.size()>=5){
                    mySos.layoutAdd.setVisibility(View.GONE);
                }else
                    mySos.layoutAdd.setVisibility(View.VISIBLE);
            }

        });*/
    }

    @Override
    public int getItemCount() {
        if (locationList != null) {
            return locationList.size();
        } else return 0;
    }
}