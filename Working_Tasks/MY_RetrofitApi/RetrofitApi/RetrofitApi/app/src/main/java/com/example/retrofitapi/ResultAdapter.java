package com.example.retrofitapi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    Context context;
    List<ProductAndCatageory> prodectList;
    List<childRules> childlist;


    public ResultAdapter(Context context, List<ProductAndCatageory> prodectList,List<childRules> childlist) {
        this.context = context;
        this.prodectList = prodectList;
        this.childlist = childlist;

    }


    @Override
    public ResultViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_data, parent, false);
        return new ResultViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ResultViewHolder  holder, int position) {
        ProductAndCatageory p = prodectList.get(position);
        Ctageory ctageory=p.getCategoryList();
        product products=p.getProducts();
        holder.CatName.setText(ctageory.getCategory_name());
        holder.CataCode.setText(ctageory.getCategory_code());
       holder.ProductName.setText(products.getProduct_name());
       holder.Mrp.setText(products.getMRP());
       holder.Units.setText(products.getUnit());
//        try {
//            holder.CataID.getBytes(products.getCategory_id());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            holder.barcode.getBytes(products.getBarcodeNumber());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            holder.ProductCode.getBytes(products.getProduct_code());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public int getItemCount() {
        return prodectList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView CatName, CataCode, ProductName, Mrp ;
        EditText  Units,Dates;
        Button GetRules,Submit;
        DatePickerDialog datePickerDialog;
        RecyclerView Re_Child;
        int ProductID;

        public ResultViewHolder(View itemView) {
            super(itemView);
            CatName = itemView.findViewById(R.id.CatagoryName);
            CataCode =itemView. findViewById(R.id.CatagoryCode);
            ProductName = itemView.findViewById(R.id.ProductName);
            Mrp = itemView.findViewById(R.id.Price);
            Units = itemView.findViewById(R.id.Quantity);
            Dates = itemView.findViewById(R.id.Date);
            GetRules=itemView.findViewById(R.id.getRule);
            Submit=itemView.findViewById(R.id.submit);
            Re_Child=itemView.findViewById(R.id.childre);

            Submit.setEnabled(false);

            Dates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // calender class's instance and get current date , month and year from calender
                    Dates.setText("2/3/2022");
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    Dates.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });
            GetRules.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Submit.setEnabled(true);
                    ChildAdapter childAdapter=new ChildAdapter(childlist);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                    Re_Child.setLayoutManager(linearLayoutManager);
                    Re_Child.setAdapter(childAdapter);

                }
            });

Submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ProductAndCatageory p=prodectList.get(getAdapterPosition());
        Ctageory ctageory=p.getCategoryList();
        product products=p.getProducts();

        final String sUnits = Units.getText().toString().trim();
        final String sDates = Dates.getText().toString().trim();
        final String sCatName = CatName.getText().toString().trim();
        final String sCateCode = CataCode.getText().toString().trim();
        final String sProductName = ProductName.getText().toString().trim();
        final String sMrp = Mrp.getText().toString().trim();
        final String sCatID =products.getCategory_id();
        final String sProductCode=products.getProduct_code();
        final String sBarcode =products.getBarcodeNumber();
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.save_dialouge);
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.btn_no_save);
// if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Scanning.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        Button videoButton = (Button) dialog.findViewById(R.id.btn_yes_to_save);
// if decline button is clicked, close the custom dialog
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDialog(sCatID,sCatName,sCateCode,sMrp,sProductName,sDates,sUnits,sProductCode,sBarcode);
                dialog.dismiss();


            }
        });

    }
});

        }

        private void SaveDialog(String cataID, String sCatName, String sCateCode, String sMrp, String sProductName, String sDates, String sUnits, String productCode, String barcode) {

            class SaveJobTask extends AsyncTask<Void, Void, Void> {


                @Override
                protected Void doInBackground(Void... voids) {
                    JobSaves jobSaves=new JobSaves();
                    jobSaves.setCataID(cataID);
                    jobSaves.setCataCode(sCateCode);
                    jobSaves.setCatName(sCatName);
                    jobSaves.setMrp(sMrp);
                    jobSaves.setProductName(sProductName);
                    jobSaves.setDates(sDates);
                    jobSaves.setUnits(sUnits);
                    jobSaves.setProductCode(productCode);
                    jobSaves.setBarcode(barcode);
                    DBClient.getInstance(context).getAppDatabase()
                            .productDaO()
                            .insert(jobSaves);
                    return null;
                }

                @Override
                protected void onPostExecute(Void unused) {
                    super.onPostExecute(unused);
                    Toast.makeText(context, "Job Saved", Toast.LENGTH_SHORT).show();
                    Intent startIntent = new Intent(context, MainActivity.class);
                    context.startActivity(startIntent);
                }
            }

            SaveJobTask ut = new SaveJobTask();
            ut.execute();

        }
    }
        }






