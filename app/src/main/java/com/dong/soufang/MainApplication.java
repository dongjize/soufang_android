package com.dong.soufang;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.dong.soufang.http.RequestUtils;
import com.dong.soufang.impl.MyLocationListener;
import com.dong.soufang.util.CommonUtils;
import com.dong.soufang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Description: 全局Application类
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class MainApplication extends Application {
    private static MainApplication application;
    private List<Activity> activityList = new LinkedList<>();
    public MyLocationListener myLocationListener;
    public LocationClient locationClient;
    public MyLocationConfiguration.LocationMode locationMode;
//    public static MyLocationData locationData;

    public static MainApplication getInstance() {
        if (null == application) {
            application = new MainApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalData.DENSITY = CommonUtils.getDensity(getApplicationContext());
        GlobalData.VERSION = CommonUtils.getVersion(getApplicationContext());
        GlobalData.OBJECTNO = "soufang";

        application = this;
        activityList = new ArrayList<>();

        init();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void init() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        GlobalData.screenWidth = wm.getDefaultDisplay().getWidth();
        GlobalData.screenHeight = wm.getDefaultDisplay().getHeight();

//        CrashHandler.getInstance().init(getApplicationContext());

        RequestUtils.initRequest(getApplicationContext());
        checkUniversalImageLoaderConfig(getApplicationContext());

        initBaiduMap();

    }

    /**
     * 初始化百度地图配置
     */
    private void initBaiduMap() {
        SDKInitializer.initialize(this);
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        locationClient = new LocationClient(this.getApplicationContext());
        myLocationListener = new MyLocationListener(getApplicationContext());
        locationClient.registerLocationListener(myLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void finishAllActivities() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public void exitApplication() {
        finishAllActivities();
    }

    /**
     * UniversalImageLoader相关配置
     *
     * @param context
     */
    private void checkUniversalImageLoaderConfig(Context context) {
        if (!ImageLoaderUtils.checkImageLoader()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2) //开3个线程
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheSize(GlobalData.ImageLoaderCacheSize)
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCache(new LimitedAgeDiskCache(StorageUtils.getCacheDirectory(context), GlobalData.DiscCacheLimitTime))
                    .build();
            ImageLoader.getInstance().init(config);
        }
    }

}
