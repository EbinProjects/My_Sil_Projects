package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Picking extends AppCompatActivity {
    CardView NewJob;
    Button NextBtn;
    ImageView Back;
    TextView Pname;
    String value="";
    RadioButton radioButton_new,radioButton_save, radioButton;
            RadioGroup radioGroup;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    Boolean check;
    Boolean check2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_picking);
        radioGroup=findViewById(R.id.radioGroup);
        NewJob = findViewById(R.id.newJob);
        Pname = findViewById(R.id.Pname);
        Back = findViewById(R.id.back);
        NextBtn = findViewById(R.id.NextBtn);
        value = getIntent().getStringExtra("data");
        Pname.setText(value);
        radioButton_new=findViewById(R.id.radio_button_1);
        radioButton_save=findViewById(R.id.radio_button_2);
         check=radioButton_new.isChecked();
         check2=radioButton_save.isChecked();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        String data=radioButton.getText().toString();
        Log.d("TAG", "ninan: "+data);



        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int CheckId=radioGroup.getCheckedRadioButtonId();
                if (CheckId==1)
                {


                }
                else {

                    Assigning(CheckId);
                }
            }
        });
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean Check = prefs.getBoolean("name",false);//"No name defined" is the default value.
        if(!Check){
            NextBtn.setEnabled(false);
            Task();
        }



    }

    private void Assigning(int checkId) {
        switch (checkId){
            case R.id.radio_button_1:
                startActivity(new Intent(getApplicationContext(), com.example.retrofitapi.NewJob.class));
                break;
            case R.id.radio_button_2:
                startActivity(new Intent(getApplicationContext(),SavedJobs.class));
                break;

        }
    }


    private void Task() {
        final Dialog dialog = new Dialog(Picking.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.btn_no);
// if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        Button videoButton = (Button) dialog.findViewById(R.id.btn_yes);
// if decline button is clicked, close the custom dialog
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean("name",true);
                editor.apply();
                NextBtn.setEnabled(true);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Now the Below Btn Is enabled", Toast.LENGTH_SHORT).show();

            }
        });

    }
}