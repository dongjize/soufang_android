package com.dong.soufang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.CityListAdapter;
import com.dong.soufang.bean.City;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.map.CityHashMap;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 切换城市页面
 * <p>
 * Author: dong
 * Date: 16/3/29
 */
public class SelectCityActivity extends BaseActivity implements OnListItemClickListener {
    private static final String TAG = SelectCityActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private CityListAdapter mAdapter;
    private List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        initToolbar();

//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getCityList();
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 获取城市列表
     */
    private void getCityList() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetCityListApi, params);
        httpHandler.getCityList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                cityList = (List<City>) result.getData();
                if (cityList != null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(SelectCityActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    if (mAdapter == null) {
                        mAdapter = new CityListAdapter(SelectCityActivity.this, cityList);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(SelectCityActivity.this);
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
    public void onClick(View itemView, int position) {
        showToast("将当前城市修改为"+ CityHashMap.cityMap.get(cityList.get(position).getCityCode()));
        Intent intent = new Intent(SelectCityActivity.this, MainActivity.class);
        intent.putExtra("city", cityList.get(position).getCityCode());
        setResult(100, intent);
        finish();
    }

    @Override
    public void onLongClick(View itemView, int position) {

    }
}
