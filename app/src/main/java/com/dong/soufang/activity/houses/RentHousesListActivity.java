package com.dong.soufang.activity.houses;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.impl.OnListItemClickListener;

/**
 * Description: 找租房
 * <p/>
 * Author: dong
 * Date: 16/3/20
 */
public class RentHousesListActivity extends BaseActivity implements View.OnClickListener,
        OnListItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = RentHousesListActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_houses_list);
        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(View itemView, int position) {

    }

    @Override
    public void onLongClick(View itemView, int position) {

    }

    @Override
    public void onRefresh() {

    }
}
