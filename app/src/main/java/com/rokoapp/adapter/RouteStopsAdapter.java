
package com.rokoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rokoapp.R;
import com.rokoapp.model.response.MyRouteSubData;

import java.util.List;

public class RouteStopsAdapter extends RecyclerView.Adapter<RouteStopsAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyRouteSubData> dataList;

    public RouteStopsAdapter(Context mContext, List<MyRouteSubData> routeFinderList) {
        this.mContext = mContext;
        this.dataList = routeFinderList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewStopName;
        private ImageView stopType;
        private View viewTop,viewBottom;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewStopName = itemView.findViewById(R.id.textViewStopName);
            viewBottom = itemView.findViewById(R.id.viewBottom);
            viewTop = itemView.findViewById(R.id.viewTop);
            stopType = itemView.findViewById(R.id.stopType);




        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_stops_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MyRouteSubData stopData = dataList.get(position);

        holder.textViewStopName.setText(stopData.getBus_name());
        if(position==0){
            holder.viewTop.setVisibility(View.INVISIBLE);
            holder.viewBottom.setVisibility(View.VISIBLE);
            holder.stopType.setColorFilter(ContextCompat.getColor(mContext,
                    R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

        }
        else if(position ==dataList.size()-1){
            holder.viewTop.setVisibility(View.VISIBLE);
            holder.viewBottom.setVisibility(View.INVISIBLE);
            holder.stopType.setColorFilter(ContextCompat.getColor(mContext,
                    R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else{
            holder.viewTop.setVisibility(View.VISIBLE);
            holder.viewBottom.setVisibility(View.VISIBLE);
            holder.stopType.setColorFilter(ContextCompat.getColor(mContext,
                    R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

        }



    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else return 0;
    }
}
