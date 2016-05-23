package com.dong.soufang.custom.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dong.soufang.R;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/5/4
 */
public class PointGroup extends LinearLayout {
    private Context mContext;
    private int pointNumber;
    private View[] pointViews;
    private int current;

    public PointGroup(Context context, int pointNumber) {
        super(context);
        this.mContext = context;
        this.pointNumber = pointNumber;
        init();
    }

    public PointGroup(Context context, AttributeSet attrs, int pointNumber) {
        super(context, attrs);
        this.mContext = context;
        this.pointNumber = pointNumber;
        init();
    }

    private void init() {
        current = 0;
        setPointNumber();
    }

    private void setPointNumber() {
        if (pointNumber < 0) {
            return;
        }
        this.removeAllViews();
        pointViews = new View[pointNumber];
        for (int i = 0; i < pointNumber; i++) {
            ImageView point = new ImageView(mContext);
            point.setBackgroundResource(R.drawable.point_bg);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            point.setLayoutParams(params);
            pointViews[i] = point;
            pointViews[i].setEnabled(false);
            this.addView(point);
        }
        setCurrent(current);
    }

    public void setCurrent(int index) {
        for (int i = 0; i < pointNumber; i++) {
            pointViews[i].setEnabled(false);
        }
        pointViews[index].setEnabled(true);

    }
}
