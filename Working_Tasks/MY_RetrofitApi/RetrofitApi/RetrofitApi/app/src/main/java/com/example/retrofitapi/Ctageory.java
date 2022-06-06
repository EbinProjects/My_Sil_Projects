package com.example.retrofitapi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Ctageory {

    @PrimaryKey(autoGenerate = true)
    private int category_id;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    @ColumnInfo
    @NonNull
    @SerializedName("category_name")
    private String category_name;

    @ColumnInfo
    @NonNull
    @SerializedName("category_code")
    private String category_code;



    @NonNull
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(@NonNull String category_name) {
        this.category_name = category_name;
    }

    @NonNull
    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(@NonNull String category_code) {
        this.category_code = category_code;
    }
}
