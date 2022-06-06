package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView UserName;
    CardView Picking,Logout,End;
    String value="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        Picking=findViewById(R.id.picking);
        Logout=findViewById(R.id.logout);
        End=findViewById(R.id.end);
        UserName=findViewById(R.id.name);

        Picking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.retrofitapi.Picking.class).putExtra("data",value));

            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popuplog();

            }
        });
         value = getIntent().getStringExtra("data");
        UserName.setText(value);






    }

    private void popuplog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
//                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Exit")
//set message
                .setMessage("Are you sure to Exit...")
//set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        logOut();
                        finish();
                    }
                })
//set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        finish();
                        startActivity(getIntent());
                    }
                })
                .show();
    }





    private void logOut() {



            class Delete extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {

                    //creating a task




                    //adding to database


                    DBClient.getInstance(getApplicationContext()).getAppDatabase()
                            .noteDao()
                            .delete();



                    return null;
                }


                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    finish();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));



                }
            }
            Delete st = new Delete();
            st.execute();

        }


    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}