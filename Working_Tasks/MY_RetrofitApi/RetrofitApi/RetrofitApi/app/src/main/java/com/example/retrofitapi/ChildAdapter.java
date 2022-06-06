package com.example.retrofitapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    List<childRules> childlist;

    public ChildAdapter(List<childRules> childlist) {
        this.childlist = childlist;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yourlayouts, parent, false);
        return new ChildViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
//        childRules childRuless=childlist.get(position);
        holder.Rule.setText(childlist.get(position).getRules());
        holder.Days.setText(childlist.get(position).getRemdays());
        holder.Discounts.setText(childlist.get(position).getDiscounds());


    }

    @Override
    public int getItemCount() {
        return this.childlist.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView Rule, Days, Discounts;

        public ChildViewHolder(View itemView) {
            super(itemView);

         Rule = itemView.findViewById(R.id.rule);
            Days =itemView.findViewById(R.id.Day);
           Discounts =itemView.findViewById(R.id.discound);


        }
    }
}
