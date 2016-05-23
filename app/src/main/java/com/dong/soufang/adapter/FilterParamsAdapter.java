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
 * Date: 5/13/16
 */
public class FilterParamsAdapter extends BaseAdapter {
    private Context context;
    private List<String> mList;

    public FilterParamsAdapter(Context context, List<String> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_params_list, null);
        TextView tvParam = (TextView) convertView.findViewById(R.id.tv_param_name);
        tvParam.setText(mList.get(position));
        return convertView;
    }
}
