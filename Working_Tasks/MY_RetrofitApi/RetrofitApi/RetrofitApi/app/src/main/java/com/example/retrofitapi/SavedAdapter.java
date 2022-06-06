package com.example.retrofitapi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedAdapter extends   RecyclerView.Adapter<SavedAdapter.CustomSavedViewHolder> {
        Context context;
        List<JobSaves> SavedList;


        public SavedAdapter(Context context, List<JobSaves> SavedList) {
                this.context = context;
                this.SavedList = SavedList;

        }

        @Override
        public CustomSavedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_list, parent, false);
                return new CustomSavedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomSavedViewHolder holder, int position) {
                JobSaves p = SavedList.get(position);
                holder.SectionCode.setText(p.getCataCode());
                holder.CategoryName.setText(p.getCatName());
                holder.ProductId.setText(p.getProductCode());
//            holder.textViewTask.setText(JobNames.get(position));
//            holder.textViewCatagory.setText(Category.get(position));
//        holder.address.setText(t.getAddress());
//        holder.place.setText(t.getPlace());
//        holder.email.setText(t.getEmail());

        }

        @Override
        public int getItemCount() {
                return SavedList.size();
        }

        class CustomSavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
                TextView SectionCode,CategoryName,ProductId;

                public CustomSavedViewHolder(@NonNull View itemView) {
                        super(itemView);
                        SectionCode=itemView.findViewById(R.id.SectionCode);
                        CategoryName=itemView.findViewById(R.id.CataNames);
                        ProductId=itemView.findViewById(R.id.productCodes);
                        itemView.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        JobSaves jobSaves=SavedList.get(getAdapterPosition());
                        String ID=jobSaves.getCataID();
                        final String SectioCodes=jobSaves.getCataCode();
                        final String CategoryNames=jobSaves.getCatName();
                        final String ProductCodes=jobSaves.getProductCode();
                        int Ids=Integer.parseInt(ID);

                        Popup(SectioCodes,CategoryNames,ProductCodes,Ids);


                }

                private void Popup(String sectioCodes, String categoryNames, String productCodes,int Ids) {
                        final Dialog dialog = new Dialog(context);

                        dialog.setContentView(R.layout.saved_jobs_dialouge);
                        TextView SectionName = dialog.findViewById(R.id.sectionNameS);
                        TextView CategoryName = dialog.findViewById(R.id.CategoryNameS);
                        TextView ProductCode = dialog.findViewById(R.id.product_Codes);
                        SectionName.setText(sectioCodes);
                        CategoryName.setText(categoryNames);
                        ProductCode.setText(productCodes);
                        dialog.show();

                        Button declineButton = (Button) dialog.findViewById(R.id.btn_No);
// if decline button is clicked, close the custom dialog
                        declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        // Close dialog
                                        dialog.dismiss();
                                }
                        });

                        Button videoButton = (Button) dialog.findViewById(R.id.btn_ye);
// if decline button is clicked, close the custom dialog
                        videoButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        Intent intent = new Intent(context, Scanning.class);
                                        intent.putExtra("ID",Ids);
                                        context.startActivity(intent);

                                        dialog.dismiss();


                                }
                        });
                }
        }
}