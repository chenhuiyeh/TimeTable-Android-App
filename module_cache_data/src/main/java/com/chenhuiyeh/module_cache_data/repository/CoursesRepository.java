package com.chenhuiyeh.module_cache_data.repository;

import android.content.Context;
import android.util.Log;

import com.chenhuiyeh.module_cache_data.AppDatabase;
import com.chenhuiyeh.module_cache_data.AppExecutor;
import com.chenhuiyeh.module_cache_data.dao.CoursesDao;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class CoursesRepository {
    private static final String TAG = "CoursesRepository";

    private static final Object LOCK = new Object();
    private static CoursesRepository sInstance;

    private CoursesDao mCoursesDao;
    private AppExecutor executor;
    private Context context;

    private MutableLiveData<ArrayList<CourseInfo>> cachedCourses;

    private CoursesRepository(Context context) {
        this.executor = AppExecutor.getInstance();
        this.context = context;
        this.mCoursesDao = AppDatabase.getInstance(context).courseDao();

        cachedCourses = new MutableLiveData<>();
    }

    public static CoursesRepository getInstance (Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CoursesRepository(context);
                Log.d(TAG, "getInstance: initialized");
            }
        }
        return sInstance;
    }

    public List<CourseInfo> loadDataFromDb() {
        return mCoursesDao.loadDataFromDB();
    }

    public CourseInfo loadDataByIdFromDb(String _id) {
        return mCoursesDao.loadDataByIdFromDb(_id);
    }

    public void saveData(CourseInfo ... courses) {
        executor.diskIO().execute(()->{
            if (courses == null) return;
            Log.d(TAG, "saveData: non null");
            mCoursesDao.saveData(courses);
        });
    }

    public void updataData(CourseInfo ... courses) {
        executor.diskIO().execute(()->{
            mCoursesDao.updateData(courses);
        });
    }

    public void deleteData(CourseInfo ... courses) {
        executor.diskIO().execute(()->{
            mCoursesDao.deleteData(courses);
        });
    }

    public void deleteAll() {
        cachedCourses.setValue(null);
        executor.diskIO().execute(()->{
            AppDatabase.getInstance(context).clearAllTables();
        });
    }

    public boolean isEmpty() {
        executor.diskIO().execute(()->{
            int num = mCoursesDao.getNumEntries();
            if (num == 0) return;
        });
        return false;
    }


}
