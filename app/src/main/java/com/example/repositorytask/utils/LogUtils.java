package com.example.repositorytask.utils;

import android.util.Log;

/**
 * This class is used to hanlde log events and exception log printing
 */
public class LogUtils {

    public static void  e(String tag, String message){
        Log.e(tag,message);
    }

    public static void  i(String tag, String message){
        Log.i(tag,message);
    }

    public static void printException(Exception e){
        e.printStackTrace();
    }

}
