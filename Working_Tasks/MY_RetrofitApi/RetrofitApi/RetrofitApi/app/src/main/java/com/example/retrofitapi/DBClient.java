package com.example.retrofitapi;


import android.content.Context;

import androidx.room.Room;

public class DBClient {
    private final Context context;
    private static DBClient mInstance;

    private final appDataBase appDatabase;

    private DBClient(Context mCtx) {
        this.context = mCtx;

        appDatabase = Room.databaseBuilder(mCtx,appDataBase.class, "Retro.db").build();
    }

    public static synchronized DBClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DBClient(mCtx);
        }
        return mInstance;
    }

    public appDataBase getAppDatabase() {
        return appDatabase;
    }
}
