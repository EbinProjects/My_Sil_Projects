package com.example.data;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;


    @Database(entities = {Task.class}, version = 6,exportSchema = false)

    public abstract class AppDatabase extends RoomDatabase {

        public abstract TaskDao taskDao();
    }

