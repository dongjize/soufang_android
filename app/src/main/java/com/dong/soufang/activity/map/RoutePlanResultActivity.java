package com.dong.soufang.activity.map;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.dong.soufang.GlobalData;
import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.overlayutil.BikingRouteOverlay;
import com.dong.soufang.overlayutil.DrivingRouteOverlay;
import com.dong.soufang.overlayutil.TransitRouteOverlay;
import com.dong.soufang.overlayutil.WalkingRouteOverlay;
import com.dong.soufang.util.BaiduMapManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 路线规划结果页面
 * <p>
 * Author: dong
 * Date: 5/20/16
 */
public class RoutePlanResultActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        OnGetRoutePlanResultListener {
    private MapView mapView;
    private BaiduMap baiduMap;
    private TabLayout mTabLayout;
//    private ViewPager routePager;
//    private List<View> viewList;

    private int transportation;
    private LatLng latLng;
    private RoutePlanSearch routePlanSearch = null;
    private RouteLine routeLine = null;
    private List<? extends RouteLine> routeLineList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_result);

        transportation = getIntent().getIntExtra("transportation", 0);
        latLng = getIntent().getParcelableExtra("latlng");

        initToolbar();

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

//        routePager = (ViewPager) findViewById(R.id.route_pager);
        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();


        List<String> titles = new ArrayList<>();
        titles.add("公交");
        titles.add("驾车");
        titles.add("步行");
        titles.add("骑行");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)), transportation == i ? true : false);
        }

        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);

        mTabLayout.setOnTabSelectedListener(this);

        executeRouteSearch(transportation);

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void executeRouteSearch(int transportation) {
        PlanNode startNode = PlanNode.withLocation(GlobalData.LATLNG);
        PlanNode endNode = PlanNode.withLocation(latLng);
//        PlanNode endNode = PlanNode.withCityNameAndPlaceName(GlobalData.CITY, etEnd.getText().toString());

        switch (transportation) {
            case 0:
                routePlanSearch.transitSearch(new TransitRoutePlanOption().city(GlobalData.CITY).from(startNode).to(endNode));
                break;
            case 1:
                routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(startNode).to(endNode));
                break;
            case 2:
                routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(startNode).to(endNode));
                break;
            case 3:
                routePlanSearch.bikingSearch(new BikingRoutePlanOption().from(startNode).to(endNode));
                break;
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanResultActivity.this, "查询结果为空", Toast.LENGTH_SHORT).show();
            BaiduMapManager.mapStatusUpdate(baiduMap, latLng, 14);
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            routeLineList = walkingRouteResult.getRouteLines();
            routeLine = routeLineList.get(0);
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData((WalkingRouteLine) routeLine);
            overlay.addToMap();
            overlay.zoomToSpan();
            int duration = routeLine.getDuration() / 60;
            showToast("距离: " + routeLine.getDistance() / 1000 + "km\n用时: " + duration / 60 + "h" + duration % 60 + "min");
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanResultActivity.this, "查询结果为空", Toast.LENGTH_SHORT).show();
            BaiduMapManager.mapStatusUpdate(baiduMap, latLng, 14);
        }
        if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

        }
        if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            routeLineList = transitRouteResult.getRouteLines();
            routeLine = routeLineList.get(0);
            TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData((TransitRouteLine) routeLine);
            overlay.addToMap();
            overlay.zoomToSpan();
            int duration = routeLine.getDuration() / 60;
            showToast("距离: " + routeLine.getDistance() / 1000 + "km\n用时: " + duration / 60 + "h" + duration % 60 + "min");
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanResultActivity.this, "查询结果为空", Toast.LENGTH_SHORT).show();
            BaiduMapManager.mapStatusUpdate(baiduMap, latLng, 14);
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            routeLineList = drivingRouteResult.getRouteLines();
            routeLine = routeLineList.get(0);
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData((DrivingRouteLine) routeLine);
            overlay.addToMap();
            overlay.zoomToSpan();
            int duration = routeLine.getDuration() / 60;
            showToast("距离: " + routeLine.getDistance() / 1000 + "km\n用时: " + duration / 60 + "h" + duration % 60 + "min");
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanResultActivity.this, "查询结果为空", Toast.LENGTH_SHORT).show();
            BaiduMapManager.mapStatusUpdate(baiduMap, latLng, 14);
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            routeLineList = bikingRouteResult.getRouteLines();
            routeLine = routeLineList.get(0);
            BikingRouteOverlay overlay = new BikingRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData((BikingRouteLine) routeLine);
            overlay.addToMap();
            overlay.zoomToSpan();
            int duration = routeLine.getDuration() / 60;
            showToast("距离: " + routeLine.getDistance() / 1000 + "km\n用时: " + duration / 60 + "h" + duration % 60 + "min");
        }
    }

//    private class ViewPagerAdapter extends PagerAdapter {
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            View contentView = LayoutInflater.from(RoutePlanResultActivity.this).
//                    inflate(R.layout.item_routes_viewpager, null);
//            TextView tvDistance = (TextView) contentView.findViewById(R.id.tv_distance);
//            TextView tvDuration = (TextView) contentView.findViewById(R.id.tv_duration);
//            tvDistance.setText(routeLine.getDistance() + "km");
//            tvDuration.setText("用时" + routeLine.getDuration() + "");
//            ((ViewPager) container).addView(contentView);
//            return contentView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager) container).removeView(viewList.get(position));
//        }
//
//        @Override
//        public int getCount() {
//            return routeLineList.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    }

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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        baiduMap.clear();
        executeRouteSearch(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
