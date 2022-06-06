package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scanning extends AppCompatActivity {
    ImageView Scanner;
   int IDs;
   Boolean check;
    String Barcod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_scanning);
        IDs = (int) getIntent().getSerializableExtra("ID");
        Scanner=findViewById(R.id.Scanner);
        Scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scan();
            }
        });
    }

    private void Scan() {
        IntentIntegrator intentIntegrator=new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scan Codes..");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult!=null){
            if(intentResult.getContents()!=null){
                Barcod=intentResult.getContents().toString();
                checkTAsk(Barcod,IDs);
//                Intent intent=new Intent(Scanning.this,Result.class);
//                intent.putExtra("Datas",Barcod);
//                intent.putExtra("idea",IDs);
//                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "no Data", Toast.LENGTH_SHORT).show();
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkTAsk(String barcod, int iDs) {
        class CTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {


                //adding to database
                productDao taskDao = DBClient.getInstance(getApplicationContext()).getAppDatabase().productDaO();

                check = taskDao.is_exist(barcod,iDs);



                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (check == false) {
                    Toast.makeText(getApplicationContext(), "Invalid QR Code Or Please select Correct Category", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent(Scanning.this,Result.class);
                intent.putExtra("Datas",Barcod);intent.putExtra("idea",IDs);
              startActivity(intent);
                }

            }
        }

        CTask st = new CTask();
        st.execute();
    }
}