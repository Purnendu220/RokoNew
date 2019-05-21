package com.rokoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveCache {

    private static SharedPreferences preferences;

    private static SharedPreferences getPreference(Context con) {
        if (preferences == null) {
            preferences = con.getSharedPreferences("appPreference",Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static void save(Context context, String key, int value) {
        getPreference(context).edit().putInt(key, value).commit();
    }

    public static void save(Context context, String key, float value) {
        getPreference(context).edit().putFloat(key, value).commit();
    }

    public static void save(Context context, String key, String value) {
        getPreference(context).edit().putString(key, value).commit();
    }

    public static void save(Context context, String key, boolean value) {
        getPreference(context).edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPreference(context).getString(key, "");
    }

    public static boolean getBoolean(Context context, String key){
        return getPreference(context).getBoolean(key, false);
    }

    public static float getFloat(Context context, String key){
        return getPreference(context).getFloat(key, 0);
    }

    public static int getInt(Context context, String key) {
        return getPreference(context).getInt(key, 0);
    }
}
