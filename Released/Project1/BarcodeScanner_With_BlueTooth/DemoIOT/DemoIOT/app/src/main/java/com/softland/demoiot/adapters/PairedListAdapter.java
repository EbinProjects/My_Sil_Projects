package com.softland.demoiot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.softland.demoiot.OnClickListner;
import com.softland.demoiot.R;

import java.util.ArrayList;

public class PairedListAdapter extends RecyclerView.Adapter<PairedListAdapter.ViewHolder> {

    private ArrayList<String> localDataSet;
    private  Context context;
    private OnClickListner onClickListner;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView DeviceName,DeviceID;
        private final CardView  card_layout;

        public ViewHolder(View view) {
            super(view);
            DeviceName = (TextView) view.findViewById(R.id.txt_device_name);
            DeviceID = (TextView) view.findViewById(R.id.txt_device_id);
            card_layout=(CardView)  view.findViewById(R.id.card_layout);
        }

        public TextView getDeviceNameTextView() {
            return DeviceName;
        }

        public TextView getDeviceIDTextView() {
            return DeviceID;
        }

        public CardView getMainCardview() {
            return card_layout;
        }


    }

    public  void  setListner(OnClickListner onClickListner){
        this.onClickListner=onClickListner;
    }
    public  PairedListAdapter(ArrayList<String> dataSet, Context context) {
       this. localDataSet = dataSet;
       this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_paired_device, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getDeviceNameTextView().setText(localDataSet.get(position).split("\n")[0]);
        viewHolder.getDeviceIDTextView().setText(localDataSet.get(position).split("\n")[1]);
        viewHolder.getMainCardview().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListner!=null){
                    onClickListner.onClick(position,localDataSet.get(position).split("\n")[0],localDataSet.get(position).split("\n")[1]);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}