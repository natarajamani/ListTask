package com.example.repositorytask.db.dao;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.repositorytask.model.RepositoryData;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM repository")
    DataSource.Factory<Integer, RepositoryData> loadAllItems();

    @Query("SELECT * FROM repository WHERE name LIKE :strSearch")
    DataSource.Factory<Integer, RepositoryData> loadSearchItems(String strSearch);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RepositoryData> data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(RepositoryData item);

}



