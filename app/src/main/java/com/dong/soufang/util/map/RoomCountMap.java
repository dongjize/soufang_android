package com.dong.soufang.util.map;

import java.util.HashMap;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 5/15/16
 */
public class RoomCountMap {
    private static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("一居", "1");
        map.put("二居", "2");
        map.put("三居", "3");
        map.put("四居", "4");
        map.put("五居以上", "5");
    }

    public static String getRoomCount(String key) {
        return map.get(key);
    }
}
