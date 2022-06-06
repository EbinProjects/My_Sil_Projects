package com.example.retrofitapi;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProductAndCatageory {
    @Embedded
    public Ctageory categoryList;


    @Relation(parentColumn = "category_id",
            entityColumn = "category_id")    public product products;

    public Ctageory getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Ctageory categoryList) {
        this.categoryList = categoryList;
    }

    public product getProducts() {
        return products;
    }

    public void setProducts(product products) {
        this.products = products;
    }
}
