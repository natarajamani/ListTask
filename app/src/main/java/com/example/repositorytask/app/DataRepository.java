package com.example.repositorytask.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.repositorytask.model.RepositoryData;
import com.example.repositorytask.db.AppDataBase;
import com.example.repositorytask.ui.activity.MainActivity;
import com.example.repositorytask.utils.LogUtils;

import java.util.List;

public class DataRepository {

    private static DataRepository sInstance;
    private final AppDataBase mDatabase;

    private DataRepository(final AppDataBase database) {
        mDatabase = database;
    }

    public static DataRepository getInstance(final AppDataBase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<PagedList<RepositoryData>> getAllItems(PagedList.Config config) {
        DataSource.Factory<Integer, RepositoryData> factory = mDatabase.itemDao().loadAllItems();

        return new LivePagedListBuilder<>(factory, config)
                .build();
    }

    public LiveData<PagedList<RepositoryData>> getSearchItems(PagedList.Config config,String strSearch) {
        DataSource.Factory<Integer, RepositoryData> factory = mDatabase.itemDao().loadSearchItems(strSearch);

        return new LivePagedListBuilder<>(factory, config)
                .build();
    }

    public void insertAll(List<RepositoryData> lsRepodata) {

        try {
            mDatabase.itemDao().insertAll(lsRepodata);
        }
        catch (Exception e)
        {
            LogUtils.printException(e);
        }
    }

   

}
