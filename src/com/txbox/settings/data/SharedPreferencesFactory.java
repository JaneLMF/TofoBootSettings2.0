package com.txbox.settings.data;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesFactory {
    private String key;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    protected SharedPreferencesFactory(Context curContext,String key) {
        this.key = key;
        this.mContext = curContext;
    }

    protected SharedPreferences getSharedPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = this.mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    protected boolean setValue(String key, String value){
        return getSharedPreferences().edit().putString(key, value).commit();
    }

    protected boolean setValue(String key, boolean value){
        return getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    protected boolean setValue(String key, int value){
        return getSharedPreferences().edit().putInt(key, value).commit();
    }

    protected String getString(String key){
        return getSharedPreferences().getString(key, null);
    }

    protected boolean getBoolean(String key){
        return getSharedPreferences().getBoolean(key, false);
    }

    protected boolean getBoolean(String key, boolean value){
        return getSharedPreferences().getBoolean(key, value);
    }

    protected String getString(String key, String defaultStr){
        return getSharedPreferences().getString(key, defaultStr);
    }

    protected int getInt(String key){
        return getSharedPreferences().getInt(key, -1);
    }

    protected int getInt(String key, int defaultStr){
        return getSharedPreferences().getInt(key, defaultStr);
    }
}
