package com.example.ebin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter  extends BaseAdapter {
    private ArrayList<DataModel> dataSet2;
    //    List<DataModel> temarray;
    Context mContext;
    private  int list_data;
    ListView listView1;
    LayoutInflater inflter;


    public SearchAdapter(ArrayList<DataModel> dataModels2, Context applicationContext, int list_data) {
//        super(dataModels,applicationContext,list_data);
        this.mContext=applicationContext;
        this.dataSet2=dataModels2;
        this.list_data=list_data;
        inflter = (LayoutInflater.from(applicationContext));



    }


    @Override
    public int getCount() {
        return dataSet2.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.list_data, null);
        TextView textView=view.findViewById(R.id.userName);
        TextView textView1=view.findViewById(R.id.dob);
        textView.setText((CharSequence) dataSet2.get(i).getName());
        textView1.setText((CharSequence) dataSet2.get(i).getDob());
        return view;

    }
}