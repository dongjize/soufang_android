package com.dong.soufang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dong.soufang.GlobalData;
import com.dong.soufang.R;
import com.dong.soufang.activity.ArticleDetailActivity;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.activity.houses.EstatesMoreActivity;
import com.dong.soufang.activity.houses.NewEstateDetailActivity;
import com.dong.soufang.activity.houses.NewEstatesListActivity;
import com.dong.soufang.activity.houses.OldHouseDetailActivity;
import com.dong.soufang.activity.houses.OldHousesListActivity;
import com.dong.soufang.activity.houses.RentHousesListActivity;
import com.dong.soufang.bean.Article;
import com.dong.soufang.bean.NewEstate;
import com.dong.soufang.bean.OldHouse;
import com.dong.soufang.bean.RentHouse;
import com.dong.soufang.custom.banner.BannerItem;
import com.dong.soufang.custom.banner.BannerPagerAdapter;
import com.dong.soufang.custom.banner.BannerViewPager;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: Home房产模块
 * <p/>
 * Author: dong
 * Date: 16/3/16
 */
public class EstatesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {
    private static final String TAG = EstatesFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout recommendLayout;
    private List recommendList;

    private BannerViewPager bannerViewPager;
    private BannerPagerAdapter mAdapter;
    private List<BannerItem> bannerItems;
    private List<ImageView> viewList;
    private int[] images = {R.mipmap.sh_house_njxlgg02, R.mipmap.sh_house_njxlgg03,
            R.mipmap.sh_house_wdtxy01, R.mipmap.sh_house_wdtxy02, R.mipmap.sh_house_wdtxy04,
            R.mipmap.sh_jwthy01};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_estates, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentView.findViewById(R.id.new_layout).setOnClickListener(this);
        contentView.findViewById(R.id.old_layout).setOnClickListener(this);
        contentView.findViewById(R.id.rent_layout).setOnClickListener(this);
        contentView.findViewById(R.id.more_layout).setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recommendLayout = (LinearLayout) contentView.findViewById(R.id.recommend_layout);

        bannerViewPager = (BannerViewPager) contentView.findViewById(R.id.banner_viewpager);
        bannerItems = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            BannerItem bannerItem = new BannerItem();
            bannerItem.setTitle("title " + i);
            bannerItem.setIntro("精品学区房 紧邻地铁 买房即享98折优惠");
            bannerItem.setPicUrl("http://pic70.nipic.com/file/20150618/21278791_104700147417_2.jpg");
            bannerItems.add(bannerItem);
        }
        viewList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images[i]);
            viewList.add(imageView);
        }

        mAdapter = new BannerPagerAdapter(context, bannerViewPager, bannerItems, viewList);
        mAdapter.setOnBannerViewClickListener(new BannerPagerAdapter.OnBannerViewClickListener() {
            @Override
            public void onBannerClick(View itemView, int position) {
                Toast.makeText(context, bannerItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        bannerViewPager.setAdapter(mAdapter);

        getRecommendedEstates();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getRecommendedEstates();
    }

    /**
     * 获取推荐列表
     */
    private void getRecommendedEstates() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetHomePageIntroduceApi, params);
        ((BaseActivity) getActivity()).httpHandler.getHomeIntroduceList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                recommendList = (List) result.getData();
                if (recommendList != null) {
                    for (int i = 0; i < recommendList.size(); i++) {
                        View childView = null;
                        if (recommendList.get(i) instanceof Article) {
                            final Article article = (Article) recommendList.get(i);
                            childView = LayoutInflater.from(getActivity()).inflate(R.layout.item_articles_list, null);
//                            ImageLoaderUtils.display(article.getAvatar(), (ImageView) childView.findViewById(R.id.iv_avatar));
                            ((TextView) childView.findViewById(R.id.tv_title)).setText(article.getTitle());
                            ((TextView) childView.findViewById(R.id.tv_sub_title)).setText(article.getSubTitle());
                            childView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id", article.getId());
                                    bundle.putBoolean("is_collected", article.isCollected());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        } else if (recommendList.get(i) instanceof NewEstate) {
                            final NewEstate newEstate = (NewEstate) recommendList.get(i);
                            LatLng latLng = new LatLng(newEstate.getLatitude(), newEstate.getLongitude());
                            LatLng myLoc = new LatLng(GlobalData.LATITUDE, GlobalData.LONGITUDE);
                            String distance = String.format("%.2f", DistanceUtil.getDistance(latLng, myLoc) / 1000);
                            childView = LayoutInflater.from(getActivity()).inflate(R.layout.item_estate_list, null);
                            ImageLoaderUtils.display(HttpApi.IMAGE_URL + newEstate.getAvatar(), (ImageView) childView.findViewById(R.id.iv_avatar));
                            ((TextView) childView.findViewById(R.id.tv_estate_name)).setText(newEstate.getName());
                            ((TextView) childView.findViewById(R.id.tv_district)).setText(newEstate.getDistrict());
                            ((TextView) childView.findViewById(R.id.tv_address)).setText(newEstate.getAddress());
                            ((TextView) childView.findViewById(R.id.tv_avg_price)).setText(newEstate.getAvgPrice() + "元/平");
                            ((TextView) childView.findViewById(R.id.tv_distance_to_me)).setText("距我" + distance + "km");
                            childView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, NewEstateDetailActivity.class);
                                    intent.putExtra("id", newEstate.getId());
                                    startActivity(intent);
                                }
                            });
                        } else if (recommendList.get(i) instanceof OldHouse) {
                            final OldHouse oldHouse = (OldHouse) recommendList.get(i);

                            childView = LayoutInflater.from(getActivity()).inflate(R.layout.item_old_houses_list, null);
                            ImageLoaderUtils.display(HttpApi.IMAGE_URL + oldHouse.getAvatar(), (ImageView) childView.findViewById(R.id.iv_avatar));
                            ((TextView) childView.findViewById(R.id.tv_title)).setText(oldHouse.getTitle());
                            ((TextView) childView.findViewById(R.id.tv_area_and_type)).setText(oldHouse.getArea() + "平米 " + oldHouse.getHouseType());
                            ((TextView) childView.findViewById(R.id.tv_estate_name)).setText(oldHouse.getEstateName());
                            ((TextView) childView.findViewById(R.id.tv_sale_price)).setText(oldHouse.getSalePrice() + "万/套");
                            ((TextView) childView.findViewById(R.id.tv_estate_name)).setText(oldHouse.getEstateName());
                            childView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, OldHouseDetailActivity.class);
                                    intent.putExtra("house_id", oldHouse.getId());
                                    intent.putExtra("title", oldHouse.getTitle());
                                    startActivity(intent);
                                }
                            });
                        } else if (recommendList.get(i) instanceof RentHouse) {
                            RentHouse rentHouse = (RentHouse) recommendList.get(i);
                            childView = LayoutInflater.from(getActivity()).inflate(R.layout.item_rent_houses_list, null);


                        }
                        recommendLayout.addView(childView);
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
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.new_layout:
                intent = new Intent(context, NewEstatesListActivity.class);
                break;
            case R.id.old_layout:
                intent = new Intent(context, OldHousesListActivity.class);
                break;
            case R.id.rent_layout:
                intent = new Intent(context, RentHousesListActivity.class);
                break;
            case R.id.more_layout:
                intent = new Intent(context, EstatesMoreActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

}
