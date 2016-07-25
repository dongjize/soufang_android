package com.dong.soufang.custom.tabLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 7/22/16
 */
public class DividerTabLayout extends TabLayout {
    private Paint mPaint;
    private TabLayout mTabLayout;

    private int mWidth, mHeight;

    private boolean isDivided = true;

    public DividerTabLayout(Context context) {
        super(context);
        init();
    }

    public DividerTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DividerTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTabLayout = this;
        mPaint = new Paint();
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        mWidth = totalWidth / getTabCount();
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDivided) {
            int count = mTabLayout.getTabCount();
            for (int i = 0; i < count; i++) {
                canvas.drawLine((i + 1) * mWidth, 0, (i + 1) * mWidth, getMeasuredHeight(), mPaint);
            }
        }
    }

    public int getDividerColor() {
        return mPaint.getColor();
    }

    public void setDividerColor(int colorInt) {
        mPaint.setColor(colorInt);
    }

    public boolean isDivided() {
        return isDivided;
    }

    public void setDivided(boolean divided) {
        isDivided = divided;
    }
}
