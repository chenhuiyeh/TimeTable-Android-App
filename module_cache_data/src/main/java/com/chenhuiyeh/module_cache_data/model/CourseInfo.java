package com.chenhuiyeh.module_cache_data.model;

import com.chenhuiyeh.module_cache_data.utils.TimeTableTypeConverters;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static com.chenhuiyeh.module_cache_data.model.CourseInfo.TABLE_NAME;


@Entity(tableName = TABLE_NAME)
public class CourseInfo {
    static final String TABLE_NAME = "course_table";

    @ColumnInfo
    private String name;

    @PrimaryKey
    @NonNull
    private String courseCode;

    @TypeConverters(TimeTableTypeConverters.class)
    private String[] times;

    private String professor;

    @TypeConverters(TimeTableTypeConverters.class)
    private String[][] descriptions;

    @TypeConverters(TimeTableTypeConverters.class)
    private String[][] locations;

    @Ignore
    public CourseInfo(String name, String courseCode, String professor, String[][] descriptions, String[][] locations) {
        this.name = name;
        this.courseCode = courseCode;
        this.professor = professor;
        this.descriptions = descriptions;
        this.locations = locations;
    }

    public CourseInfo(String name, String courseCode, String[] times, String professor, String[][] descriptions, String[][] locations) {
        this.name = name;
        this.courseCode = courseCode;
        this.times = times;
        this.professor = professor;
        this.descriptions = descriptions;
        this.locations = locations;
    }


    @Ignore
    public CourseInfo() {
    }

    @Ignore
    public CourseInfo(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] courseTimes) {
        this.times = courseTimes;
    }

    public void setCourseTime(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.times = new String[]{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
    }

    @NonNull
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(@NonNull String courseCode) {
        this.courseCode = courseCode;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String[][] getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String[][] descriptions) {
        this.descriptions = descriptions;
    }

    public String[][] getLocations() {
        return locations;
    }

    public void setLocations(String[][] locations) {
        this.locations = locations;
    }
}
