package com.example.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
//        holder.address.setText(t.getAddress());
//        holder.place.setText(t.getPlace());
//        holder.email.setText(t.getEmail());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView address,place,email, textViewTask, textViewDesc;

        public TasksViewHolder(View itemView) {
            super(itemView);


            textViewTask = itemView.findViewById(R.id.conName);
            textViewDesc = itemView.findViewById(R.id.conNumb);
//            address = itemView.findViewById(R.id.address);
//            place = itemView.findViewById(R.id.place);
//            email = itemView.findViewById(R.id.email);


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx, UpdateTaskActivity.class);
            intent.putExtra("task", task);

            mCtx.startActivity(intent);
        }
    }
}

