package com.dong.soufang;

import com.baidu.mapapi.model.LatLng;
import com.dong.soufang.bean.User;

/**
 * Description: 全局变量和全局常量
 * <p/>
 * Author: dong
 * Date: 16/3/15
 */
public class GlobalData {
    public static final boolean IS_DEBUG = true;
    public static boolean isExit = false;
    public static User USER = new User();
    public static boolean isLogin = false;

    public static String rawCookies;
    public static String VERSION = "";
    public static String OBJECTNO = "";

    public static float DENSITY = 0;
    public static int screenWidth;

    public static int screenHeight;
    public static String CITY = "上海"; //TODO
    public static String CURRENT_CITY;

    public static double LATITUDE = 31.038518;
    public static double LONGITUDE = 121.456701;
    public static LatLng LATLNG = new LatLng(LATITUDE, LONGITUDE);

    public static final String RELOGIN_ACTION = "com.dong.soufang.RELOGIN";

    //UniversalImageLoader的缓存期限
    public static final long DiscCacheLimitTime = 3600 * 24 * 15L;
    public static final int ImageLoaderCacheSize = 20 * 1024 * 1024;

}
