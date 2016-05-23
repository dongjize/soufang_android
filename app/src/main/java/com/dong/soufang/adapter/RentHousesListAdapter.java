package com.dong.soufang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: 租房adapter
 * <p>
 * Author: dong
 * Date: 16/3/21
 */
public class RentHousesListAdapter extends RecyclerView.Adapter<RentHousesListAdapter.RentHousesListViewHolder> {

    @Override
    public RentHousesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RentHousesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RentHousesListViewHolder extends RecyclerView.ViewHolder {

        public RentHousesListViewHolder(View itemView) {
            super(itemView);
        }
    }
}
