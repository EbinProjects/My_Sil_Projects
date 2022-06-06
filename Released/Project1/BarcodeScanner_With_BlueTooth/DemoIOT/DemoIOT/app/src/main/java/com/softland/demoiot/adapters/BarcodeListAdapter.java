package com.softland.demoiot.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.softland.demoiot.OnClickListner;
import com.softland.demoiot.R;

import java.util.ArrayList;

public class BarcodeListAdapter extends RecyclerView.Adapter<BarcodeListAdapter.ViewHolder> {

    private ArrayList<String> localDataSet;
    private  Context context;
    public int count;
    private OnClickListner onClickListner;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView DeviceName;
        private final CardView  card_layout;

        public ViewHolder(View view) {
            super(view);
            DeviceName = (TextView) view.findViewById(R.id.txt_barcode_number);
            card_layout=(CardView)  view.findViewById(R.id.card_layout);
        }

        public TextView getDeviceNameTextView() {
            return DeviceName;
        }


        public CardView getMainCardview() {
            return card_layout;
        }


    }

    public  void  setListner(OnClickListner onClickListner){
        this.onClickListner=onClickListner;
    }
    public BarcodeListAdapter(ArrayList<String> dataSet, Context context) {
       this. localDataSet = dataSet;
       this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_barcode_device, viewGroup, false);

        return new ViewHolder(view);
    }

    public  void setData(ArrayList<String> dataSet){
        Log.e("abcd","    public  void setData(ArrayList<String> dataSet){  ==> "+dataSet.size());
        this. localDataSet = dataSet;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getDeviceNameTextView().setText(localDataSet.get(position));

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}