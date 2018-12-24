package com.chenhuiyeh.module_cache_data;

import android.content.Context;

import com.chenhuiyeh.module_cache_data.dao.CoursesDao;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CourseInfo.class}, version = 1)
@TypeConverters(TimeTableTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    // new daos can be added here
    public abstract CoursesDao courseDao();

    private static final String DATABASE_NAME = "timetable_db";

    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration() // for testing
                        .build();
            }
        }

        return sInstance;
    }

    // add migrations here
}