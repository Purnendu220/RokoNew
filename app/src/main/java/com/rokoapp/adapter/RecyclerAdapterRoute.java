package com.rokoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.activity.SelectShortestRoute;
import com.rokoapp.model.response.RouteFinderModel;
import com.rokoapp.utils.ParamUtils;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerAdapterRoute extends RecyclerView.Adapter<RecyclerAdapterRoute.MyViewHolder> {

    private static int selectedItem = -1;
    private boolean ctrl = false;
    private Context mContext;
    private List<RouteFinderModel> routeFinderList;
    private  SelectShortestRoute selectShortestRoute;

    public RecyclerAdapterRoute(Context mContext, List<RouteFinderModel> routeFinderList, SelectShortestRoute selectShortestRoute) {
        this.mContext = mContext;
        this.routeFinderList = routeFinderList;
        this.selectShortestRoute = selectShortestRoute;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView totalSeats, availableSeats, pickupLocation, pickupPointDistance, dropLocation,dropPointDistance;
        private LinearLayout cardRoute;
        private ImageView checkedIcon;

        MyViewHolder(View itemView) {
            super(itemView);
            totalSeats = itemView.findViewById(R.id.totalSeats);
            availableSeats = itemView.findViewById(R.id.availableSeats);
            pickupLocation = itemView.findViewById(R.id.pickupLocation);
            pickupPointDistance = itemView.findViewById(R.id.pickupPointDistance);
            dropLocation = itemView.findViewById(R.id.dropLocation);
            dropPointDistance = itemView.findViewById(R.id.dropPointDistance);
            cardRoute = itemView.findViewById(R.id.cardRoute);
            checkedIcon = itemView.findViewById(R.id.checkedIcon);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_to_dest_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RouteFinderModel routeFinder = routeFinderList.get(position);

        holder.totalSeats.setText(routeFinder.getTotalSeats());
        holder.availableSeats.setText(routeFinder.getAvailableSeats());
        holder.pickupLocation.setText(routeFinder.getOriginStation());

        Float number=Float.parseFloat(routeFinder.getDistanceOrigin());
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String sourceDistance = df.format(number);

        holder.pickupPointDistance.setText(sourceDistance+" Kms.");
        holder.dropLocation.setText(routeFinder.getDestinationStation());

        Float numberDest = Float.parseFloat(routeFinder.getDistanceDestination());
        DecimalFormat dfN = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String destDistance = dfN.format(numberDest);
        holder.dropPointDistance.setText(destDistance+" Kms.");
        if (!ctrl) {
            selectedItem = 0;
            holder.checkedIcon.setVisibility(View.VISIBLE);
        }
        if (selectedItem == position) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            /*itemSize = sizeList.get(selectedItem).getIdSize();
            Log.d("u", "onClick:without " + itemSize);*/
        } else
            holder.checkedIcon.setVisibility(View.GONE);

        holder.cardRoute.setOnClickListener(v -> {
            ctrl = true;
            Log.d(TAG, "onBindViewHolder: ================================ "+ParamUtils.getCurrentDate());
            Log.d(TAG, "onBindViewHolder: ================================ virtual route id "+routeFinder.getVirtualRouteId());
            Log.d(TAG, "getTotalSeats: ======================= "+routeFinder.getTotalSeats());
            Log.d(TAG, "getAvailableSeats: ======================= "+routeFinder.getAvailableSeats());
            Log.d(TAG, "getOriginStation: ======================= "+routeFinder.getOriginStation());
            Log.d(TAG, "getDistanceOrigin: ======================= "+routeFinder.getDistanceOrigin());
            Log.d(TAG, "getDestinationStation: ======================= "+routeFinder.getDestinationStation());
            Log.d(TAG, "getDistanceDestination: ======================= "+routeFinder.getDistanceDestination());
            Log.d(TAG, "getOriginTime: ======================= "+routeFinder.getOriginTime());
            Log.d(TAG, "getDestinationTime: ======================= "+routeFinder.getDestinationTime());
            Log.d(TAG, "getRouteDateAndTime: ======================= "+routeFinder.getRouteDateAndTime());

            selectShortestRoute.selectMyRoute(routeFinder.getTotalSeats(), routeFinder.getAvailableSeats(), routeFinder.getOriginStation(), routeFinder.getDistanceOrigin(), routeFinder.getDestinationStation(), routeFinder.getDistanceDestination(), routeFinder.getOriginTime(), routeFinder.getDestinationTime(), routeFinder.getRouteDateAndTime(), routeFinder.getVirtualRouteId(), routeFinder.getOrigin_station_id(), routeFinder.getDestinationStationId());
            selectShortestRoute.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            notifyItemChanged(selectedItem);
            selectedItem = selectShortestRoute.locationRecycler.getChildPosition(v);
//                itemSize = sizeList.get(selectedItem).getSize();
//                Log.d("u", "onClick: " + itemSize);

            notifyItemChanged(selectedItem);
        });
    }

    @Override
    public int getItemCount() {
        if (routeFinderList != null) {
            return routeFinderList.size();
        }
        else return 0;
    }
}
