package com.dong.soufang.activity.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.dong.soufang.GlobalData;
import com.dong.soufang.MainApplication;
import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.custom.ArcMenu;
import com.dong.soufang.impl.MyLocationListener;
import com.dong.soufang.overlayutil.PoiOverlay;
import com.dong.soufang.util.BaiduMapManager;

/**
 * Description: 房产地图页
 * <p/>
 * Author: dong
 * Date: 16/4/7
 */
public class EstateDetailMapActivity extends BaseActivity implements OnGetPoiSearchResultListener,
        View.OnClickListener {
    private String name, address;
    private double longitude, latitude;
    private LatLng latLng;
    private BaiduMap baiduMap;
    private MapView mapView;
    private MyLocationListener myLocationListener;
    private LocationClient locationClient;
    private MyLocationConfiguration.LocationMode locationMode;

    private ArcMenu arcMenu;
    private boolean isNormal = true;

    private PoiSearch poiSearch;
    private String[] keywords = {"医院", "学校", "超市", "饭店"};
    private String keyword = keywords[0];
    private MenuItem clearBtn;

    private RelativeLayout floatLayout;
    private LinearLayout routeLayout;
    private TextView tvBus, tvCar, tvWalk, tvBiking;
    private EditText etStart, etEnd;
    private TextView tvSearchRoute, tvConvert;
    private int transportation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_detail_map);
        getIntentData();
        initToolbar();
        initLocation();

        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(12).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(msu);
        BaiduMapManager.addMapOverlays(baiduMap, latLng);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PopupWindow popupWindow = new PopupWindow(EstateDetailMapActivity.this);
                View contentView = LayoutInflater.from(EstateDetailMapActivity.this).inflate(R.layout.layout_map_popup_window, null);
                popupWindow.setContentView(contentView);
                TextView tvName = (TextView) contentView.findViewById(R.id.tv_poi_name);
                TextView tvAddress = (TextView) contentView.findViewById(R.id.tv_address);
                TextView tvNavi = (TextView) contentView.findViewById(R.id.tv_navi);

                tvName.setText(name);
                tvAddress.setText(address);
                tvNavi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("qwertyui");
                    }
                });
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(0);
                popupWindow.showAtLocation(mapView, Gravity.CENTER, 50, 50);
                return true;
            }
        });

        initFloatingRouteLayout();

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        setArcMenu();
    }

    private void initFloatingRouteLayout() {
        floatLayout = (RelativeLayout) findViewById(R.id.float_layout);
        routeLayout = (LinearLayout) findViewById(R.id.search_route_layout);

        etStart = (EditText) findViewById(R.id.et_start);
        etEnd = (EditText) findViewById(R.id.et_end);
        etStart.setText("我的位置");
        etEnd.setText(name);
        tvConvert = (TextView) findViewById(R.id.tv_convert);
        tvConvert.setOnClickListener(this);
        tvSearchRoute = (TextView) findViewById(R.id.tv_search_route);
        tvSearchRoute.setOnClickListener(this);

        tvBus = (TextView) findViewById(R.id.tv_bus);
        tvCar = (TextView) findViewById(R.id.tv_car);
        tvWalk = (TextView) findViewById(R.id.tv_walk);
        tvBiking = (TextView) findViewById(R.id.tv_biking);
        tvBus.setOnClickListener(this);
        tvCar.setOnClickListener(this);
        tvWalk.setOnClickListener(this);
        tvBiking.setOnClickListener(this);

    }

    private void setTransportationSelected(int position) {
        transportation = position;
        tvBus.setTextColor(getResources().getColor(R.color.black));
        tvCar.setTextColor(getResources().getColor(R.color.black));
        tvWalk.setTextColor(getResources().getColor(R.color.black));
        tvBiking.setTextColor(getResources().getColor(R.color.black));
        tvBus.setBackgroundResource(R.drawable.white_bg_with_blue_border);
        tvCar.setBackgroundResource(R.drawable.white_bg_with_blue_border);
        tvWalk.setBackgroundResource(R.drawable.white_bg_with_blue_border);
        tvBiking.setBackgroundResource(R.drawable.white_bg_with_blue_border);

        switch (position) {
            case 0:
                tvBus.setTextColor(getResources().getColor(R.color.white));
                tvBus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                tvCar.setTextColor(getResources().getColor(R.color.white));
                tvCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                tvWalk.setTextColor(getResources().getColor(R.color.white));
                tvWalk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                tvBiking.setTextColor(getResources().getColor(R.color.white));
                tvBiking.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
        }

    }

    private void setArcMenu() {
        arcMenu = (ArcMenu) findViewById(R.id.arcmenu);
        arcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                switch (pos) {
                    case 0:
                        //切换地图模式
                        if (isNormal) {
                            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                            Toast.makeText(EstateDetailMapActivity.this, "卫星模式", Toast.LENGTH_SHORT).show();
                        } else {
                            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                            Toast.makeText(EstateDetailMapActivity.this, "普通模式", Toast.LENGTH_SHORT).show();
                        }
                        isNormal = !isNormal;
                        break;
                    case 1:
                        //周边查找
                        new AlertDialog.Builder(EstateDetailMapActivity.this)
                                .setTitle("查找附近的: ")
                                .setSingleChoiceItems(keywords, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        keyword = keywords[which];
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        PoiNearbySearchOption option = new PoiNearbySearchOption();
                                        option.location(new LatLng(latitude, longitude))
                                                .keyword(keyword)
                                                .radius(2000)
                                                .sortType(PoiSortType.distance_from_near_to_far);
                                        poiSearch.searchNearby(option);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        clearBtn.setVisible(true);
                        clearBtn.setEnabled(true);
                        break;
                    case 2:
                        //路线搜索
                        floatLayout.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(EstateDetailMapActivity.this, R.anim.anim_appear_from_top);
                        routeLayout.setAnimation(animation);

                        break;
                    case 3:
                        //定位
                        LatLng latLng = new LatLng(GlobalData.LATITUDE, GlobalData.LONGITUDE);
                        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                        baiduMap.animateMapStatus(msu);
                        break;

                }
            }
        });
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name", "");
        address = bundle.getString("address", "");
        longitude = bundle.getDouble("longitude");
        latitude = bundle.getDouble("latitude");
        latLng = new LatLng(latitude, longitude);
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        locationClient = new LocationClient(getApplicationContext());
        myLocationListener = MainApplication.getInstance().myLocationListener;
        locationClient.registerLocationListener(myLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        locationClient.setLocOption(option);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear_baidumap, menu);
        clearBtn = toolbar.getMenu().findItem(R.id.action_clear);
        clearBtn.setEnabled(false);
        clearBtn.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                baiduMap.clear();
                clearBtn.setEnabled(false);
                clearBtn.setVisible(false);
                break;
            case android.R.id.home:
                if (floatLayout.getVisibility() == View.VISIBLE) {
                    floatLayout.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;
        }
        return true;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            showToast("抱歉, 没找到");
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            baiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
            BaiduMapManager.mapStatusUpdate(baiduMap, poiDetailResult.getLocation(), baiduMap.getMaxZoomLevel());
            showToast(poiDetailResult.getName());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_convert:
                String start = etStart.getText().toString() + "";
                String end = etEnd.getText().toString() + "";
                etStart.setText(end);
                etEnd.setText(start);
                break;
            case R.id.tv_search_route:
                Animation animation = AnimationUtils.loadAnimation(EstateDetailMapActivity.this, R.anim.anim_disappear_to_top);
                routeLayout.setAnimation(animation);
                floatLayout.setVisibility(View.GONE);

                Intent intent = new Intent(EstateDetailMapActivity.this, RoutePlanResultActivity.class);
                intent.putExtra("transportation", transportation);
                intent.putExtra("latlng", latLng);
                startActivity(intent);
                break;
            case R.id.tv_bus:
                setTransportationSelected(0);
                break;
            case R.id.tv_car:
                setTransportationSelected(1);
                break;
            case R.id.tv_walk:
                setTransportationSelected(2);
                break;
            case R.id.tv_biking:
                setTransportationSelected(3);
                break;

        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(i);
//            if (poiInfo.hasCaterDetails) {
            poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
//            }
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
