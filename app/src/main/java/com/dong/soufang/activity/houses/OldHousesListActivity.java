package com.dong.soufang.activity.houses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.activity.map.OldHousesListMapActivity;
import com.dong.soufang.adapter.FilterParamsAdapter;
import com.dong.soufang.adapter.OldHousesListAdapter;
import com.dong.soufang.bean.District;
import com.dong.soufang.bean.OldHouse;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.IoUtils;
import com.dong.soufang.util.JsonUtils;
import com.dong.soufang.util.map.RoomCountMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 二手房列表
 * <p/>
 * Author: dong
 * Date: 16/4/10
 */
public class OldHousesListActivity extends BaseActivity implements View.OnClickListener,
        OnListItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = OldHousesListActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<OldHouse> oldHouseList;
    private OldHousesListAdapter mAdapter;

    private String district, roomCount, priceLow, priceHigh, areaSmall, areaBig;
    private LinearLayout districtBtn, areaBtn, roomCountBtn, priceBtn;
    private LinearLayout floatLayer;
    private ListView listView;

    private HashMap<String, Object> filterParams;
    private List<District> districts;
    private List<String> districtParams, areaParams, roomCountParams, priceParams;
    private String filterUrl;
    private boolean isListOpen = false;
    private FilterParamsAdapter fAdapter;
    private int currentList = -1;
    private String parameters = "";

    private final String TABLE_NAME = "districts.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_houses_list);

        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        districtBtn = (LinearLayout) findViewById(R.id.district);
        districtBtn.setOnClickListener(this);
        areaBtn = (LinearLayout) findViewById(R.id.area);
        areaBtn.setOnClickListener(this);
        roomCountBtn = (LinearLayout) findViewById(R.id.room_count);
        roomCountBtn.setOnClickListener(this);
        priceBtn = (LinearLayout) findViewById(R.id.price);
        priceBtn.setOnClickListener(this);

        floatLayer = (LinearLayout) findViewById(R.id.float_layer);
        listView = (ListView) floatLayer.findViewById(R.id.float_list_view);
        oldHouseList = new ArrayList<>();

        getFilterParams();
        getOldHousesList();
    }

    private void getFilterParams() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetOldHouseFilterVariablesApi, params);
        httpHandler.getOldHousesFilters(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                filterParams = (HashMap<String, Object>) result.getData();
                districtParams = new ArrayList<>();
                districts = (List<District>) filterParams.get("districts");
                for (District district : districts) {
                    districtParams.add(district.getName());
                }
                districtParams.add(0, "不限");
                areaParams = (List<String>) filterParams.get("areas");
                roomCountParams = (List<String>) filterParams.get("house_types");
                priceParams = (List<String>) filterParams.get("sale_prices");

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });

    }

    private void getOldHousesList() {
        swipeRefreshLayout.setRefreshing(false);
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetOldHousesListApi, params);
        httpHandler.getOldHousesList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                oldHouseList = (List<OldHouse>) result.getData();
                LinearLayoutManager layoutManager = new LinearLayoutManager(OldHousesListActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(mAdapter = new OldHousesListAdapter(OldHousesListActivity.this, oldHouseList));
                mAdapter.setOnListItemClickListener(OldHousesListActivity.this);
            }

            @Override
            public void onFailure(String msg) {
                Snackbar.make(null, msg, Snackbar.LENGTH_SHORT).setAction("action", null).show();
            }
        });
    }


    private void getFilteredOldHousesList() {
        HashMap<String, Object> params = new HashMap<>();
        parameters = (TextUtils.isEmpty(district) ? "" : "a" + district)
                + ((TextUtils.isEmpty(priceLow) || TextUtils.isEmpty(priceHigh)) ? "" : "__b" + priceLow + "c" + priceHigh)
                + ((TextUtils.isEmpty(areaSmall) || TextUtils.isEmpty(areaBig)) ? "" : "__d" + priceLow + "e" + priceHigh)
                + (TextUtils.isEmpty(roomCount) ? "" : "__f" + roomCount);

        if (parameters.startsWith("__")) {
            parameters = parameters.substring(2);
        }
        if (TextUtils.isEmpty(parameters)) {
            filterUrl = HttpApi.GetOldHousesListApi;
        } else {
            filterUrl = HttpApi.GetFilteredOldHousesApi + parameters;
        }

        RequestBean requestBean = new RequestBean(TAG, filterUrl, params);
        httpHandler.getFilteredOldHouses(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                oldHouseList = (List<OldHouse>) result.getData();
                if (oldHouseList != null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(OldHousesListActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(mAdapter = new OldHousesListAdapter(OldHousesListActivity.this, oldHouseList));
                    mAdapter.setOnListItemClickListener(OldHousesListActivity.this);
                }
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    private void setArrayAdapter(List<String> params) {
        if (!isListOpen) {
            floatLayer.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(false);
        }
        fAdapter = new FilterParamsAdapter(this, params);
        listView.setAdapter(fAdapter);

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.district:
                if (currentList == 0) {
                    floatLayer.setVisibility(View.GONE);
                    swipeRefreshLayout.setEnabled(true);
                    currentList = -1;
                } else {
                    setArrayAdapter(districtParams);
                    currentList = 0;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                String s = IoUtils.readFromAssets(OldHousesListActivity.this, TABLE_NAME);
                                if (!TextUtils.isEmpty(s)) {
                                    try {
                                        JSONObject json = new JSONObject(s);
                                        HashMap<String, String> map = JsonUtils.jsonObject2HashMap(json);
                                        district = map.get(districtParams.get(position));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                district = "";
                            }
                            getFilteredOldHousesList();
                            floatLayer.setVisibility(View.GONE);
                            swipeRefreshLayout.setEnabled(true);
                        }
                    });

                }
                break;

            case R.id.price:
                if (currentList == 1) {
                    floatLayer.setVisibility(View.GONE);
                    swipeRefreshLayout.setEnabled(true);
                    currentList = -1;
                } else {
                    setArrayAdapter(priceParams);
                    currentList = 1;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                String s = priceParams.get(position);
                                String[] sArr = s.split("-");
                                priceLow = sArr[0];
                                priceHigh = sArr[1].substring(0, sArr[1].length() - 1); //去掉字符串中的"万"字
                            } else {
                                priceLow = "";
                                priceHigh = "";
                            }
                            getFilteredOldHousesList();
                            floatLayer.setVisibility(View.GONE);
                            swipeRefreshLayout.setEnabled(true);
                        }
                    });
                }
                break;

            case R.id.area:
                if (currentList == 2) {
                    floatLayer.setVisibility(View.GONE);
                    swipeRefreshLayout.setEnabled(true);
                    currentList = -1;
                } else {
                    setArrayAdapter(areaParams);
                    currentList = 2;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                String s = areaParams.get(position);
                                String[] sArr = s.split("-");
                                areaSmall = sArr[0];
                                areaBig = sArr[1];
                            } else {
                                areaSmall = "";
                                areaBig = "";
                            }
                            getFilteredOldHousesList();
                            floatLayer.setVisibility(View.GONE);
                            swipeRefreshLayout.setEnabled(true);
                        }
                    });
                }
                break;

            case R.id.type:
                if (currentList == 3) {
                    floatLayer.setVisibility(View.GONE);
                    swipeRefreshLayout.setEnabled(true);
                    currentList = -1;
                } else {
                    setArrayAdapter(roomCountParams);
                    currentList = 3;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                roomCount = RoomCountMap.getRoomCount(roomCountParams.get(position));
                            } else {
                                roomCount = "";
                            }
                            getFilteredOldHousesList();
                            floatLayer.setVisibility(View.GONE);
                            swipeRefreshLayout.setEnabled(true);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onClick(View itemView, int position) {
        OldHouse oldHouse = oldHouseList.get(position);
        Intent intent = new Intent(OldHousesListActivity.this, OldHouseDetailActivity.class);
        intent.putExtra("house_id", oldHouse.getId());
        startActivity(intent);
    }

    @Override
    public void onLongClick(View itemView, int position) {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getFilterParams();
        getFilteredOldHousesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                Intent intent = new Intent(OldHousesListActivity.this, OldHousesListMapActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
