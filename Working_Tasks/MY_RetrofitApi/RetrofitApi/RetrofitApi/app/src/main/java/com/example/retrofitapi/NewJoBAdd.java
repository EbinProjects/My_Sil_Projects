package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kotlin.Unit;

public class NewJoBAdd extends AppCompatActivity {
  private EditText Category_Name,Category_Code,Product_Code,Barcode,Product_name,MRP,Units,Category_id;
    Button SaveData;
    Boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_jo_badd);
        Category_Name=findViewById(R.id.Catagory_names);
        Category_Code=findViewById(R.id.Catagory_Code);
        Product_Code=findViewById(R.id.Product_code);
        Barcode=findViewById(R.id.BarcodeNumbers);
        Product_name=findViewById(R.id.Product_name);
        MRP=findViewById(R.id.MRP);
        Units=findViewById(R.id.Unit);
        Category_id=findViewById(R.id.customerID);



        SaveData=findViewById(R.id.savedata);
        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String CategoryName=Category_Name.getText().toString();
               String CategoryCode=Category_Code.getText().toString();
               String ProductCode=Product_Code.getText().toString();
               String Barcodes=Barcode.getText().toString();
               String ProductName=Product_name.getText().toString();
               String Mrps=MRP.getText().toString();
               String Unit=Units.getText().toString();
               String CategoryId=Category_id.getText().toString();
            savetask(CategoryName,CategoryCode,ProductCode,Barcodes,ProductName,Mrps,Unit,CategoryId);

            }
        });

    }

    private void savetask(String categoryName, String categoryCode, String productCode, String barcodes, String productName, String mrps, String unit, String categoryId) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Ctageory category=new Ctageory();

                category.setCategory_name(categoryName);
                category.setCategory_code(categoryCode);

                product newproduct=new product();
                newproduct.setProduct_code(productCode);
                newproduct.setProduct_name(productName);
                newproduct.setBarcodeNumber(barcodes);
                newproduct.setMRP(mrps);
                newproduct.setUnit(unit);
                newproduct.setCategory_id(categoryId);




                //adding to database
//                productDao taskDao = DBClient.getInstance(getApplicationContext()).getAppDatabase().productDaO();
//
//                check = taskDao.is_exist(productidd);
                if (!(category.getCategory_code().isEmpty()&&category.getCategory_name().isEmpty())) {
                    DBClient.getInstance(getApplicationContext()).getAppDatabase()
                            .productDaO()
                            .insert(newproduct);
                }

                DBClient.getInstance(getApplicationContext()).getAppDatabase()
                        .productDaO()
                        .insert(category);
//                }



                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), NewJob.class));
//                if (check == false) {
//                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(getApplicationContext(), "the name is already exists", Toast.LENGTH_LONG).show();
//                }

            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }
    }


