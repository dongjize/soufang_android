package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.HousesOfEstateListAdapter;
import com.dong.soufang.bean.NewHouse;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.impl.OnListItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 全部户型列表页
 * <p/>
 * Author: dong
 * Date: 5/13/16
 */
public class HousesOfEstateActivity extends BaseActivity {
    private static final String TAG = HousesOfEstateActivity.class.getSimpleName();
    private int estateId;
    private List<NewHouse> newHouseList;
    private RecyclerView recyclerView;
    private HousesOfEstateListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses_of_estate);
        initToolbar();

        estateId = getIntExtra("estate_id", 0);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getHousesOfEstate();

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getHousesOfEstate() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetHousesOfNewEstateApi + estateId + "/houses", params);
        httpHandler.getHousesOfNewEstate(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                newHouseList = (List<NewHouse>) result.getData();
                if (newHouseList != null) {
                    if (mAdapter == null) {
                        mAdapter = new HousesOfEstateListAdapter(HousesOfEstateActivity.this, newHouseList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HousesOfEstateActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.setOnListItemClickListener(new OnListItemClickListener() {
                            @Override
                            public void onClick(View itemView, int position) {
                                showToast(position + "");
                            }

                            @Override
                            public void onLongClick(View itemView, int position) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
