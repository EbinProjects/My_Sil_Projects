package com.example.retrofitapi;



import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ResponseData.class,product.class,Ctageory.class,JobSaves.class},version=17)
public abstract class appDataBase extends RoomDatabase {
    public abstract NoteDao noteDao();

    public  abstract productDao productDaO();

}
