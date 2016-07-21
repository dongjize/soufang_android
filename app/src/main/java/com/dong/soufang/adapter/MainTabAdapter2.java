package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 7/21/16
 */
public class MainTabAdapter2 extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<Fragment> fragmentList;
    private int[] titleList;

    public MainTabAdapter2(Context context, FragmentManager fm, List<Fragment> fragmentList, int[] titleList) {
        super(fm);
        this.mContext = context;
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public View getTabView(int position) {
        ImageView iv = new ImageView(mContext);
        iv.setImageResource(titleList[position]);
        return iv;
    }
}
