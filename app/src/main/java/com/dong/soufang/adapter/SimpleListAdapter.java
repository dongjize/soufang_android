package com.dong.soufang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dong.soufang.R;

import java.util.List;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/3/29
 */
public class SimpleListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;

    public SimpleListAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_simple_list, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
        tvName.setText(mList.get(position));
        return convertView;
    }
}
