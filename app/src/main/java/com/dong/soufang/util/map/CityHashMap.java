package com.dong.soufang.util.map;

import java.util.HashMap;

/**
 * Description: 存放城市编码与城市名的键值对
 * <p/>
 * Author: dong
 * Date: 16/4/6
 */
public class CityHashMap {
    public static final HashMap<String, String> cityMap = new HashMap<>();

    static {
        cityMap.put("021", "上海");
        cityMap.put("010", "北京");
        cityMap.put("020", "广州");
        cityMap.put("0571", "杭州");
        cityMap.put("0371", "郑州");
        cityMap.put("0379", "洛阳");

        //暂时写这几个, 还有很多
    }
}
