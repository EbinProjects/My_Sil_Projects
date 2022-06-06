package com.example.ebin;

import android.content.Context;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapter  extends ArrayAdapter<DataModel>  {

private List<DataModel> dataSet;
private List<DataModel> dataSet1;
List<DataModel> temarray;
        Context mContext;
     private  int list_data;
     ListView listViewData;
     View cview;


    public CustomAdapter(List<DataModel> dataModels, Context applicationContext, int list_data,List<DataModel> dataModels1) {
        super(applicationContext, list_data, dataModels);
        this.mContext=applicationContext;
        this.dataSet=dataModels;
        this.dataSet1=dataModels1;
        this.list_data=list_data;
       this.temarray=dataSet;




    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(list_data, parent, false);

            TextView textView=convertView.findViewById(R.id.userName);
            TextView textView1=convertView.findViewById(R.id.dob);
            textView.setText(getItem(position).getName());
            textView1.setText(getItem(position).getDob());
            cview=convertView;


        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().trim();
                if (!charString.isEmpty() ) {
                    List<DataModel> filteredList = new ArrayList<>();
                    for (DataModel row : dataSet1) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;

                    return filterResults;
                }else {
                 FilterResults   filterResults = new FilterResults();
                    filterResults.values = dataSet1;
                    return filterResults;
                }


            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ArrayList<DataModel> filtered = (ArrayList<DataModel>) filterResults.values;
                dataSet.clear();
                dataSet.addAll(filtered);
                notifyDataSetChanged();

            }
        };
    }

}

