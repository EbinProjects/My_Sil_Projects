package com.example.retrofitapi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    Context context;
    List<Ctageory> newJobList;


    public CustomAdapter(Context context, List<Ctageory> newJobList) {
        this.context = context;
        this.newJobList = newJobList;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Ctageory p = newJobList.get(position);
        holder.textViewTask.setText(p.getCategory_code());
        holder.textViewCatagory.setText(p.getCategory_name());
//            holder.textViewTask.setText(JobNames.get(position));
//            holder.textViewCatagory.setText(Category.get(position));
//        holder.address.setText(t.getAddress());
//        holder.place.setText(t.getPlace());
//        holder.email.setText(t.getEmail());

    }

    @Override
    public int getItemCount() {
        return newJobList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTask, textViewCatagory;

        public CustomViewHolder(View itemView) {
            super(itemView);


            textViewTask = itemView.findViewById(R.id.jOBName);
            textViewCatagory = itemView.findViewById(R.id.product);
//            address = itemView.findViewById(R.id.address);
//            place = itemView.findViewById(R.id.place);
//            email = itemView.findViewById(R.id.email);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
//            String SectionCode=JobNames.get(getAdapterPosition());
//            String Section=Category.get(getAdapterPosition());
            Ctageory ctageory = newJobList.get(getAdapterPosition());
            String Section = ctageory.getCategory_code();
            String SectionCode = ctageory.getCategory_name();
            int id=ctageory.getCategory_id();

            popup(Section,SectionCode,id);





        }


    }

    private void popup(String section, String sectionCode,int ids) {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.new_job_dialouge);
        TextView dataName = dialog.findViewById(R.id.SectionCode);
        TextView dataCategoryName = dialog.findViewById(R.id.Section);
        dataName.setText(sectionCode);
        dataCategoryName.setText(section);
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.btn_n);
// if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        Button videoButton = (Button) dialog.findViewById(R.id.btn_y);
// if decline button is clicked, close the custom dialog
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Scanning.class);
                intent.putExtra("ID",ids);
                context.startActivity(intent);

                dialog.dismiss();


            }
        });
    }
}





