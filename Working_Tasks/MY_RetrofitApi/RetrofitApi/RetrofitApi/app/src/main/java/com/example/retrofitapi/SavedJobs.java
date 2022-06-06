package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

public class SavedJobs extends AppCompatActivity {
    RecyclerView RV_Savred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_saved_jobs);

        RV_Savred = findViewById(R.id.RV_Save);

        RV_Savred.setLayoutManager(new LinearLayoutManager(this));
        getTask();
    }
    private void getTask() {
        class GetTasks extends AsyncTask<Void, Void, List<JobSaves>> {

            @Override
            protected List<JobSaves> doInBackground(Void... voids) {
                List<JobSaves> taskList = DBClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDaO()
                        .getSaveAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<JobSaves> tasks) {
                super.onPostExecute(tasks);
                SavedAdapter adapter = new SavedAdapter(SavedJobs.this, tasks);
                RV_Savred.setAdapter(adapter);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
}
