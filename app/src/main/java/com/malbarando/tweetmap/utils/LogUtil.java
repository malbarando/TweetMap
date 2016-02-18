package com.malbarando.tweetmap.utils;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class LogUtil {


    public static final String APP_TAG = "TwitterMap-Log";

    // For Debugging
    public static void printObjectValues(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = null;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                System.out.printf("%s : %s%n", name, value);
            }
        }
        return;
    }


    public static void d(String msg){
        Log.d(APP_TAG, msg);
    }

    public static void i(String msg){
        Log.i(APP_TAG, msg);
    }

    public static void w(String msg){
        Log.w(APP_TAG, msg);
    }

}
