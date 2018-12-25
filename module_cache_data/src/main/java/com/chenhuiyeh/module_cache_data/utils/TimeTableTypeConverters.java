package com.chenhuiyeh.module_cache_data.utils;


import androidx.room.TypeConverter;

public class TimeTableTypeConverters {

    @TypeConverter
    public String fromArray(String[] strings) {
        String string = "";
        for(int i = 0; i<strings.length; i ++) string += (strings[i] + " ");

        return string;
    }

    @TypeConverter
    public String[] toArray(String concatenatedStrings) {
        String[] myStrings = new String[7];
        int i = 0;
        for(String s : concatenatedStrings.split(" ")) {
            myStrings[i] = s;
            i++;
        }

        return myStrings;
    }
}
