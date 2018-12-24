package com.chenhuiyeh.module_cache_data.dao;

import com.chenhuiyeh.module_cache_data.TimeTableTypeConverters;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

@Dao
@TypeConverters(TimeTableTypeConverters.class)
public interface CoursesDao {

    @Query("SELECT * FROM course_table")
    List<CourseInfo> loadDataFromDB();

    @Query("SELECT * FROM course_table WHERE courseCode =:id")
    CourseInfo loadDataByIdFromDb(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveData(CourseInfo ... courses);

    @Delete
    void deleteData(CourseInfo ... courses);

    @Update
    void updateData(CourseInfo ... courses);

    @Query("DELETE FROM course_table")
    void deleteTableContents();


}
