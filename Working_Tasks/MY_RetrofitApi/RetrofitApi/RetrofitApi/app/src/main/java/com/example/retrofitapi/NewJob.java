package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewJob extends AppCompatActivity {
    RecyclerView Datas;
    TextView NewJoBDataBase;
    ArrayList<String> JobNames = new ArrayList<String>(Arrays.asList("Person 1", "Person 2", "Person 3", "Person 4"));
    ArrayList<String> Category = new ArrayList<String>(Arrays.asList("Person 1", "Person 2", "Person 3", "Person 4"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_job);
        Datas=findViewById(R.id.data);
        NewJoBDataBase=findViewById(R.id.newJobDataBase);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        Datas.setLayoutManager(linearLayoutManager);
        Datas.setLayoutManager(new LinearLayoutManager(this));
        getTask();
        NewJoBDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewJoBAdd.class));
            }
        });

    }

    private void getTask() {
        class GetTasks extends AsyncTask<Void, Void, List<Ctageory>> {

            @Override
            protected List<Ctageory> doInBackground(Void... voids) {
                List<Ctageory> taskList = DBClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDaO()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Ctageory> tasks) {
                super.onPostExecute(tasks);
                CustomAdapter adapter = new CustomAdapter(NewJob.this, tasks);
                Datas.setAdapter(adapter);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
}