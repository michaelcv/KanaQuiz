package com.noprestige.kanaquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

abstract class OptionsControl
{
    static private SharedPreferences sharedPreferences;
    static private SharedPreferences.Editor editor;
    static private Resources resources;

    static void initialize(Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        editor = sharedPreferences.edit();
        resources = context.getResources();
    }

    static boolean getBoolean(int resId)
    {
        return getBoolean(resources.getString(resId));
    }
    static boolean getBoolean(String prefId)
    {
        return sharedPreferences.getBoolean(prefId, false);
    }

    static void setBoolean(int resId, boolean setting)
    {
        setBoolean(resources.getString(resId), setting);
    }
    static void setBoolean(String prefId, boolean setting)
    {
        editor.putBoolean(prefId, setting);
        editor.apply();
    }

    static String getString(int resId)
    {
        return getString(resources.getString(resId));
    }
    static String getString(String prefId)
    {
        return sharedPreferences.getString(prefId, "");
    }

    static void setString(int resId, String setting)
    {
        setString(resources.getString(resId), setting);
    }
    static void setString(String prefId, String setting)
    {
        editor.putString(prefId, setting);
        editor.apply();
    }

    static boolean compareStrings(int prefId, int comparator)
    {
        return compareStrings(resources.getString(prefId), resources.getString(comparator));
    }
    static boolean compareStrings(String prefId, String comparator)
    {
        return sharedPreferences.getString(prefId, "").equals(comparator);
    }
}
