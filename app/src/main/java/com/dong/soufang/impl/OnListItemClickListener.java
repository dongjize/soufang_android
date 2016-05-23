package com.dong.soufang.impl;

import android.view.View;

/**
 * Description: 列表点击回调接口
 * <p>
 * Author: dong
 * Date: 16/3/16
 */
public interface OnListItemClickListener {
    void onClick(View itemView, int position);

    void onLongClick(View itemView, int position);
}
