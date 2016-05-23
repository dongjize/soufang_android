package com.dong.soufang.util;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dong.soufang.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Description: 百度地图工具类
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class BaiduMapManager {

    /**
     * 定位到用户所在的位置
     *
     * @param baiduMap
     * @param latitude
     * @param longitude
     */
    public static void centerToMyLocation(BaiduMap baiduMap, double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        centerToMyLocation(baiduMap, latLng);
    }

    public static void centerToMyLocation(BaiduMap baiduMap, LatLng latLng) {
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
    }

    /**
     * 将地图定位到指定坐标
     *
     * @param baiduMap  当前context中的BaiduMap对象
     * @param latitude  纬度
     * @param longitude 经度
     */
    public static void mapStatusUpdate(BaiduMap baiduMap, double latitude, double longitude) {
        if (latitude > 0 && longitude > 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(15).build();
            MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
            baiduMap.animateMapStatus(msu);
        }
    }

    /**
     * 将地图定位到指定坐标
     *
     * @param baiduMap 当前context中的BaiduMap对象
     * @param latLng   坐标
     */
    public static void mapStatusUpdate(BaiduMap baiduMap, LatLng latLng, float zoomRate) {
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(zoomRate).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(msu);
    }

    /**
     * 添加地图覆盖物
     *
     * @param baiduMap
     * @param latLng
     */
    public static void addMapOverlays(BaiduMap baiduMap, LatLng latLng) {
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.mipmap.star_red);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(bd).zIndex(5);
        baiduMap.addOverlay(options);
    }

    /**
     * 切换地图模式
     *
     * @param context
     * @param baiduMap
     */
    public static void switchMapType(Context context, BaiduMap baiduMap) {
//        if (baiduMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL) {
//            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//            Toast.makeText(context, "卫星地图", Toast.LENGTH_SHORT).show();
//        } else if (baiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
//            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//            Toast.makeText(context, "基础地图", Toast.LENGTH_SHORT).show();
//        }

    }

    /**
     * 测距
     *
     * @param nodes 屏幕记录下的一系列节点
     * @return 第一个节点到最后一个节点的折线距离
     */
    public static String getDistance(ArrayList<LatLng> nodes) {
        String popDistance;
        double curDistance;
        double distance = 0;
        nodes = new ArrayList<LatLng>();
        curDistance = DistanceUtil.getDistance(nodes.get(nodes.size() - 1), nodes.get(nodes.size() - 2));
        distance += curDistance;
        if (distance >= 1000) {
            //显示公里,保留两位小数
            popDistance = new DecimalFormat(".##%").format(distance / 1000) + "km";
        } else {
            popDistance = new DecimalFormat(".##%").format(distance) + "m";
        }
        return popDistance;
    }

    public static void poiSearch(final Context context, final BaiduMap baiduMap) {
        PoiSearch mPoiSearch = PoiSearch.newInstance();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(context, "未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    baiduMap.clear();
//                    PoiOverlay overlay = new

                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        };
    }
}
