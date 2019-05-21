package com.rokoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.model.response.TripHistorySubModel;
import com.rokoapp.utils.ParamUtils;

import java.util.List;

import static android.content.ContentValues.TAG;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<TripHistorySubModel> tripList;

    public TripHistoryAdapter(Context mContext, List<TripHistorySubModel> tripList) {
        this.mContext = mContext;
        this.tripList = tripList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView routeName, sourceLocation, destLocation, dateRide, timeRide;
        private LinearLayout rideHistoryL, boardLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            sourceLocation = itemView.findViewById(R.id.sourceLocation);
            destLocation = itemView.findViewById(R.id.destLocation);
            dateRide = itemView.findViewById(R.id.dateRide);
            timeRide = itemView.findViewById(R.id.timeRide);
            rideHistoryL = itemView.findViewById(R.id.rideHistoryL);
            boardLayout = itemView.findViewById(R.id.boardLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_history_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TripHistorySubModel passModel = tripList.get(position);

        holder.routeName.setText(passModel.getRouteName());
        holder.sourceLocation.setText(passModel.getOriginStationName());
        holder.destLocation.setText(passModel.getDestinationStationName());
        String bookDate = ParamUtils.changeBookingDateFormat(passModel.getBookingDateTime());
        holder.dateRide.setText(bookDate);
        if (!passModel.getAttended().equals("false")) {
            holder.boardLayout.setVisibility(View.VISIBLE);
            String attendedDate = ParamUtils.changeAttendDateFormat(passModel.getAttendedDateTime());
            holder.timeRide.setText(attendedDate);
        }else {
            holder.boardLayout.setVisibility(View.GONE);
        }
        holder.rideHistoryL.setOnClickListener( v ->{
            Log.d(TAG, "onBindViewHolder: ");
        });

    }

    @Override
    public int getItemCount() {
        if (tripList != null) {
            return tripList.size();
        } else return 0;
    }
}
