package com.chenhuiyeh.module_cache_data.viewmodel;

import android.app.Application;

import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.module_cache_data.repository.CoursesRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CoursesViewModel extends AndroidViewModel {
    private CoursesRepository mCoursesRepository;
    private MutableLiveData<List<CourseInfo>> courses;

    public CoursesViewModel(@NonNull Application application) {
        super(application);
        mCoursesRepository = CoursesRepository.getInstance(application.getApplicationContext());
        courses = mCoursesRepository.loadLiveDataFromDb();
    }

    public LiveData<List<CourseInfo>> loadLiveDataFromDb() {
        return courses;
    }

    public LiveData<CourseInfo> loadLiveDataById(String code) {
        return mCoursesRepository.loadLiveDataByIdFromDb(code);
    }

    public List<CourseInfo> loadDataFromDb() {
        return mCoursesRepository.loadDataFromDb();
    }

    public CourseInfo loadDataByIdFromDb(String _id) {
        return mCoursesRepository.loadDataByIdFromDb(_id);
    }

    public void updateCourseTime(String [] times, String code) {
        mCoursesRepository.updateTime(times, code);
    }

    public void saveData(CourseInfo ... courseInfo) {
        mCoursesRepository.saveData(courseInfo);
    }

    public void updateData(CourseInfo ... courseInfos) {
        mCoursesRepository.updataData(courseInfos);
    }

    public void deleteData(CourseInfo ... courseInfos) {
        mCoursesRepository.deleteData(courseInfos);
    }

    public void deleteAll() {
        mCoursesRepository.deleteAll();
    }

    public boolean isEmpty() {
        return mCoursesRepository.isEmpty();
    }
}
