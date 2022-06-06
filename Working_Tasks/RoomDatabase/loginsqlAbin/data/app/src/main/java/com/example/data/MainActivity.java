package com.example.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText, editText1;
Boolean check;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getTasks();

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();


                // Create an alert builder
                AlertDialog.Builder builder
                        = new AlertDialog.Builder(this);
                builder.setTitle("Add Contact");

                // set the custom layout
                View customLayout
                        = getLayoutInflater()
                        .inflate(
                                R.layout.add_contact,
                                null);
                builder.setView(customLayout);

                // add a button
                builder
                        .setPositiveButton(
                                "Save",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {

                                        // send data from the
                                        // AlertDialog to the Activity
                                        editText = customLayout.findViewById(R.id.name);
                                        editText1 = customLayout.findViewById(R.id.phoneNumber);
                                        sendDialogDataToActivity(
                                                editText.getText().toString(), editText1.getText().toString());
                                    }
                                });

                // create and show
                // the alert dialog
                AlertDialog dialog
                        = builder.create();
                dialog.show();

                // Do something with the data
                // coming from the AlertDialog

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void sendDialogDataToActivity(String data,String data1)
    {
        if (data.isEmpty() && data1.isEmpty() ) {


            // after adding the data we are displaying a toast message.
            Toast.makeText(MainActivity.this, "Please Add Details.", Toast.LENGTH_SHORT).show();

        }
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.setPersonName(data);
                task.setPhoneNumber(data1);


                //adding to database
                TaskDao taskDao = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao();

                check = taskDao.is_exist(data);
                if (check == false) {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .insert(task);
                }



                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                if (check == false) {
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "the name is already exists", Toast.LENGTH_LONG).show();
                }

            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }
    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                TaskAdapter adapter = new TaskAdapter(MainActivity.this, tasks);
                recyclerView.setAdapter(adapter);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}



