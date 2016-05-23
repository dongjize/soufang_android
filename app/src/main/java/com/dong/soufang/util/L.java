package com.dong.soufang.util;

import android.util.Log;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class L {
    private static final String TAG = "soufang";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void v(String msg) {
        Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.i(tag, msg);
    }
}
