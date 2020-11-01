package com.example.repositorytask.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.repositorytask.model.RepositoryData;
import com.example.repositorytask.app.DataRepository;
import com.example.repositorytask.app.MyApplication;

import java.util.List;


public class ItemListViewModel extends AndroidViewModel {

    private LiveData<PagedList<RepositoryData>> mObservableItems;
    private DataRepository mRepository;

    PagedList.Config myPagingConfig = new PagedList.Config.Builder()
            .setPageSize(50)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(true)
            .build();


    public ItemListViewModel(Application application) {
        super(application);
        mRepository= MyApplication.getInstance(application.getApplicationContext()).getRepository();
    }

    public LiveData<PagedList<RepositoryData>> getItems() {

        mObservableItems = mRepository.getAllItems(myPagingConfig);

        return mObservableItems;
    }

    public LiveData<PagedList<RepositoryData>> getSearchItems(String strSearch)
    {
        mObservableItems = mRepository.getSearchItems(myPagingConfig,strSearch);

        return mObservableItems;
    }

    public void insertAll(List<RepositoryData> lsRepodata)
    {
        mRepository.insertAll(lsRepodata);
    }


}
