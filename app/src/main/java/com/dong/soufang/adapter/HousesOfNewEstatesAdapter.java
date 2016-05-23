package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.soufang.R;
import com.dong.soufang.bean.House;
import com.dong.soufang.bean.NewHouse;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.List;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/4/6
 */
public class HousesOfNewEstatesAdapter extends RecyclerView.Adapter<HousesOfNewEstatesAdapter.HouseViewHolder> {
    private Context context;
    private List<NewHouse> houseList;
    private OnListItemClickListener listener;

    public HousesOfNewEstatesAdapter(Context context, List<NewHouse> houseList) {
        this.context = context;
        this.houseList = houseList;
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_horizontal_house, null);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HouseViewHolder holder, final int position) {
        House house = houseList.get(position);
        ImageLoaderUtils.display(HttpApi.IMAGE_URL + house.getPhotos().get(0), holder.ivAvatar, R.mipmap.ic_launcher);
        holder.tvArea.setText(house.getArea() + "平米");
        holder.tvHouseType.setText(house.getHouseType());
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
        if (houseList != null) {
            return houseList.size();
        }
        return 0;
    }

    class HouseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvArea, tvHouseType;

        public HouseViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_house);
            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
            tvHouseType = (TextView) itemView.findViewById(R.id.tv_house_type);
        }
    }
}
