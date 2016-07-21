package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.custom.imageView.ZoomImagePagerAdapter;

import java.util.ArrayList;

/**
 * Description: 放大浏览图片
 * <p/>
 * Author: dong
 * Date: 16/5/7
 */
public class ShowImageActivity extends BaseActivity {
    private ViewPager viewPager;
    private ZoomImagePagerAdapter mAdapter;
    private int index;
    private ArrayList<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        initToolbar();
        index = getIntExtra("index", 0);
        urlList = getIntent().getStringArrayListExtra("url");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mAdapter == null) {
            if (urlList != null) {
                mAdapter = new ZoomImagePagerAdapter(ShowImageActivity.this, urlList);
            }
        }
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
