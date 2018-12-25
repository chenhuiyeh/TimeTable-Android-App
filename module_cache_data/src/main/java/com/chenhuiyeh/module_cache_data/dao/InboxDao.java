package com.chenhuiyeh.module_cache_data.dao;

import com.chenhuiyeh.module_cache_data.model.InboxItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface InboxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveData(InboxItem ... inboxItems);

    @Delete
    void deleteData(InboxItem ... inboxItems);

    @Update
    void updateData(InboxItem ... inboxItems);

    @Query("SELECT * FROM inbox_table")
    List<InboxItem> loadDataFromDb();

    @Query("SELECT * FROM inbox_table WHERE id =:id")
    InboxItem loadDataByIdFromDb(int id);

    @Query("DELETE FROM inbox_table")
    void deleteAll();

}
