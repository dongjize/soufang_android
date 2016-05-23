package com.dong.soufang.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.dong.soufang.GlobalData;

import java.util.UUID;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public class CommonUtils {
    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取当前版本
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据资源名得到ID
     *
     * @param context
     * @param name
     * @param defType
     * @return
     */
    public static int getResId(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getApplicationInfo().packageName);
    }

    /**
     * 根据图片名字得到图片的资源ID
     *
     * @param context
     * @param imageName
     * @return
     */
    public static int getResIdByImageName(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 同步Cookie
     *
     * @param context
     * @param url
     */
    public static void syncCookies(Context context, String url) {
        if (TextUtils.isEmpty(GlobalData.rawCookies)) {
            return;
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, GlobalData.rawCookies);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().sync();
        }

    }
}
