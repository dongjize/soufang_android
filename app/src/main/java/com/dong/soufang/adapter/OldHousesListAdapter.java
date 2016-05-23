package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.soufang.R;
import com.dong.soufang.bean.OldHouse;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.List;

/**
 * Description: 二手房adapter
 * <p/>
 * Author: dong
 * Date: 16/3/21
 */
public class OldHousesListAdapter extends RecyclerView.Adapter<OldHousesListAdapter.OldHousesListViewHolder> {
    private Context context;
    private List<OldHouse> oldHouseList;
    private OnListItemClickListener listener;

    public OldHousesListAdapter(Context context, List<OldHouse> list) {
        this.context = context;
        this.oldHouseList = list;
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public OldHousesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_old_houses_list, null);
        return new OldHousesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OldHousesListViewHolder holder, final int position) {
        OldHouse oldHouse = oldHouseList.get(position);
        holder.tvTitle.setText(oldHouse.getTitle());
        holder.tvAreaAndType.setText(oldHouse.getArea() + "m2  " + oldHouse.getHouseType());
        holder.tvSalePrice.setText(oldHouse.getSalePrice() + "万元");
        holder.tvEstateName.setText(oldHouse.getEstateName());
        ImageLoaderUtils.display(HttpApi.IMAGE_URL + oldHouse.getAvatar(), holder.ivAvatar, R.mipmap.ic_launcher);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(holder.itemView, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongClick(holder.itemView, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (oldHouseList != null) {
            return oldHouseList.size();
        }
        return 0;
    }

    class OldHousesListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAreaAndType, tvSalePrice, tvEstateName;
        ImageView ivAvatar;

        public OldHousesListViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAreaAndType = (TextView) itemView.findViewById(R.id.tv_area_and_type);
            tvSalePrice = (TextView) itemView.findViewById(R.id.tv_sale_price);
            tvEstateName = (TextView) itemView.findViewById(R.id.tv_estate_name);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

}
