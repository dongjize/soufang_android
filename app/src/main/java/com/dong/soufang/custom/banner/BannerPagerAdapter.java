package com.dong.soufang.custom.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.soufang.R;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/5/4
 */
public class BannerPagerAdapter extends PagerAdapter {
    private List<ImageView> viewList;
    private LayoutInflater mInflater;
    private OnBannerViewClickListener mListener;
    private List<BannerItem> bannerItemList;
    private BannerViewPager bannerViewPager;
    private ViewPager viewPager;
    private PointGroup pointGroup;

    private int imageMode;
    private final int LOCAL_IMAGE = 0;
    private final int INTERNET_IMAGE = 1;

    protected Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public BannerPagerAdapter(Context context, BannerViewPager pager, List<BannerItem> list, List<ImageView> imageViewList) {
        init(context, pager);
        imageMode = LOCAL_IMAGE;
        if (list != null) {
            bannerItemList = list;
        }
        viewList = imageViewList;
        this.pointGroup = new PointGroup(context, viewList.size());
        this.bannerViewPager.setPointGroup(pointGroup);

    }

    public BannerPagerAdapter(Context context, BannerViewPager pager, List<BannerItem> list) {
        imageMode = INTERNET_IMAGE;
        init(context, pager);
        viewList = new ArrayList<>();
        if (list != null) {
            bannerItemList = list;
            for (int i = 0; i < list.size(); i++) {
                ImageView imageView = new ImageView(context);
                viewList.add(imageView);
            }
        }
        this.pointGroup = new PointGroup(context, viewList.size());
        this.bannerViewPager.setPointGroup(pointGroup);

    }

    private void init(Context context, BannerViewPager pager) {
        this.bannerViewPager = pager;
        this.viewPager = bannerViewPager.getViewPager();
        this.bannerViewPager.setOnBannerPageChangeListener();
        mInflater = LayoutInflater.from(context);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (viewPager.getCurrentItem() == viewList.size() - 1) {
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                handler.sendEmptyMessageDelayed(0, bannerViewPager.getInterval());
            }
        };
    }

    public void setOnBannerViewClickListener(OnBannerViewClickListener listener) {
        this.mListener = listener;
    }

    public interface OnBannerViewClickListener {
        void onBannerClick(View itemView, int position);
    }

    @Override
    public int getCount() {
        return bannerItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = mInflater.inflate(R.layout.item_viewpager, null);
        RelativeLayout picLayout = (RelativeLayout) view.findViewById(R.id.picture_layout);
        ImageView imageView = viewList.get(position);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ViewGroup parent = (ViewGroup) imageView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        picLayout.addView(imageView);

        BannerItem item = bannerItemList.get(position);

        if (imageMode == INTERNET_IMAGE) {
            ImageLoaderUtils.display(HttpApi.IMAGE_URL + item.getPicUrl(), imageView, R.mipmap.ic_launcher);
        }

        TextView textView = (TextView) view.findViewById(R.id.tv_intro);
        if (!TextUtils.isEmpty(item.getIntro())) {
            textView.setText(item.getIntro());
        } else {
            textView.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBannerClick(view, position);
                }
            }
        });
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewList.get(position));
    }
}
