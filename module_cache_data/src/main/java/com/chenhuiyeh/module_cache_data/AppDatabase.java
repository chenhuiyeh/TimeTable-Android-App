package com.chenhuiyeh.module_cache_data;

import android.content.Context;

import com.chenhuiyeh.module_cache_data.dao.CoursesDao;
import com.chenhuiyeh.module_cache_data.dao.InboxDao;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.module_cache_data.model.InboxItem;
import com.chenhuiyeh.module_cache_data.utils.TimeTableTypeConverters;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CourseInfo.class, InboxItem.class}, version = 2)
@TypeConverters(TimeTableTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    // new daos can be added here
    public abstract CoursesDao courseDao();
    public abstract InboxDao inboxDao();

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
