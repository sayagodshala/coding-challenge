package com.sayagodshala.livesplash.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    public static final String APP_SHARED_PREFERENCE_NAME = "com.sayagodshala.dingdong";
    public static SharedPreferences prefs = null;
    public static final String PREF_GCM_REGISTRATION_ID = "PREF_GCM_REGISTRATION_ID";
    public static final String PREF_GCM_REGISTRATION_STATUS = "PREF_GCM_REGISTRATION_STATUS";

    public static final String PREF_USER_DATA = "PREF_USER_DATA";
    public static final String PREF_DATA = "PREF_DATA";



    public static final String PREF_LAT_LNG = "PREF_LAT_LNG";
    public static final String PREF_LAT_LNG_ADDRESS = "PREF_LAT_LNG_ADDRESS";


    public static String getValue(Context context, String key,
                                  String defaultValue) {
        prefs = context.getSharedPreferences(APP_SHARED_PREFERENCE_NAME, 0);
        return prefs.getString(key, defaultValue);
    }

    public static void setValue(Context context, String key, String value) {
        prefs = context.getSharedPreferences(APP_SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value.toString());
        editor.commit();
    }

    public static void clearAllPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}