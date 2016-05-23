package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dong.soufang.GlobalData;
import com.dong.soufang.R;
import com.dong.soufang.bean.NewEstate;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.impl.OnListItemClickListener;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.List;

/**
 * Description: 新房列表页
 * <p/>
 * Author: dong
 * Date: 16/3/16
 */
public class NewEstatesListAdapter extends RecyclerView.Adapter<NewEstatesListAdapter.NewEstatesListViewHolder> {
    private Context context;
    private List<NewEstate> estateList;
    private OnListItemClickListener listener;

    public NewEstatesListAdapter(Context context, List<NewEstate> list) {
        this.context = context;
        this.estateList = list;
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public NewEstatesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_estate_list, null);
        return new NewEstatesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewEstatesListViewHolder holder, final int position) {
        NewEstate newEstate = estateList.get(position);
        LatLng latLng = new LatLng(newEstate.getLatitude(), newEstate.getLongitude());
        LatLng myLoc = new LatLng(GlobalData.LATITUDE, GlobalData.LONGITUDE);
        String distance = String.format("%.2f", DistanceUtil.getDistance(latLng, myLoc) / 1000);
        holder.tvName.setText(newEstate.getName());
        holder.tvAddress.setText(newEstate.getAddress());
        holder.tvDistrict.setText(newEstate.getDistrict());
        holder.tvPrice.setText(newEstate.getAvgPrice() + "元/平");
        holder.tvDistance.setText("距我" + distance + "km");
        ImageLoaderUtils.display(HttpApi.IMAGE_URL + newEstate.getAvatar(), holder.ivAvatar, R.mipmap.ic_launcher);
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
        if (estateList != null) {
            return estateList.size();
        }
        return 0;
    }

    class NewEstatesListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvDistrict, tvPrice, tvDistance;
        ImageView ivAvatar;

        public NewEstatesListViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_estate_name);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvDistrict = (TextView) itemView.findViewById(R.id.tv_district);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_avg_price);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance_to_me);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }


}
