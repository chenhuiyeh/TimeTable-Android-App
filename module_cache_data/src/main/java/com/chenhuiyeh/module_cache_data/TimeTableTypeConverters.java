package com.chenhuiyeh.module_cache_data;


import androidx.room.TypeConverter;

public class TimeTableTypeConverters {

    @TypeConverter
    public String fromArray(String[] strings) {
        String string = "";
        for(String s : strings) string += (s + ",");

        return string;
    }

    @TypeConverter
    public String[] toArray(String concatenatedStrings) {
        String[] myStrings = new String[7];
        int i = 0;
        for(String s : concatenatedStrings.split(",")) {
            myStrings[i] = s;
            i++;
        }

        return myStrings;
    }
}
