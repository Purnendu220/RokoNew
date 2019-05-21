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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rokoapp.R;
import com.rokoapp.activity.MySos;
import com.rokoapp.activity.Notifications;
import com.rokoapp.dbHelper.RokoDbAdapter;
import com.rokoapp.model.ContactList;
import com.rokoapp.model.NotifyModel;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    private Context mContext;
    private List<NotifyModel> notificationList;
    private RokoDbAdapter dbAdapter;
    private Notifications notifications;

    public NotifyAdapter(Context mContext, List<NotifyModel> notificationList, RokoDbAdapter dbAdapter, Notifications notifications) {
        this.mContext = mContext;
        this.notificationList = notificationList;
        this.dbAdapter = dbAdapter;
        this.notifications = notifications;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView notificationTitle, notificationDesc;
        private RelativeLayout notifyLayout;
        private ImageView deleteButton, notificationImg;

        MyViewHolder(View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationDesc = itemView.findViewById(R.id.notificationDesc);
            notificationImg = itemView.findViewById(R.id.notificationImg);
            notifyLayout = itemView.findViewById(R.id.notifyLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final NotifyModel passModel = notificationList.get(position);

        holder.notificationTitle.setText(passModel.getNotifyTitle());
        if (passModel.getNotifyDesc() != null && !passModel.getNotifyDesc().isEmpty()) {
            holder.notificationDesc.setVisibility(View.VISIBLE);
            holder.notificationDesc.setText(passModel.getNotifyDesc());
        }else
            holder.notificationDesc.setVisibility(View.GONE);
        if (passModel.getNotifyImg() != null && !passModel.getNotifyImg().isEmpty()){

        }

        holder.notifyLayout.setOnClickListener(v -> {
            /*Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" +passModel.getContactNumber()));
            mContext.startActivity(intent);*/
        });

        holder.deleteButton.setOnClickListener(v -> {
            boolean contact = dbAdapter.deleteNotification(passModel.getId());
            if (contact){
                Toast.makeText(mContext,"Notification removed from list.", Toast.LENGTH_LONG).show();
                notifications.notificationList.remove(position);
                notifications.notifyAdapter.notifyDataSetChanged();
                /*if (mySos.contactLists.size()>=5){
                    mySos.layoutAdd.setVisibility(View.GONE);
                }else
                    mySos.layoutAdd.setVisibility(View.VISIBLE);*/
            }

        });
    }

    @Override
    public int getItemCount() {
        if (notificationList != null) {
            return notificationList.size();
        } else return 0;
    }
}