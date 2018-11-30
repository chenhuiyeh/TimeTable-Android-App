package com.chenhuiyeh.timetable.data;

public class Courses extends Object{
    private int id;
    private String name;
    private String code;
    private String info;

    public Courses(String name, String code, String info) {
        this.name = name;
        this.code = code;
        this.info = info;
    }

    public Courses(int id, String name, String code, String info) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
