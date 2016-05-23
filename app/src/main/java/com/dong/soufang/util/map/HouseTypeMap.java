package com.dong.soufang.util.map;

import java.util.HashMap;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/4/17
 */
public class HouseTypeMap {
    private static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("住宅", "1");
        map.put("别墅", "2");
        map.put("商铺", "3");
    }

    public static String getHouseType(String key) {
        return map.get(key);
    }
}
