package com.example.retrofitapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class JobSaves {


                 @PrimaryKey(autoGenerate = true)
                  private int SaveProduct_id;



        @ColumnInfo
        @SerializedName("CataID")
        private String CataID;

        @ColumnInfo
        @SerializedName("CatName")
        private String CatName;

        @ColumnInfo
        @SerializedName("CataCode")
        private String CataCode;

        @ColumnInfo
        @SerializedName("ProductName")
        private String ProductName;

        @ColumnInfo
        @SerializedName("ProductCode")
        private String ProductCode;

        @ColumnInfo
        @SerializedName("Units")
        private String Units;
        @ColumnInfo
        @SerializedName("Dates")
        private String Dates;

        @ColumnInfo
        @SerializedName("Mrp")
        private String Mrp;
    @ColumnInfo
    @SerializedName("barcode")
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getSaveProduct_id() {
        return SaveProduct_id;
    }

    public void setSaveProduct_id(int saveProduct_id) {
        SaveProduct_id = saveProduct_id;
    }

    public String getCataID() {
        return CataID;
    }

    public void setCataID(String cataID) {
        CataID = cataID;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    public String getCataCode() {
        return CataCode;
    }

    public void setCataCode(String cataCode) {
        CataCode = cataCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getUnits() {
        return Units;
    }

    public void setUnits(String units) {
        Units = units;
    }

    public String getDates() {
        return Dates;
    }

    public void setDates(String dates) {
        Dates = dates;
    }

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }
}
