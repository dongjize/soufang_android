package com.dong.soufang.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MyLocationData;
import com.dong.soufang.GlobalData;
import com.dong.soufang.util.map.CityHashMap;

/**
 * Description: 百度地图位置监听器
 * <p/>
 * Author: dong
 * Date: 5/10/16
 */
public class MyLocationListener implements BDLocationListener {
    private Context context;
    private MyLocationData locationData;
    private String currentCity;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        locationData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                .latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude())
                .build();
        currentCity = bdLocation.getCity();

        String city = CityHashMap.cityMap.get(GlobalData.CITY);
        if (!TextUtils.isEmpty(city) && currentCity != city) {
            new AlertDialog.Builder(context)
                    .setTitle("您定位城市发生变化,是否选择" + currentCity + "为当前城市?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GlobalData.CITY = currentCity;
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

        GlobalData.LATITUDE = bdLocation.getLatitude();
        GlobalData.LONGITUDE = bdLocation.getLongitude();
    }

}
