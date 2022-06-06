package com.example.retrofitapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class product {


    @PrimaryKey(autoGenerate = true)
    private int product_Id;

    public int getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(int product_Id) {
        this.product_Id = product_Id;
    }

    @ColumnInfo
    @SerializedName("product_code")
    private String product_code;

    @ColumnInfo
    @SerializedName("product_name")
    private String product_name;

    @ColumnInfo
    @SerializedName("MRP")
    private String MRP;

    @ColumnInfo
    @SerializedName("BarcodeNumber")
    private String BarcodeNumber;

    @ColumnInfo
    @SerializedName("Unit")
    private String Unit;

    @ColumnInfo
    @SerializedName("category_id")
    private String category_id;



    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getBarcodeNumber() {
        return BarcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        BarcodeNumber = barcodeNumber;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
