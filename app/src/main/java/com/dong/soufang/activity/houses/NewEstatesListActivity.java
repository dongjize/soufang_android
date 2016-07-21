package com.dong.soufang.activity.houses;

import android.content.Intent;
import android.os.Bundle;
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

import com.dong.soufang.DividerItemDecoration;
import com.dong.soufang.GlobalData;
import com.dong.soufang.util.map.HouseTypeMap;
import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.activity.map.NewEstatesListMapActivity;
import com.dong.soufang.adapter.FilterParamsAdapter;
import com.dong.soufang.adapter.NewEstatesListAdapter;
import com.dong.soufang.bean.District;
import com.dong.soufang.bean.NewEstate;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.IoUtils;
import com.dong.soufang.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 新房列表
 * <p>
 * Author: dong
 * Date: 16/4/17
 */
public class NewEstatesListActivity extends BaseActivity implements View.OnClickListener,
        OnListItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NewEstatesListActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewEstatesListAdapter mAdapter;
    private List<NewEstate> newEstateList;

    private String district, type, priceLow, priceHigh;
    private LinearLayout districtBtn, typeBtn, priceBtn;
    private LinearLayout floatLayer;
    private ListView listView;

    private HashMap<String, Object> filterParams;
    private List<District> districts;
    private List<String> districtParams, typeParams, priceParams;
    private String filterUrl;
    private boolean isListOpen = false;
    private FilterParamsAdapter fAdapter;
    private int currentList = -1;
    private String parameters = "";

    private final String TABLE_NAME = "districts.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estates_list);
        initToolbar();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        districtBtn = (LinearLayout) findViewById(R.id.district);
        districtBtn.setOnClickListener(this);
        typeBtn = (LinearLayout) findViewById(R.id.type);
        typeBtn.setOnClickListener(this);
        priceBtn = (LinearLayout) findViewById(R.id.price);
        priceBtn.setOnClickListener(this);
        floatLayer = (LinearLayout) findViewById(R.id.float_layer);
        listView = (ListView) floatLayer.findViewById(R.id.float_list_view);
        newEstateList = new ArrayList<>();

        getFilterParams();
        getNewEstatesList();

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //How many roads must a man walk down
    }


    /**
     * 获取到参数列表
     */
    private void getFilterParams() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetFilterVariablesApi, params);
        httpHandler.getNewEstatesFilters(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                filterParams = (HashMap<String, Object>) result.getData();
                districtParams = new ArrayList<>();
                districts = (List<District>) filterParams.get("districts");
                for (District district : districts) {
                    districtParams.add(district.getName());
                }
                districtParams.add(0, "不限");
                typeParams = (List<String>) filterParams.get("types");
                priceParams = (List<String>) filterParams.get("prices");

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });

    }


    /**
     * 获取新房列表
     */
    private void getNewEstatesList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("city_code", GlobalData.CITY);
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetNewEstatesListApi, params);
        httpHandler.getNewEstatesList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                newEstateList = (List<NewEstate>) result.getData();
                if (newEstateList != null) {
                    if (mAdapter == null) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(NewEstatesListActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(mAdapter = new NewEstatesListAdapter(NewEstatesListActivity.this, newEstateList));
                        recyclerView.addItemDecoration(new DividerItemDecoration(NewEstatesListActivity.this,
                                DividerItemDecoration.VERTICAL_LIST));
                        mAdapter.setOnListItemClickListener(NewEstatesListActivity.this);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }


    /**
     * 获取过滤后的房产列表
     */
    private void getFilteredEstatesList() {
        HashMap<String, Object> params = new HashMap<>();
        parameters = (TextUtils.isEmpty(district) ? "" : "a" + district)
                + (TextUtils.isEmpty(type) ? "" : "__b" + type)
                + ((TextUtils.isEmpty(priceLow) || TextUtils.isEmpty(priceHigh)) ? "" : "__c" + priceLow + "d" + priceHigh);
        if (parameters.startsWith("__")) {
            parameters = parameters.substring(2);
        }
        if (TextUtils.isEmpty(parameters)) {
            filterUrl = HttpApi.GetNewEstatesListApi;
        } else {
            filterUrl = HttpApi.GetFilteredNewEstatesApi + parameters;
        }

        RequestBean requestBean = new RequestBean(TAG, filterUrl, params);
        httpHandler.getFilteredNewEstates(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                newEstateList = (List<NewEstate>) result.getData();
                if (newEstateList != null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(NewEstatesListActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(mAdapter = new NewEstatesListAdapter(NewEstatesListActivity.this, newEstateList));
                    mAdapter.setOnListItemClickListener(NewEstatesListActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_map_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                Intent intent = new Intent(NewEstatesListActivity.this, NewEstatesListMapActivity.class);
                intent.putExtra("district", district);
                intent.putExtra("type", type);
                intent.putExtra("price_low", priceLow);
                intent.putExtra("price_high", priceHigh);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                                String s = IoUtils.readFromAssets(NewEstatesListActivity.this, TABLE_NAME);
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
                            view.findViewById(R.id.iv_red_tick).setVisibility(View.VISIBLE);
                            getFilteredEstatesList();
                            floatLayer.setVisibility(View.GONE);
                            swipeRefreshLayout.setEnabled(true);
                        }
                    });

                }
                break;
            case R.id.type:
                if (currentList == 1) {
                    floatLayer.setVisibility(View.GONE);
                    swipeRefreshLayout.setEnabled(true);
                    currentList = -1;
                } else {
                    if (typeParams != null) {
                        setArrayAdapter(typeParams);
                    }
                    currentList = 1;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                type = HouseTypeMap.getHouseType(typeParams.get(position));
                            } else {
                                type = "";
                            }
                            view.findViewById(R.id.iv_red_tick).setVisibility(View.VISIBLE);
                            getFilteredEstatesList();
                            floatLayer.setVisibility(View.GONE);
                            swipeRefreshLayout.setEnabled(true);
                        }
                    });
                }
                break;
            case R.id.price:
                if (currentList == 2) {
                    floatLayer.setVisibility(View.GONE);
                    currentList = -1;
                } else {
                    setArrayAdapter(priceParams);
                    currentList = 2;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                String s = priceParams.get(position);
                                String[] sArr = s.split("-");
                                priceLow = sArr[0];
                                priceHigh = sArr[1].substring(0, sArr[1].length() - 1); //去掉字符串中的"元"字
                            } else {
                                priceLow = "";
                                priceHigh = "";
                            }
                            getFilteredEstatesList();
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
        NewEstate newEstate = newEstateList.get(position);
        Intent intent = new Intent(NewEstatesListActivity.this, NewEstateDetailActivity.class);
        intent.putExtra("id", newEstate.getId());
        intent.putExtra("title", newEstate.getName());
        startActivity(intent);
    }

    @Override
    public void onLongClick(View itemView, int position) {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getFilterParams();
        getFilteredEstatesList();
    }
}
