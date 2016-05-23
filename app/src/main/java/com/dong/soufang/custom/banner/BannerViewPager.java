package com.dong.soufang.custom.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.dong.soufang.R;

import java.lang.reflect.Field;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/5/5
 */
public class BannerViewPager extends RelativeLayout {
    private ViewPager viewPager;
    private LinearLayout pointsLayout;
    private PointGroup mPointGroup;
    private OnBannerPageChangeListener mListener;
    private BannerViewPagerScroller mScroller;

    private int interval = 4000; //自动播放间隔
    private int scrollDuration = 2000; //滑动时间
    private boolean isAllowAutoScroll = true; //是否自动滑动

    public BannerViewPager(Context context) {
        this(context, null, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.BannerViewPager, defStyleAttr, 0);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (ta.getIndex(i)) {
                case R.styleable.BannerViewPager_interval:
                    interval = ta.getInt(attr, interval);
                    break;
                case R.styleable.BannerViewPager_allowAutoScroll:
                    isAllowAutoScroll = ta.getBoolean(attr, isAllowAutoScroll);
                    break;
                case R.styleable.BannerViewPager_scrollDuration:
                    scrollDuration = ta.getInt(attr, scrollDuration);
                    break;
            }
        }
        ta.recycle();

    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_banner_viewpager, this, true);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        pointsLayout = (LinearLayout) this.findViewById(R.id.points_layout);

        if (scrollDuration != 2000) {
            setDuration(scrollDuration);
        } else {
            setDuration();
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setAdapter(BannerPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        if (isAllowAutoScroll) {
            Handler handler = adapter.getHandler();
            handler.sendEmptyMessageDelayed(0, interval);
        }
    }

    public void setPointGroup(PointGroup pointGroup) {
        this.mPointGroup = pointGroup;
        pointsLayout.addView(mPointGroup);
    }

    public void setDuration() {
        try {
            Class<?> viewPager = ViewPager.class;
            Field scroller = viewPager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewPager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            if (mScroller == null) {
                mScroller = new BannerViewPagerScroller(getContext());
            }
            scroller.set(this.viewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setDuration(int duration) {
        setDuration();
        mScroller.setDuration(duration);
    }

    public int getInterval() {
        return interval;
    }

    class BannerViewPagerScroller extends Scroller {
        private int mDuration = scrollDuration;

        public BannerViewPagerScroller(Context context) {
            super(context);
        }

        public BannerViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }
    }

    private class OnBannerPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            if (mPointGroup != null) {
                mPointGroup.setCurrent(position);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void setOnBannerPageChangeListener() {
        this.mListener = new OnBannerPageChangeListener();
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(mListener);
        }
    }
}
