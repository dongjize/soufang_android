package com.dong.soufang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dong.soufang.R;
import com.dong.soufang.adapter.RentHousesListAdapter;
import com.dong.soufang.bean.RentHouse;

import java.util.List;

/**
 * Description: 租房列表Fragment
 * <p>
 * Author: dong
 * Date: 16/3/20
 */
public class RentHousesListFragment extends BaseFragment {
    private static final String TAG = RentHousesListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private RentHousesListAdapter mAdapter;
    private List<RentHouse> rentHouseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_rent_houses, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        getRentHousesList();

    }

    private void getRentHousesList() {

    }
}
