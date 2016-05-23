package com.dong.soufang.util;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/4/17
 */
public class JsonUtils {
    public static HashMap<String, String> jsonObject2HashMap(JSONObject json) {
        if (json == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        for (Iterator<String> keys = json.keys(); keys.hasNext(); ) {
            String key = keys.next();
            map.put(key, json.optString(key));
        }
        return map;
    }

    public static JSONObject hashMap2JsonObject(HashMap<String, String> map) {
        if (map == null) {
            return null;
        }
        JSONObject o = new JSONObject();
        try {
            if (map != null) {
                for (String key : map.keySet()) {
                    o.put(key, map.get(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o;
    }

    public static String jsonObject2StringParams(JSONObject json) {
        if (json == null) {
            return "";
        }
        String params = "";
        for (Iterator<String> keys = json.keys(); keys.hasNext(); ) {
            String key = keys.next();
            params += key + "=" + json.optString(key) + "&";
        }
        if (params.endsWith("&")) {
            params = params.substring(0, params.length() - 1);
        }

        return params;
    }

    public static Bundle jsonObject2Bundle(JSONObject json) {
        if (json == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (Iterator<String> keys = json.keys(); keys.hasNext(); ) {
            String key = keys.next();
            bundle.putString(key,json.optString(key));
        }

        return bundle;
    }
}
