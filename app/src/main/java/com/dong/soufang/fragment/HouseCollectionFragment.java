package com.dong.soufang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.HouseCollectionAdapter;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 房产收藏列表
 * <p/>
 * Author: dong
 * Date: 16/3/29
 */
public class HouseCollectionFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = HouseCollectionFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private HouseCollectionAdapter mAdapter;
    private List collections;
    private LinearLayout filterCollection, selectSequence;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_estates_collection, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterCollection = (LinearLayout) contentView.findViewById(R.id.filter_collection);
        selectSequence = (LinearLayout) contentView.findViewById(R.id.select_sequence);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        swipeRefreshLayout.setOnRefreshListener(this);
        filterCollection.setOnClickListener(this);
        selectSequence.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        getHouseCollectionList();
    }

    private void getHouseCollectionList() {
        swipeRefreshLayout.setRefreshing(false);

        HashMap<String, Object> params = new HashMap<>();
        //TODO
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetUserEstateCollectionsApi, params);
        ((BaseActivity) getActivity()).httpHandler.getUserEstatesCollection(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                collections = (List) result.getData();
                if (mAdapter == null) {
                    recyclerView.setAdapter(mAdapter = new HouseCollectionAdapter(context, collections));
                } else {
                    mAdapter.notifyDataSetChanged();
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
        switch (v.getId()) {
            case R.id.filter_collection:
                showToast("筛选,待实现");
                break;
            case R.id.select_sequence:
                showToast("排序方案,待实现");
                break;
        }
    }

}
