package com.dong.soufang.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.dong.soufang.R;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    //菜单的默认显示位置:右下
    private Position mPosition = Position.RIGHT_BOTTOM;
    //菜单的显示半径,默认100dp
    private int mRadius = 100;
    //用户点击的按钮
    private View mButton;
    //当前ArcMenu的状态
    private Status mCurrentStatus = Status.CLOSE;
    //回调接口
    private OnMenuItemClickListener onMenuItemClickListener;

    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }

    //状态的枚举
    private enum Status {
        OPEN, CLOSE
    }

    //菜单显示的位置,默认右下
    private enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //dp-->px
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius,
                getResources().getDisplayMetrics());

        //用下面的方法获取自定义属性数组
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ArcMenu_position:
                    int val = a.getInt(attr, 0);
                    switch (val) {
                        case 0:
                            mPosition = Position.LEFT_TOP;
                            break;
                        case 1:
                            mPosition = Position.LEFT_BOTTOM;
                            break;
                        case 2:
                            mPosition = Position.RIGHT_TOP;
                            break;
                        case 3:
                            mPosition = Position.RIGHT_BOTTOM;
                            break;
                    }
                    break;
                case R.styleable.ArcMenu_radius:
                    //dp-->px
                    mRadius = a.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            mRadius, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutButton();
            int count = getChildCount();
            /*
            设置所有子控件的位置
             */
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i + 1);
                child.setVisibility(View.GONE); //设置所有孩子隐藏

                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
                // childview width
                int cWidth = child.getMeasuredWidth();
                // childview height
                int cHeight = child.getMeasuredHeight();

                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }

                child.layout(cl, ct, cl + cWidth, ct + cHeight);
            }
        }
    }

    /**
     * 第一个子元素为按钮,为按钮布局且初始化点击事件
     */
    private void layoutButton() {
        View cButton = getChildAt(0);
        cButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int width = cButton.getMeasuredWidth();
        int height = cButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        cButton.layout(l, t, l + width, t + height);
    }

    /**
     * 为按钮添加点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        mButton = findViewById(R.id.arc_button);
        if (mButton == null) {
            mButton = getChildAt(0);
        }
        //rotateView(mButton, 0f, 270f, 300);
        toggleMenu(300);
    }


    /**
     * 设置主按钮点击旋转动画
     *
     * @param view
     * @param fromDegrees
     * @param toDegrees
     * @param durationMillis
     */
    public void rotateView(View view, float fromDegrees, float toDegrees, int durationMillis) {
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    /**
     * 菜单的打开与关闭
     *
     * @param durationMillis
     */
    public void toggleMenu(int durationMillis) {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);

            int xflag = 1;
            int yflag = 1;
            //左上或左下
            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM)
                xflag = -1;
            //左上或右上
            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP)
                yflag = -1;
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

            AnimationSet animSet = new AnimationSet(true);
            Animation anim = null;
            if (mCurrentStatus == Status.CLOSE) {
                // to open
                animSet.setInterpolator(new OvershootInterpolator(2F));
                anim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                // to close
                anim = new TranslateAnimation(0f, xflag * cl, 0f, yflag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE)
                        childView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            anim.setFillAfter(true);
            anim.setDuration(durationMillis);
            anim.setStartOffset((i * 100) / (count - 1)); //设置延迟时间
            //RotateAnimation rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
            // Animation.RELATIVE_TO_SELF, 0.5f);

            animSet.addAnimation(anim);
            childView.startAnimation(animSet);

            final int index = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onClick(childView, index - 1);
                    menuItemAnim(index - 1);
                    changeStatus();
                }
            });
        }
        changeStatus();

    }

    /**
     * 更改状态
     */
    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }

    /**
     * 点击子按钮动画,点击的放大,其他的缩小消失
     *
     * @param item
     */
    private void menuItemAnim(int item) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (i == item) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }
    }

    /**
     * 缩小消失的动画
     *
     * @param durationMillis
     * @return
     */
    private Animation scaleSmallAnim(int durationMillis) {
        Animation anim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(durationMillis);
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 放大透明的动画
     *
     * @param durationMillis
     * @return
     */
    private Animation scaleBigAnim(int durationMillis) {
        AnimationSet set = new AnimationSet(true);
        Animation scale = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation alpha = new AlphaAnimation(1, 0);
        set.addAnimation(scale);
        set.addAnimation(alpha);
        set.setDuration(durationMillis);
        set.setFillAfter(true);
        return set;
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public Position getmPosition() {
        return mPosition;
    }

    public void setmPosition(Position mPosition) {
        this.mPosition = mPosition;
    }

    public Status getmCurrentStatus() {
        return mCurrentStatus;
    }

    public void setmCurrentStatus(Status mCurrentStatus) {
        this.mCurrentStatus = mCurrentStatus;
    }

    public OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }
}