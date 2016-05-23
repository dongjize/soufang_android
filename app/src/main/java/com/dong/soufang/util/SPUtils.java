package com.dong.soufang.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Set;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class SPUtils {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SPUtils(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void addStringData(String key, String data) {
        editor.putString(key, data);
        editor.commit();
    }

    public String getStringData(String key) {
        return sp.getString(key, "");
    }

    public String getStringData(String key, String def) {
        return sp.getString(key, def);
    }

    public void addBooleanData(String key, Boolean data) {
        editor.putBoolean(key, data);
        editor.commit();
    }

    public Boolean getBooleanData(String key) {
        return sp.getBoolean(key, false);
    }

    public Boolean getBooleanData(String key, boolean def) {
        return sp.getBoolean(key, def);
    }

    public void addLongData(String key, Long data) {
        editor.putLong(key, data);
        editor.commit();
    }

    public Long getLongData(String key) {
        return sp.getLong(key, 0);
    }

    public void addFloatData(String key, Float data) {
        editor.putFloat(key, data);
        editor.commit();
    }

    public Float getFloatData(String key) {
        return sp.getFloat(key, 0);
    }

    public void addIntData(String key, Integer data) {
        editor.putInt(key, data);
        editor.commit();
    }

    public Integer getIntData(String key) {
        return sp.getInt(key, 0);
    }

    @SuppressLint("NewApi")
    public void addStringSetData(String key, Set<String> data) {
        editor.putStringSet(key, data);
        editor.commit();
    }

    @SuppressLint("NewApi")
    public Set<String> getStringSetData(String key) {
        return sp.getStringSet(key, null);
    }

    public void addObject(String key, Object object) {
        String strJson = "";
        Gson gson = new Gson();
        strJson = gson.toJson(object);
        addStringData(key, strJson);
    }
}
