package com.chenhuiyeh.module_cache_data.repository;

import android.content.Context;
import android.util.Log;

import com.chenhuiyeh.module_cache_data.AppDatabase;
import com.chenhuiyeh.module_cache_data.dao.InboxDao;
import com.chenhuiyeh.module_cache_data.model.InboxItem;
import com.chenhuiyeh.module_cache_data.utils.AppExecutor;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class InboxRepository {
    private static final String TAG = "InboxRepository";

    private static final Object LOCK = new Object();
    private static InboxRepository sInstance;

    private InboxDao mInboxDao;
    private AppExecutor executor;
    private Context context;

    private MutableLiveData<List<InboxItem>> cachedItems = new MutableLiveData<>();

    private InboxRepository(Context context) {
        this.executor = AppExecutor.getInstance();
        this.context = context;
        this.mInboxDao = AppDatabase.getInstance(context).inboxDao();

        executor.diskIO().execute(()->{
            List<InboxItem> inboxItems= mInboxDao.loadDataFromDb();
            executor.mainThread().execute(()->{
                cachedItems.setValue(inboxItems);
            });
        });

    }

    public static InboxRepository getInstance (Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new InboxRepository(context);
                Log.d(TAG, "getInstance: initialized");
            }
        }
        return sInstance;
    }

    public MutableLiveData<List<InboxItem>> loadLiveDataFromDb() {
        final MutableLiveData<List<InboxItem>> items = new MutableLiveData<>();
        executor.diskIO().execute(()->{
            List<InboxItem> inboxItems = mInboxDao.loadDataFromDb();
            executor.mainThread().execute(()->{
                items.postValue(inboxItems);
            });

        });

        return items;
    }

    public MutableLiveData<InboxItem> loadLiveDataByIdFromDb(int _id) {
        final MutableLiveData<InboxItem> item = new MutableLiveData<>();
        executor.diskIO().execute(()->{
            InboxItem itemData = mInboxDao.loadDataByIdFromDb(_id);
            executor.mainThread().execute(()->{
                item.postValue(itemData);
            });

        });
        return item;
    }

    public List<InboxItem> loadDataFromDb() {
        return mInboxDao.loadDataFromDb();
    }

    public InboxItem loadDataByIdFromDb(int _id) {
        return mInboxDao.loadDataByIdFromDb(_id);
    }

    public void saveData(InboxItem ... items) {
        executor.diskIO().execute(()->{
            if (items == null) return;
            Log.d(TAG, "saveData: non null");
            mInboxDao.saveData(items);
        });
    }

    public void updataData(InboxItem ... items) {
        executor.diskIO().execute(()->{
            mInboxDao.updateData(items);
        });
    }


    public void deleteData(InboxItem... items) {
        executor.diskIO().execute(()->{
            mInboxDao.deleteData(items);
        });
    }

    public void deleteAll() {
        executor.diskIO().execute(()->{
            AppDatabase.getInstance(context).clearAllTables();
        });
    }

}
