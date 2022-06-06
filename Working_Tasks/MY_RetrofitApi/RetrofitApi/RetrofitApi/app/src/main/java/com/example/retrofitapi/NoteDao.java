package com.example.retrofitapi;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM responsedata")
    List<ResponseData> getAllNotes();

    @Query("DELETE FROM ResponseData")
     void delete();

    @Insert
    void insert(ResponseData note);

    @Delete
    void delete(ResponseData note);

    @Update
    void update(ResponseData note);
}
