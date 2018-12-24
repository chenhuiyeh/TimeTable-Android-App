package com.chenhuiyeh.timetable.activities.main.fragments.model;

public class CourseInfo {
    private String name = null;
    private String courseCode = null;
    private String[] times = null;
    private String professor = null;
    private String description = null;
    private String location = null;

    public CourseInfo(String name, String courseCode, String professor, String description, String location) {
        this.name = name;
        this.courseCode = courseCode;
        this.professor = professor;
        this.description = description;
        this.location = location;
    }

    public CourseInfo() {
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

    public void setCourseTime(String[] courseTimes) {
        this.times = courseTimes;
    }

    public void setCourseTime(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.times = new String[]{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
