package com.dong.soufang.activity.houses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.dong.soufang.R;
import com.dong.soufang.activity.HousesOfEstateActivity;
import com.dong.soufang.activity.ShowImageActivity;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.activity.map.EstateDetailMapActivity;
import com.dong.soufang.adapter.HousesOfNewEstatesAdapter;
import com.dong.soufang.bean.NewEstate;
import com.dong.soufang.bean.NewHouse;
import com.dong.soufang.custom.banner.BannerItem;
import com.dong.soufang.custom.banner.BannerPagerAdapter;
import com.dong.soufang.custom.banner.BannerViewPager;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.BaiduMapManager;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 新房详情页
 * <p/>
 * Author: dong
 * Date: 16/4/17
 */
public class NewEstateDetailActivity extends BaseActivity implements OnListItemClickListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NewEstateDetailActivity.class.getSimpleName();
    private int estateId;
    private NewEstate newEstate;
    private TextView tvName, tvPrice, tvAddress, tvOpenDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView housesRecyclerView;
    private List<NewHouse> houseList;
    private HousesOfNewEstatesAdapter mAdapter;
    private LinearLayout houseListLayout;
    private MapView mapView;
    private BaiduMap baiduMap;

    private BannerViewPager bannerViewPager;
    private BannerPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estate_detail);

        initToolbar();

        estateId = getIntExtra("id", 0);

        tvName = (TextView) findViewById(R.id.tv_estate_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvOpenDate = (TextView) findViewById(R.id.tv_open_date);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        housesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        houseListLayout = (LinearLayout) findViewById(R.id.houses_list_layout);
        findViewById(R.id.map_layout).setOnClickListener(this);
        findViewById(R.id.tv_see_all).setOnClickListener(this);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.getUiSettings().setScrollGesturesEnabled(false);

        bannerViewPager = (BannerViewPager) findViewById(R.id.banner_viewpager);

        getEstateDetails();

    }

    private void getEstateDetails() {
        swipeRefreshLayout.setRefreshing(false);
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetNewEstateDetailsApi + estateId, params);
        httpHandler.getEstateDetails(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                newEstate = (NewEstate) result.getData();
                if (newEstate != null) {
                    LatLng latLng = new LatLng(newEstate.getLatitude(), newEstate.getLongitude());
                    BaiduMapManager.mapStatusUpdate(baiduMap, latLng, 12);
                    BaiduMapManager.addMapOverlays(baiduMap, latLng);
                    tvName.setText(newEstate.getName());
                    tvPrice.setText(newEstate.getAvgPrice() + "元/平方米");
                    tvAddress.setText("地址: " + newEstate.getAddress());
                    tvOpenDate.setText("开盘: " + newEstate.getOpenDate());

                    List<BannerItem> bannerItems = new ArrayList<>();
                    for (int i = 0; i < newEstate.getPictures().size(); i++) {
                        BannerItem bannerItem = new BannerItem();
                        bannerItem.setPicUrl(newEstate.getPictures().get(i));
                        bannerItems.add(bannerItem);
                    }

                    pagerAdapter = new BannerPagerAdapter(NewEstateDetailActivity.this, bannerViewPager, bannerItems);
                    pagerAdapter.setOnBannerViewClickListener(new BannerPagerAdapter.OnBannerViewClickListener() {
                        @Override
                        public void onBannerClick(View itemView, int position) {
                            Intent intent = new Intent(NewEstateDetailActivity.this, ShowImageActivity.class);
                            intent.putExtra("urls", newEstate.getPictures());
                            intent.putExtra("index", position);
                            startActivity(intent);
                        }
                    });
                    bannerViewPager.setAdapter(pagerAdapter);

                    houseList = newEstate.getNewHouses();

                    if (houseList != null) {
                        int length = houseList.size() > 3 ? 3 : houseList.size();
                        for (int i = 0; i < length; i++) {
                            View view = View.inflate(NewEstateDetailActivity.this, R.layout.item_hot_new_houses, null);
                            TextView tvHouseTitle = (TextView) view.findViewById(R.id.tv_title);
                            TextView tvHousePrice = (TextView) view.findViewById(R.id.tv_price);
                            TextView tvStorage = (TextView) view.findViewById(R.id.tv_storage);
                            ImageView ivHouseAvatar = (ImageView) view.findViewById(R.id.iv_avatar);

                            final NewHouse newHouse = houseList.get(i);

                            tvHouseTitle.setText(newHouse.getName());
                            tvHousePrice.setText((int) (newHouse.getPrice() * newHouse.getArea() / 10000) + "万元/套");
                            tvStorage.setText("剩余" + newHouse.getStorage() + "套");
                            ImageLoaderUtils.display(HttpApi.IMAGE_URL + newHouse.getPhotos().get(0), ivHouseAvatar, R.mipmap.ic_launcher);
                            houseListLayout.addView(view);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showToast(newHouse.getId() + "");
                                }
                            });
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(NewEstateDetailActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        housesRecyclerView.setLayoutManager(layoutManager);
                        housesRecyclerView.setAdapter(mAdapter = new HousesOfNewEstatesAdapter(NewEstateDetailActivity.this, houseList));
                        mAdapter.setOnListItemClickListener(NewEstateDetailActivity.this);
                    }

                }

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.map_layout:
                intent = new Intent(NewEstateDetailActivity.this, EstateDetailMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", newEstate.getName());
                bundle.putString("address", newEstate.getAddress());
                bundle.putDouble("longitude", newEstate.getLongitude());
                bundle.putDouble("latitude", newEstate.getLatitude());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_see_all:
                intent = new Intent(NewEstateDetailActivity.this, HousesOfEstateActivity.class);
                intent.putExtra("estate_id", estateId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick(View itemView, int position) {

    }

    @Override
    public void onLongClick(View itemView, int position) {

    }

    @Override
    public void onRefresh() {
        getEstateDetails();
    }
}
