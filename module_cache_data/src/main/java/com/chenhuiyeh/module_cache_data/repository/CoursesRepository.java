package com.chenhuiyeh.module_cache_data.repository;

import android.content.Context;
import android.util.Log;

import com.chenhuiyeh.module_cache_data.AppDatabase;
import com.chenhuiyeh.module_cache_data.OnSaveDataListener;
import com.chenhuiyeh.module_cache_data.utils.AppExecutor;
import com.chenhuiyeh.module_cache_data.dao.CoursesDao;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class CoursesRepository {
    private static final String TAG = "CoursesRepository";

    private static final Object LOCK = new Object();
    private static CoursesRepository sInstance;

    private CoursesDao mCoursesDao;
    private AppExecutor executor;
    private Context context;
    private OnSaveDataListener mOnSaveDataListener;

    private MutableLiveData<List<CourseInfo>> coursesLiveData;

    private MutableLiveData<List<CourseInfo>> cachedCourses = new MutableLiveData<>();

    private CoursesRepository(Context context) {
        this.executor = AppExecutor.getInstance();
        this.context = context;
        this.mCoursesDao = AppDatabase.getInstance(context).courseDao();
        coursesLiveData = new MutableLiveData<>();
        executor.diskIO().execute(()->{
            List<CourseInfo> courseInfos= mCoursesDao.loadDataFromDB();
            executor.mainThread().execute(()->{
                cachedCourses.setValue(courseInfos);
            });
        });

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

    public MutableLiveData<List<CourseInfo>> loadLiveDataFromDb() {

        executor.diskIO().execute(()->{
            List<CourseInfo> courseInfo = mCoursesDao.loadDataFromDB();
            executor.mainThread().execute(()->{
                coursesLiveData.postValue(courseInfo);
            });

        });

        return coursesLiveData;
    }

    public MutableLiveData<CourseInfo> loadLiveDataByIdFromDb(String _id) {
        final MutableLiveData<CourseInfo> course = new MutableLiveData<>();
        executor.diskIO().execute(()->{
            CourseInfo courseInfo = mCoursesDao.loadDataByIdFromDb(_id);
            executor.mainThread().execute(()->{
                course.postValue(courseInfo);
            });

        });

        return course;
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
            coursesLiveData.postValue(mCoursesDao.loadDataFromDB());
            executor.mainThread().execute(()->{
                if(mOnSaveDataListener!= null)
                    mOnSaveDataListener.onSaveCompleted();
            });
        });
    }

    public void updataData(CourseInfo ... courses) {
        executor.diskIO().execute(()->{
            mCoursesDao.updateData(courses);
            coursesLiveData.postValue(mCoursesDao.loadDataFromDB());
        });
    }

    public void updateTime(String[] times, String code) {
        executor.diskIO().execute(()->{
            mCoursesDao.updateCourseTime(code, times);
        });
    }

    public void deleteData(CourseInfo ... courses) {
        executor.diskIO().execute(()->{
            mCoursesDao.deleteData(courses);
        });
    }

    public void deleteAll() {

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
