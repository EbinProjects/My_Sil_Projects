package com.example.loginsqlabin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText editTextName, editTextPh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);


        editTextName = findViewById(R.id.editTextName);
        editTextPh = findViewById(R.id.phoneNumber);


        final Task task = (Task) getIntent().getSerializableExtra("task");

        loadTask(task);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(task);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(task);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }

    private void loadTask(Task task) {
        editTextName.setText(task.getPersonName());
        editTextPh.setText(task.getPhoneNumber());

    }

    private void updateTask(final Task task) {
        final String sTask = editTextName.getText().toString().trim();
        final String sDesc = editTextPh.getText().toString().trim();


        if (sTask.isEmpty() && sDesc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insert Proper details", Toast.LENGTH_SHORT).show();

            return;
        }


        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setPersonName(sTask);
                task.setPhoneNumber(sDesc);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

