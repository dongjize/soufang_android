package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.soufang.R;
import com.dong.soufang.bean.City;
import com.dong.soufang.impl.OnListItemClickListener;

import java.util.List;

/**
 * Description: 城市列表Adapter
 * <p/>
 * Author: dong
 * Date: 16/3/29
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {
    private Context mContext;
    private List<City> cityList;
    private OnListItemClickListener listener;

    public CityListAdapter(Context context, List<City> list) {
        this.mContext = context;
        this.cityList = list;
    }

    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_simple_list, null);
        return new CityListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CityListViewHolder holder, final int position) {
        City city = cityList.get(position);
        holder.tvCityName.setText(city.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(holder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cityList != null) {
            return cityList.size();
        }
        return 0;
    }

    class CityListViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName;

        public CityListViewHolder(View itemView) {
            super(itemView);
            tvCityName = (TextView) itemView.findViewById(R.id.tv_item_name);
        }
    }
}
