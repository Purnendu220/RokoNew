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
import com.rokoapp.dbHelper.RokoDbAdapter;
import com.rokoapp.model.ContactList;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private Context mContext;
    private List<ContactList> passModelList;
    private RokoDbAdapter dbAdapter;
    private MySos mySos;

    public ContactAdapter(Context mContext, List<ContactList> passModelList, RokoDbAdapter dbAdapter, MySos mySos) {
        this.mContext = mContext;
        this.passModelList = passModelList;
        this.dbAdapter = dbAdapter;
        this.mySos = mySos;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, mobileNum;
        private RelativeLayout sosLayout;
        private ImageView deleteButton;

        MyViewHolder(View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            userName = itemView.findViewById(R.id.userName);
            mobileNum = itemView.findViewById(R.id.mobileNum);
            sosLayout = itemView.findViewById(R.id.sosLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ContactList passModel = passModelList.get(position);

        holder.userName.setText(passModel.getContactName());
        holder.mobileNum.setText(passModel.getContactNumber());

        holder.sosLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" +passModel.getContactNumber()));
            mContext.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
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

        });
    }

    @Override
    public int getItemCount() {
        if (passModelList != null) {
            return passModelList.size();
        } else return 0;
    }
}