package com.example.repositorytask.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.example.repositorytask.db.AppDataBase;


public class MyApplication extends MultiDexApplication {

    public static  MyApplication appInstance;
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance=this;
        mAppExecutors = new AppExecutors();

    }

    public static MyApplication getInstance(Context context) {

        return appInstance;
    }

    public AppDataBase getDatabase() {
        return AppDataBase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }



}
