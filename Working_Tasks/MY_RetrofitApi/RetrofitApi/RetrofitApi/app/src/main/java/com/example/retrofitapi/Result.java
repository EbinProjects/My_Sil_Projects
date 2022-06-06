package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity {

    Boolean check;
    private RecyclerView recyclerView1;
    //    ArrayList<childRules> childList;
    List<childRules> childlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_result);
//        String[] rule={"Rule1","Rule2","Rule3"};
//        String[] remDAy={"5","10","15"};
//        String[] dis={"30.0%","35.0%","40.%"};
//        childList=new ArrayList<childRules>();
        childlist = new ArrayList<childRules>();
        childlist.add(new childRules("ruule1", "3", "44"));
        childlist.add(new childRules("ruule1", "3", "44"));
        childlist.add(new childRules("ruule1", "3", "44"));

        recyclerView1 = findViewById(R.id.recy);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        final int IDEA = (int) getIntent().getSerializableExtra("idea");
        final String Data = (String) getIntent().getSerializableExtra("Datas");
        getTask(Data, IDEA);
//        for(int i=0;i<rule.length;i++){
//
//            childRules childRuless=new childRules(rule[i],remDAy[i],dis[i]);
//            childList.add(childRuless);
//        }

    }

    private void getTask(String data, int idea) {
        class GetTasks extends AsyncTask<Void, Void, List<ProductAndCatageory>> {

            @Override
            protected List<ProductAndCatageory> doInBackground(Void... voids) {

                List<ProductAndCatageory> taskList = DBClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDaO()
                        .getProductAndCategory(data, idea);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<ProductAndCatageory> tasks) {
                super.onPostExecute(tasks);
                if (tasks.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid or Please Select Correct Category", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Result.this, Scanning.class));
                } else {
                    ResultAdapter adapter = new ResultAdapter(Result.this, tasks, childlist);
                    recyclerView1.setAdapter(adapter);
                }

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
}

