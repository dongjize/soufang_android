package com.dong.soufang.activity.map;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.dong.soufang.GlobalData;
import com.dong.soufang.R;
import com.dong.soufang.SuggestionProvider;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.bean.NewEstate;
import com.dong.soufang.custom.ArcMenu;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.overlayutil.PoiOverlay;
import com.dong.soufang.util.BaiduMapManager;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 新房列表地图页面
 * <p/>
 * Author: dong
 * Date: 16/5/3
 */
public class NewEstatesListMapActivity extends BaseActivity implements OnGetPoiSearchResultListener,
        OnGetGeoCoderResultListener {
    private static final String TAG = NewEstatesListMapActivity.class.getSimpleName();
    private MapView mapView;
    private BaiduMap baiduMap;
    private ArcMenu arcMenu;
    private boolean isNormal = true;

    private List<NewEstate> newEstateList;

    private SearchView searchView;
    private SearchRecentSuggestions suggestions;
    private MenuItem searchItem;

    private PoiSearch poiSearch;
    private SuggestionSearch suggestionSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estates_map);
        initToolbar();

        setArcMenu();
        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        geoCoder.geocode(new GeoCodeOption().city(GlobalData.CITY).address(GlobalData.CITY));

        suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(1000);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        getNewEstatesList();
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
                            Toast.makeText(NewEstatesListMapActivity.this, "卫星模式", Toast.LENGTH_SHORT).show();
                        } else {
                            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                            Toast.makeText(NewEstatesListMapActivity.this, "普通模式", Toast.LENGTH_SHORT).show();
                        }
                        isNormal = !isNormal;
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

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getNewEstatesList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("city_code", GlobalData.CITY);
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetNewEstatesListApi, params);
        httpHandler.getNewEstatesList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                newEstateList = (List<NewEstate>) result.getData();
                //todo 将地图定位到当前城市
                if (newEstateList != null) {
                    for (int i = 0; i < newEstateList.size(); i++) {
                        LatLng latLng = new LatLng(newEstateList.get(i).getLatitude(), newEstateList.get(i).getLongitude());
                        BaiduMapManager.addMapOverlays(baiduMap, latLng);
                        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                return true;
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                Snackbar.make(null, msg, Snackbar.LENGTH_SHORT).setAction("action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        searchItem = menu.add(android.R.string.search_go);
        searchItem.setIcon(R.mipmap.ic_search_white_36dp);
        MenuItemCompat.setActionView(searchItem, searchView);
        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSearch(false);
        Bundle bundle = intent.getExtras();
        String userQuery = String.valueOf(bundle.get(SearchManager.USER_QUERY));
        String query = String.valueOf(bundle.get(SearchManager.QUERY));
        poiSearch.searchInCity((new PoiCitySearchOption()).city(GlobalData.CITY).keyword(query).pageNum(0));

    }

    private void showSearch(boolean visible) {
        if (visible) {
            MenuItemCompat.expandActionView(searchItem);
        } else {
            MenuItemCompat.collapseActionView(searchItem);
            hideKeyboard(searchView.getWindowToken());
        }
    }

    @Override
    public boolean onSearchRequested() {
        showSearch(true);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
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
        poiSearch.destroy();
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

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            return;
        }
        //获取地理编码结果
        MapStatus mapStatus = new MapStatus.Builder().target(geoCodeResult.getLocation()).zoom(12).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(msu);

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

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

}
