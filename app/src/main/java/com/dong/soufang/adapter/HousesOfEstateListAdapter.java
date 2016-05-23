package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.soufang.R;
import com.dong.soufang.bean.NewHouse;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.List;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 5/13/16
 */
public class HousesOfEstateListAdapter extends RecyclerView.Adapter<HousesOfEstateListAdapter.HousesViewHolder> {
    private Context context;
    private List<NewHouse> newHouseList;
    private OnListItemClickListener mListener;

    public HousesOfEstateListAdapter(Context context, List<NewHouse> newHouseList) {
        this.context = context;
        this.newHouseList = newHouseList;
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public HousesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_new_houses_list, null);
        return new HousesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HousesViewHolder holder, final int position) {
        NewHouse newHouse = newHouseList.get(position);
        ImageLoaderUtils.display(HttpApi.IMAGE_URL + newHouse.getAvatar(), holder.ivAvatar, R.mipmap.ic_launcher);
        holder.tvHouseName.setText(newHouse.getName());
        holder.tvPrice.setText(newHouse.getPrice() + "元/平");
        holder.tvAreaType.setText(newHouse.getArea() + " " + newHouse.getHouseType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(holder.itemView, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(holder.itemView, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return newHouseList.size();
    }

    class HousesViewHolder extends RecyclerView.ViewHolder {
        TextView tvHouseName, tvPrice, tvAreaType, tvHouseType;
        ImageView ivAvatar;

        public HousesViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvHouseName = (TextView) itemView.findViewById(R.id.tv_house_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_unit_price);
            tvAreaType = (TextView) itemView.findViewById(R.id.tv_area_and_type);
        }
    }
}
