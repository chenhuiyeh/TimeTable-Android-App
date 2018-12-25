package com.chenhuiyeh.module_cache_data.viewmodel;

import android.app.Application;

import com.chenhuiyeh.module_cache_data.model.InboxItem;
import com.chenhuiyeh.module_cache_data.repository.InboxRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class InboxItemViewModel extends AndroidViewModel {
    private InboxRepository mInboxRepository;
    private MutableLiveData<List<InboxItem>> inboxItems;

    public InboxItemViewModel(@NonNull Application application) {
        super(application);
        mInboxRepository = InboxRepository.getInstance(application.getApplicationContext());
        inboxItems = mInboxRepository.loadLiveDataFromDb();
    }

    public LiveData<List<InboxItem>> loadLiveDataFromDb() {
        return inboxItems;
    }

    public LiveData<InboxItem> loadLiveDataById(int id) {
        return mInboxRepository.loadLiveDataByIdFromDb(id);
    }

    public List<InboxItem> loadDataFromDb() {
        return mInboxRepository.loadDataFromDb();
    }

    public InboxItem loadDataByIdFromDb(int _id) {
        return mInboxRepository.loadDataByIdFromDb(_id);
    }

    public void saveData(InboxItem ... inboxItems) {
        mInboxRepository.saveData(inboxItems);
    }

    public void updateData(InboxItem ... inboxItems) {
        mInboxRepository.updataData(inboxItems);
    }

    public void deleteData(InboxItem ... inboxItems) {
        mInboxRepository.deleteData(inboxItems);
    }

    public void deleteAll() {
        mInboxRepository.deleteAll();
    }
}
