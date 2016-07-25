package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.soufang.R;

import java.util.List;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 7/21/16
 */
public class MainTabAdapter2 extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragmentList;
    private int[] mList;
    private List<String> titleList;

    public MainTabAdapter2(FragmentManager fm, Context context, List<Fragment> fragments, List<String> titles, int[] list) {
        super(fm);
        this.context = context;
        this.fragmentList = fragments;
        this.mList = list;
        this.titleList = titles;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_main_tab, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_tab);
        ImageView imageView = (ImageView) v.findViewById(R.id.iv_tab);
        textView.setText(titleList.get(position));
        imageView.setImageResource(mList[position]);
        return v;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
