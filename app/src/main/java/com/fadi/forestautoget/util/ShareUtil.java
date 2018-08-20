package com.fadi.forestautoget.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class ShareUtil {

    private Context mContext;
    private String filename = null;

    public ShareUtil(Context context) {
        mContext = context;
    }

    public ShareUtil(Context context, String shareFile) {
        mContext = context;
        filename = shareFile;
    }

    private SharedPreferences getPreferences() {
        if (filename == null) {
            return PreferenceManager.getDefaultSharedPreferences(mContext);
        } else {
            return mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        }
    }

    private Editor getEditor() {
        if (filename == null) {
            return PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        } else {
            return mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public boolean setShare(String key, boolean value) {
        return getEditor().putBoolean(key, value).commit();
    }

    public boolean setShare(String key, float value) {
        return getEditor().putFloat(key, value).commit();
    }

    public boolean setShare(String key, int value) {
        return getEditor().putInt(key, value).commit();
    }

    public boolean setShare(String key, long value) {
        return getEditor().putLong(key, value).commit();
    }

    public boolean setShare(String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    public void removeShare(String key) {
        getEditor().remove(key).commit();
    }

}
