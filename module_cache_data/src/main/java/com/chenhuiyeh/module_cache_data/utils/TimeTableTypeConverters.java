package com.chenhuiyeh.module_cache_data.utils;

import android.util.Log;

import androidx.room.TypeConverter;

public class TimeTableTypeConverters {
    private static final String TAG = "TimeTableTypeConverters";

    @TypeConverter
    public String fromArray(String[] strings) {
        String string = "";
        for(int i = 0; i<strings.length; i ++) string += (strings[i] + ",");

        return string;
    }

    @TypeConverter
    public String[] toArray(String concatenatedStrings) {
        String[] myStrings = new String[5];
        int i = 0;
        for(String s : concatenatedStrings.split(",")) {
            myStrings[i] = s;
            i++;
        }

        return myStrings;
    }
    
    @TypeConverter
    public String fromDoubleArray(String[][] strings) {
        String s = "";
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                if(strings[i][j] == null || strings[i][j].isEmpty())
                    s+=" ";
                else
                    s+=strings[i][j];

                if(j!=strings[i].length-1)
                    s+=",";
            }
            if(i!=strings.length-1)
                s+=";";
        }

        return s;
    }
    
    @TypeConverter
    public String[][] toDoubleArray(String strings) {
        String[][]result = new String[9][7];

        int i = 0;
        for(String s : strings.split(";")) {
            int j = 0;
            for(String inner:s.split(",") ) {
                result[i][j] = inner;
                Log.d(TAG, "toDoubleArray: " + i + " " + j);
                j++;
            }
            if(i < 8)
                i++;
            else
                break;
        }
        return result;
    }
}
