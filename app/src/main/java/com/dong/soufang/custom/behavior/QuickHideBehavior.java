package com.dong.soufang.custom.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import com.dong.soufang.R;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class QuickHideBehavior extends CoordinatorLayout.Behavior {
    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = -1;

    private int mScrollingDirection;
    private int mScrollTrigger;
    private int mScrollDistance;
    private int mScrollThreshold;

    public QuickHideBehavior() {
    }

    public QuickHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        //当滑动距离大于二分之Toolbar高度,触发Behavior
        mScrollThreshold = a.getDimensionPixelSize(0, 0) / 2;
        a.recycle();
    }

}
