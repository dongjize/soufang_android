package com.dong.soufang.activity.houses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.dong.soufang.R;
import com.dong.soufang.activity.ShowImageActivity;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.activity.map.EstateDetailMapActivity;
import com.dong.soufang.bean.OldHouse;
import com.dong.soufang.custom.banner.BannerItem;
import com.dong.soufang.custom.banner.BannerPagerAdapter;
import com.dong.soufang.custom.banner.BannerViewPager;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.util.BaiduMapManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 二手房详情页
 * <p/>
 * Author: dong
 * Date: 16/4/8
 */
public class OldHouseDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OldHouseDetailActivity.class.getSimpleName();
    private int id;
    private TextView tvTitle, tvHouseType, tvAddress, tvSalePrice, tvArea, tvFloor, tvYear,
            tvDecoration, tvEstateName;
    private MapView mapView;
    private BaiduMap baiduMap;
    private double longitude, latitude;
    private OldHouse oldHouse;

    private BannerViewPager bannerViewPager;
    private BannerPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_house_detail);

        id = getIntent().getIntExtra("house_id", 0);
        initToolbar();

        bannerViewPager = (BannerViewPager) findViewById(R.id.banner_viewpager);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.getUiSettings().setScrollGesturesEnabled(false);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSalePrice = (TextView) findViewById(R.id.tv_sale_price);
        tvHouseType = (TextView) findViewById(R.id.tv_house_type);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvArea = (TextView) findViewById(R.id.tv_area);
        tvFloor = (TextView) findViewById(R.id.tv_floor);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvDecoration = (TextView) findViewById(R.id.tv_decoration);
        tvEstateName = (TextView) findViewById(R.id.tv_estate_name);
        findViewById(R.id.map_layout).setOnClickListener(this);

        getOldHouseDetails();
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getStringExtra("title"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getOldHouseDetails() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetOldHouseDetailsApi + id, params);
        httpHandler.getOldHouseDetails(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                oldHouse = (OldHouse) result.getData();
                if (oldHouse!=null){
                    LatLng latLng = new LatLng(oldHouse.getLatitude(), oldHouse.getLongitude());
                    BaiduMapManager.mapStatusUpdate(baiduMap, latLng, 12);
                    BaiduMapManager.addMapOverlays(baiduMap, latLng);

                    longitude = oldHouse.getLongitude();
                    latitude = oldHouse.getLatitude();
                    tvTitle.setText(oldHouse.getTitle());
                    tvSalePrice.setText("售价: " + oldHouse.getSalePrice() + "万元");
                    tvAddress.setText("地址: " + oldHouse.getAddress());
                    tvYear.setText("年代: " + oldHouse.getYear());
                    tvArea.setText("面积: " + oldHouse.getArea() + "m2");
                    tvFloor.setText("楼层: " + oldHouse.getFloor() + "");
                    tvHouseType.setText("户型: " + oldHouse.getHouseType());
                    tvDecoration.setText("装修: " + oldHouse.getDecoration());

                    List<BannerItem> bannerItems = new ArrayList<>();
                    for (int i = 0; i < oldHouse.getPhotos().size(); i++) {
                        BannerItem bannerItem = new BannerItem();
                        bannerItem.setPicUrl(oldHouse.getPhotos().get(i));
                        bannerItems.add(bannerItem);
                    }

                    pagerAdapter = new BannerPagerAdapter(OldHouseDetailActivity.this, bannerViewPager, bannerItems);
                    pagerAdapter.setOnBannerViewClickListener(new BannerPagerAdapter.OnBannerViewClickListener() {
                        @Override
                        public void onBannerClick(View itemView, int position) {
                            Intent intent = new Intent(OldHouseDetailActivity.this, ShowImageActivity.class);
                            intent.putExtra("urls", oldHouse.getPhotos());
                            intent.putExtra("index", position);
                            startActivity(intent);
                        }
                    });
                    bannerViewPager.setAdapter(pagerAdapter);
                }

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_heart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collect:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.map_layout:
                intent = new Intent(OldHouseDetailActivity.this, EstateDetailMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", oldHouse.getEstateName());
                bundle.putString("address", oldHouse.getAddress());
                bundle.putDouble("longitude", oldHouse.getLongitude());
                bundle.putDouble("latitude", oldHouse.getLatitude());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
