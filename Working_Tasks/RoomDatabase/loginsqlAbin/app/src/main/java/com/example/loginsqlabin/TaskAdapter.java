package com.example.loginsqlabin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TasksViewHolder> {
    private Context mCtx;
    private List<Task> taskList;

    public TaskAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.custom_list, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.textViewTask.setText(t.getPersonName());
        holder.textViewDesc.setText(t.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTask, textViewDesc, textViewFinishBy;

        public TasksViewHolder(View itemView) {
            super(itemView);


            textViewTask = itemView.findViewById(R.id.conName);
            textViewDesc = itemView.findViewById(R.id.conNumb);



            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());

            // Create an alert builder
            AlertDialog.Builder builder
                    = new AlertDialog.Builder(mCtx);
            builder.setTitle("Name");

            // set the custom layout;
            View view1=LayoutInflater.from(mCtx).inflate(R.layout.add_contact,null);
            builder.setView(view1);
            EditText editText=view1.findViewById(R.id.name);
            EditText editText1=view1.findViewById(R.id.phoneNumber);
            editText.setText(task.getPersonName());
            editText1.setText(task.getPhoneNumber());

            // add a button
            builder
                    .setPositiveButton(
                            "Update",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which)
                                {

                                    // send data from the

                                    sendDialogDataToActivity(
                                            editText.getText().toString(), editText1.getText().toString());
                                    // AlertDialog to the Activity



                                }
                            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteTask(task);


                }
            });

            // create and show
            // the alert dialog
            AlertDialog dialog
                    = builder.create();
            dialog.show();


        }

        private void deleteTask(Task task) {
            class DeleteTask extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    DatabaseClient.getInstance(mCtx.getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .delete(task);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(mCtx.getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                    mCtx.startActivity(new Intent(mCtx.getApplicationContext(), MainActivity.class));
                }
            }

            DeleteTask dt = new DeleteTask();
            dt.execute();


        }

        private void sendDialogDataToActivity(String data, String data1) {


                if (data.isEmpty() && data1.isEmpty() ) {


                    // after adding the data we are displaying a toast message.
                    Toast.makeText(mCtx, "Please Add Details.", Toast.LENGTH_SHORT).show();

                }
                class UpdateTask extends AsyncTask<Void, Void, Void> {
                    final Task task = taskList.get(getAdapterPosition());

                    @Override
                    protected Void doInBackground(Void... voids) {
                        task.setPersonName(data);
                        task.setPhoneNumber(data1);

                        DatabaseClient.getInstance(mCtx).getAppDatabase()
                                .taskDao()
                                .update(task);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(mCtx, "Updated", Toast.LENGTH_LONG).show();
                        mCtx.startActivity(new Intent(mCtx,MainActivity.class));

                    }
                }

                UpdateTask ut = new UpdateTask();
                ut.execute();
            }

        }
        }














