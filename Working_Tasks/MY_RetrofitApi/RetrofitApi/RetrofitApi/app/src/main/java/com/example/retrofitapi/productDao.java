package com.example.retrofitapi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface productDao {

    @Query("SELECT * FROM Ctageory")
    List<Ctageory> getAll();

    @Query("SELECT * FROM JobSaves WHERE barcode=:data AND CataID=:Id")
    List<JobSaves> getSavedData(String data,String Id);

    @Query("SELECT EXISTS(SELECT * FROM JobSaves WHERE barcode=:data AND CataID=:Id)")
    Boolean is_existData(String data,String Id);

    @Query("SELECT * FROM jobsaves")
    List<JobSaves> getSaveAll();

    @Query("SELECT p.*, ct.* FROM product p INNER JOIN Ctageory ct ON p.category_id = ct.category_id WHERE BarcodeNumber=:data AND p.category_id=:Id")
            List<ProductAndCatageory> getProductAndCategory(String data,int Id);




    @Query("SELECT EXISTS(SELECT p.*, ct.* FROM product p INNER JOIN Ctageory ct ON p.category_id = ct.category_id WHERE BarcodeNumber=:data AND p.category_id=:Id)")
    Boolean is_exist(String data,int Id);

    @Query("SELECT * FROM product WHERE BarcodeNumber =:dataa")
    List<product> getDatas(String dataa);

    @Insert
    void insert(product products);
    @Insert
    void insert(Ctageory ctageory);

    @Insert
    void insert(JobSaves jobSaves);

    @Delete
    void delete(product products);

    @Update
    void update(product products);

}

