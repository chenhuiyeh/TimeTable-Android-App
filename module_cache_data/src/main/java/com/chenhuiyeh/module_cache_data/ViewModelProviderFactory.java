package com.chenhuiyeh.module_cache_data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelProviderFactory<V> implements ViewModelProvider.Factory {

    private Application mContext;

    public ViewModelProviderFactory(Application context) {
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CoursesViewModel.class)) {
            return (T) new CoursesViewModel(mContext);
        }
        throw new IllegalArgumentException("Unknown class");
    }
}
